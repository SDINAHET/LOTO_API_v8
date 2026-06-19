#!/bin/bash
set -e

# ========================
# CONFIG
# ========================
PORT_STATIC=5500
PORT_SPRING=8082
PORT_AI=8091

STATIC_DIR="src/main/resources/static"

# ✅ Mets true seulement quand tu veux lancer l'AI
AI_ENABLED=false

# ========================
# HELPERS
# ========================
open_browser() {
  local URL="$1"
  if command -v powershell.exe >/dev/null 2>&1; then
    powershell.exe start "$URL" >/dev/null 2>&1 || true
    echo "   🪟 Ouverture demandée côté Windows: $URL"
  else
    echo "➡️ Ouvre: $URL"
  fi
}

# ========================
# AI SERVICE
# ========================

start_ai_service() {
  echo "==> AI service (8091)"

  if [ "$AI_ENABLED" != "true" ]; then
    echo "   ⏭️ AI désactivée (AI_ENABLED=false)"
    return 0
  fi

  if lsof -i :"$PORT_AI" >/dev/null 2>&1; then
    echo "   ⚠️ Port $PORT_AI déjà utilisé (skip)"
    return 0
  fi

  nohup python3 -m uvicorn ai:app --host 0.0.0.0 --port "$PORT_AI" \
    >/tmp/ai_8091.log 2>&1 & disown

  for _ in {1..20}; do
    if curl -s "http://localhost:$PORT_AI/health" >/dev/null 2>&1; then
      echo "   ✅ AI OK"
      return 0
    fi
    sleep 1
  done

  echo "   ⚠️ AI lancé mais /health ne répond pas"
}

export DOCKER_HOST=unix:///var/run/docker.sock

# ========================
# SERVICES SYSTEM
# ========================
echo "==> MongoDB"
sudo service mongod start >/dev/null 2>&1 || true

echo "==> PostgreSQL"
sudo service postgresql start >/dev/null 2>&1 || true

# ========================
# FRONT STATIC
# ========================
echo "==> Front static (5500)"
if ! lsof -i :"$PORT_STATIC" >/dev/null 2>&1; then
  (cd "$STATIC_DIR" && nohup python3 -m http.server "$PORT_STATIC" \
    >/tmp/static_http.log 2>&1 & disown)
  sleep 1
fi



# # ========================
# # SPRING BOOT
# # ========================
# echo "==> Build Spring Boot"
# mvn clean install
# # mvn clean install -DskipTests

# echo "==> Démarrage Spring Boot"
# nohup mvn spring-boot:run \
#   -Dspring-boot.run.arguments="--server.port=$PORT_SPRING" \
#   >/tmp/spring_8082.log 2>&1 & disown

# echo "==> Attente Spring Boot..."
# for _ in {1..60}; do
#   if curl -s "http://localhost:$PORT_SPRING/actuator/health" >/dev/null 2>&1; then
#     echo "   ✅ Spring UP"
#     break
#   fi
#   sleep 2
# done

# # open_browser "http://localhost:$PORT_SPRING/swagger-ui/index.html"
# # open_browser "http://localhost:$PORT_STATIC/admin-login.html"
# open_browser "http://localhost:$PORT_STATIC/index.html"

# ========================
# SPRING BOOT
# ========================
echo "==> Build Spring Boot"
# mvn clean install
# mvn clean install -DskipTests

echo "==> Build Spring Boot"

set +e
mvn clean install | tee /tmp/maven_loto_tests.log
MAVEN_EXIT_CODE=${PIPESTATUS[0]}
set -e

echo ""
echo "========================================"
echo "📊 RÉSUMÉ TESTS BACKEND / E2E API"
echo "========================================"

grep "Tests run:" /tmp/maven_loto_tests.log | tail -500 || true

echo ""
echo "========================================"
echo "📊 SYNTHÈSE MAVEN"
echo "========================================"
echo ""

if [ "$MAVEN_EXIT_CODE" -eq 0 ]; then
  echo "✅ Tests backend terminés"
  echo "   Maven/JUnit/Failsafe : OK"
  echo "   E2E API Backend      : OK"
  echo "   JaCoCo couverture    : OK"
else
  echo "❌ Tests backend en échec"
  echo "   Maven/JUnit/Failsafe : FAILED"
  echo "   E2E API Backend      : NON VALIDÉ"
  echo "   JaCoCo couverture    : NON GÉNÉRÉE"
  echo ""
  echo "👉 Voir les logs Maven au-dessus."
  exit "$MAVEN_EXIT_CODE"
fi

# grep -E "BUILD SUCCESS|Tests run:|Failures:|Errors:|Skipped:|All coverage checks" /tmp/maven_loto_tests.log | tail -20 || true

echo "==> Démarrage Spring Boot"
nohup mvn spring-boot:run \
  -Dspring-boot.run.arguments="--server.port=$PORT_SPRING" \
  >/tmp/spring_8082.log 2>&1 & disown

echo "==> Attente Spring Boot..."
SPRING_READY=false

for _ in {1..60}; do
  if curl -s "http://localhost:$PORT_SPRING/actuator/health" >/dev/null 2>&1; then
    echo "   ✅ Spring UP"
    SPRING_READY=true
    break
  fi
  sleep 2
done

if [ "$SPRING_READY" != "true" ]; then
  echo "   ❌ Spring Boot ne répond pas sur /actuator/health"
  echo "   Voir logs : /tmp/spring_8082.log"
  exit 1
fi

open_browser "http://localhost:$PORT_STATIC/index.html"

# ========================
# TESTS E2E PLAYWRIGHT
# ========================
echo "==> Tests E2E Playwright"

if [ -f "package.json" ]; then
  npm run test:e2e
  echo "   ✅ Tests Playwright OK"
else
  echo "   ⏭️ package.json introuvable"
fi

# ========================
# AI (optionnelle)
# ========================
start_ai_service

echo "==> 🚀 Stack complète prête"
echo "   Front  : http://localhost:5500/admin-login.html"
echo "   Admin  : http://localhost:5500/admin/dashboard.html"
echo "   API    : http://localhost:8082"
echo "   Swagger: http://localhost:8082/swagger-ui/index.html"
