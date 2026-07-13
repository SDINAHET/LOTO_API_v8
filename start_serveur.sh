# #!/bin/bash

# echo "=== Arrêt des conteneurs ==="
# docker compose down

# echo "=== Démarrage du serveur ==="
# docker compose -f docker-compose_serveur.yml up -d --build

# echo "=== Terminé ==="
# docker ps

#!/bin/bash

echo "=== Arrêt ==="
docker compose -f docker-compose_serveur.yml down

echo "=== Reconstruction et démarrage ==="
docker compose -f docker-compose_serveur.yml up -d --build

echo "=== Conteneurs en cours ==="
docker compose -f docker-compose_serveur.yml ps
