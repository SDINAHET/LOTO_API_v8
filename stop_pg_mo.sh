#!/bin/bash

echo "🛑 Arrêt des bases de données..."

# Stop MongoDB
if systemctl is-active --quiet mongod; then
    echo "➡️  MongoDB en cours d'arrêt..."
    sudo systemctl stop mongod
    echo "✅ MongoDB arrêté"
else
    echo "⚠️  MongoDB déjà arrêté"
fi

# Stop PostgreSQL
if systemctl is-active --quiet postgresql; then
    echo "➡️  PostgreSQL en cours d'arrêt..."
    sudo systemctl stop postgresql
    echo "✅ PostgreSQL arrêté"
else
    echo "⚠️  PostgreSQL déjà arrêté"
fi

echo "🎯 Terminé"
