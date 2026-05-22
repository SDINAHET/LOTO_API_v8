#!/bin/sh

echo "⏳ Attente Mongo..."
sleep 5

mongosh <<EOF
use admin

if (!db.getUser("mongo")) {
  print("👤 Création utilisateur mongo...");
  db.createUser({
    user: "mongo",
    pwd: "mongo",
    roles: [{ role: "root", db: "admin" }]
  });
} else {
  print("✅ Utilisateur déjà présent");
}
EOF
