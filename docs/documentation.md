# Documentation d’exploitation – Loto Tracker API

## Objectif

Cette documentation décrit l’exploitation technique du projet **Loto Tracker API**.

Elle a pour objectif de faciliter :

* l’installation du projet ;
* le déploiement sur un serveur ;
* la maintenance courante ;
* la sauvegarde des données ;
* la reprise du projet par un autre développeur ou administrateur système.

Le projet est actuellement fonctionnel avec une architecture basée sur **Spring Boot**, **PostgreSQL**, **MongoDB**, **Docker**, **Apache** et une interface frontend en **HTML/CSS/JavaScript**.

---

# 1. Architecture générale

## 1.1 Vue d’ensemble

Loto Tracker API repose sur une architecture web en plusieurs couches.

```text
Utilisateur
   │
   ▼
Navigateur Web
   │
   ▼
Frontend HTML / CSS / JavaScript / Bootstrap
   │
   ▼
Apache2 Reverse Proxy HTTPS
   │
   ▼
Backend Spring Boot Java 21
   │
   ├── PostgreSQL
   │     ├── utilisateurs
   │     ├── tickets
   │     └── gains
   │
   └── MongoDB
         └── historique des tirages du Loto
```

## 1.2 Frontend

Le frontend est composé de pages statiques développées avec :

* HTML5 ;
* CSS3 ;
* JavaScript ;
* Bootstrap 5.

Il permet notamment :

* l’affichage des derniers tirages ;
* la consultation des statistiques ;
* la gestion des tickets utilisateur ;
* l’accès au profil utilisateur ;
* l’affichage de certaines informations d’administration.

Le frontend communique avec le backend via des requêtes HTTP vers les endpoints REST de l’API.

## 1.3 Backend

Le backend est développé avec :

* Java 21 ;
* Spring Boot ;
* Spring Security ;
* Spring Data JPA ;
* Spring Data MongoDB.

Il expose une API REST permettant de gérer :

* les utilisateurs ;
* l’authentification ;
* les tickets joués ;
* les gains ;
* l’historique des tirages ;
* les statistiques.

## 1.4 Bases de données

Le projet utilise deux systèmes de stockage.

### PostgreSQL

PostgreSQL est utilisé pour les données relationnelles :

* comptes utilisateurs ;
* tickets enregistrés ;
* gains calculés ;
* jetons d’authentification ou de session selon la configuration.

### MongoDB

MongoDB est utilisé pour les données semi-structurées :

* historique des tirages ;
* détails des résultats ;
* données issues de l’import ou du scraping.

Cette séparation permet de profiter à la fois de la fiabilité relationnelle de PostgreSQL et de la souplesse documentaire de MongoDB.

## 1.5 Reverse proxy Apache

Apache2 est utilisé comme reverse proxy.

Il permet :

* d’exposer le frontend ;
* de rediriger les appels `/api` vers le backend Spring Boot ;
* d’activer le HTTPS avec Let’s Encrypt ;
* de centraliser la configuration réseau ;
* d’ajouter des en-têtes de sécurité ;
* de gérer les erreurs serveur.

---

# 2. Prérequis

## 2.1 Serveur recommandé

Le projet peut être déployé sur un serveur Linux.

Configuration recommandée :

```text
OS : Ubuntu Server 22.04 LTS ou supérieur
RAM : 4 Go minimum
CPU : 2 vCPU minimum
Stockage : 40 Go minimum
Accès SSH : obligatoire
Nom de domaine : recommandé
```

## 2.2 Logiciels nécessaires

Les outils suivants doivent être installés :

```bash
sudo apt update
sudo apt install git curl wget unzip apache2 certbot python3-certbot-apache -y
```

Pour Docker :

```bash
sudo apt install docker.io docker-compose-plugin -y
sudo systemctl enable docker
sudo systemctl start docker
```

Vérification :

```bash
docker --version
docker compose version
```

## 2.3 Ports utilisés

| Service             | Port interne | Utilisation        |
| ------------------- | -----------: | ------------------ |
| Frontend            |         5500 | Interface web      |
| Backend Spring Boot |         8082 | API REST           |
| PostgreSQL          |         5432 | Base relationnelle |
| MongoDB             |        27017 | Base documentaire  |
| Apache HTTP         |           80 | Redirection HTTPS  |
| Apache HTTPS        |          443 | Accès sécurisé     |

---

# 3. Déploiement Docker

## 3.1 Récupération du projet

```bash
git clone https://github.com/SDINAHET/LOTO_API_v8.git
cd LOTO_API_v8
```

## 3.2 Configuration des variables d’environnement

Créer ou modifier le fichier `.env`.

Exemple :

```env
SPRING_PROFILES_ACTIVE=prod

POSTGRES_DB=lotodb
POSTGRES_USER=loto_user
POSTGRES_PASSWORD=mot_de_passe_fort

MONGO_INITDB_ROOT_USERNAME=mongo_admin
MONGO_INITDB_ROOT_PASSWORD=mot_de_passe_fort

JWT_SECRET=changer_cette_valeur_en_production
```

Les mots de passe ne doivent pas être versionnés sur GitHub.

Il est recommandé d’utiliser un fichier `.env` local ignoré par Git.

## 3.3 Construction des conteneurs

```bash
docker compose build
```

## 3.4 Démarrage de l’application

```bash
docker compose up -d
```

## 3.5 Vérification des services

```bash
docker compose ps
```

Les services principaux doivent être à l’état `Up`.

## 3.6 Consultation des logs

Logs de tous les services :

```bash
docker compose logs -f
```

Logs du backend uniquement :

```bash
docker compose logs -f backend
```

Logs PostgreSQL :

```bash
docker compose logs -f postgres
```

Logs MongoDB :

```bash
docker compose logs -f mongodb
```

## 3.7 Arrêt de l’application

```bash
docker compose down
```

## 3.8 Redémarrage après mise à jour

```bash
git pull
docker compose build
docker compose up -d
```

---

# 4. Configuration Apache

## 4.1 Rôle d’Apache

Apache est utilisé pour rendre l’application accessible publiquement.

Il reçoit les requêtes HTTPS et les redirige vers les bons services Docker ou locaux.

Exemple :

```text
https://loto-tracker.fr/       → frontend
https://loto-tracker.fr/api/   → backend Spring Boot
```

## 4.2 Exemple de VirtualHost

Exemple simplifié de configuration Apache :

```apache
<VirtualHost *:80>
    ServerName loto-tracker.fr
    ServerAlias www.loto-tracker.fr

    Redirect permanent / https://loto-tracker.fr/
</VirtualHost>

<VirtualHost *:443>
    ServerName loto-tracker.fr
    ServerAlias www.loto-tracker.fr

    SSLEngine on

    ProxyPreserveHost On

    ProxyPass /api/ http://127.0.0.1:8082/api/
    ProxyPassReverse /api/ http://127.0.0.1:8082/api/

    ProxyPass / http://127.0.0.1:5500/
    ProxyPassReverse / http://127.0.0.1:5500/

    Header always set X-Frame-Options "SAMEORIGIN"
    Header always set X-Content-Type-Options "nosniff"
    Header always set Referrer-Policy "strict-origin-when-cross-origin"
</VirtualHost>
```

## 4.3 Activation des modules Apache

```bash
sudo a2enmod proxy
sudo a2enmod proxy_http
sudo a2enmod headers
sudo a2enmod ssl
sudo a2enmod rewrite
sudo systemctl restart apache2
```

## 4.4 Vérification de la configuration Apache

```bash
sudo apache2ctl configtest
```

Si le résultat est `Syntax OK`, Apache peut être redémarré :

```bash
sudo systemctl reload apache2
```

## 4.5 Certificat SSL Let’s Encrypt

Installation du certificat :

```bash
sudo certbot --apache -d loto-tracker.fr -d www.loto-tracker.fr
```

Test du renouvellement automatique :

```bash
sudo certbot renew --dry-run
```

---

# 5. Procédures de sauvegarde

## 5.1 Objectif des sauvegardes

Les sauvegardes permettent de restaurer le projet en cas de :

* panne serveur ;
* erreur de manipulation ;
* perte de données ;
* corruption de base ;
* mauvaise mise à jour ;
* migration vers un autre serveur.

Les éléments à sauvegarder sont :

* PostgreSQL ;
* MongoDB ;
* fichiers de configuration ;
* fichiers `.env` ;
* configuration Apache ;
* volumes Docker ;
* éventuels fichiers de logs importants.

---

## 5.2 Sauvegarde PostgreSQL

Exemple avec `pg_dump` :

```bash
pg_dump -U loto_user -d lotodb > backup_postgres_lotodb.sql
```

Si PostgreSQL est dans Docker :

```bash
docker exec -t postgres_container pg_dump -U loto_user lotodb > backup_postgres_lotodb.sql
```

Restauration :

```bash
psql -U loto_user -d lotodb < backup_postgres_lotodb.sql
```

Avec Docker :

```bash
cat backup_postgres_lotodb.sql | docker exec -i postgres_container psql -U loto_user -d lotodb
```

---

## 5.3 Sauvegarde MongoDB

Sauvegarde :

```bash
mongodump --out backup_mongodb
```

Si MongoDB est dans Docker :

```bash
docker exec mongodb_container mongodump --out /backup_mongodb
```

Restauration :

```bash
mongorestore backup_mongodb
```

Avec Docker :

```bash
docker exec mongodb_container mongorestore /backup_mongodb
```

---

## 5.4 Sauvegarde de la configuration Apache

```bash
sudo cp -r /etc/apache2/sites-available ./backup_apache_sites_available
sudo cp -r /etc/apache2/sites-enabled ./backup_apache_sites_enabled
```

Il est également conseillé de sauvegarder :

```bash
/etc/apache2/apache2.conf
/etc/apache2/ports.conf
/etc/letsencrypt/
```

---

## 5.5 Sauvegarde des fichiers sensibles

À sauvegarder hors GitHub :

```text
.env
certificats SSL
mots de passe
clés JWT
fichiers de configuration serveur
```

Ces fichiers ne doivent jamais être publiés dans un dépôt public.

---

## 5.6 Automatisation avec cron

Exemple d’exécution quotidienne à 2h du matin :

```bash
crontab -e
```

Ajouter :

```cron
0 2 * * * /home/user/scripts/backup_loto_tracker.sh
```

Le script peut contenir les sauvegardes PostgreSQL, MongoDB et Apache.

---

# 6. Opérations de maintenance courantes

## 6.1 Vérifier l’état des conteneurs

```bash
docker compose ps
```

## 6.2 Redémarrer un service

```bash
docker compose restart backend
```

Ou tous les services :

```bash
docker compose restart
```

## 6.3 Consulter les logs

```bash
docker compose logs -f backend
```

Pour limiter le nombre de lignes :

```bash
docker compose logs --tail=100 backend
```

## 6.4 Vérifier l’espace disque

```bash
df -h
```

Nettoyage Docker :

```bash
docker system prune
```

Attention : cette commande supprime les ressources Docker inutilisées.

## 6.5 Vérifier la mémoire

```bash
free -h
```

## 6.6 Vérifier les processus

```bash
top
```

Ou :

```bash
htop
```

## 6.7 Tester l’API

```bash
curl http://localhost:8082/actuator/health
```

Réponse attendue :

```json
{
  "status": "UP"
}
```

## 6.8 Tester le frontend

```bash
curl http://localhost:5500
```

## 6.9 Tester Apache

```bash
sudo systemctl status apache2
```

Redémarrage :

```bash
sudo systemctl restart apache2
```

## 6.10 Renouveler les certificats SSL

```bash
sudo certbot renew --dry-run
```

Si le test est correct, le renouvellement automatique peut être conservé.

## 6.11 Mettre à jour l’application

```bash
git pull
docker compose build
docker compose up -d
docker compose logs -f
```

## 6.12 Vérifier les bases de données

PostgreSQL :

```bash
docker exec -it postgres_container psql -U loto_user -d lotodb
```

MongoDB :

```bash
docker exec -it mongodb_container mongosh
```

---

# 7. Tests

## 7.1 Compilation du backend

```bash
mvn clean install
```

## 7.2 Tests unitaires

```bash
mvn test
```

## 7.3 Tests d’intégration

```bash
mvn verify
```

Les tests permettent de vérifier que les principales fonctionnalités du backend restent opérationnelles après une modification.

---

# 8. Sécurité

Le projet intègre plusieurs mécanismes de sécurité :

* authentification JWT ;
* hashage des mots de passe avec BCrypt ;
* rôles utilisateur et administrateur ;
* configuration CORS ;
* protection de certains endpoints ;
* HTTPS via Apache et Let’s Encrypt ;
* en-têtes de sécurité Apache ;
* séparation entre frontend, backend et bases de données ;
* variables sensibles placées dans un fichier `.env`.

Bonnes pratiques recommandées :

* ne jamais versionner les mots de passe ;
* changer les secrets JWT en production ;
* limiter les ports exposés ;
* sauvegarder régulièrement les bases ;
* surveiller les logs ;
* maintenir les dépendances à jour.

---

# 9. Limites actuelles

## 9.1 Flyway non finalisé

La mise en place de Flyway a été étudiée pour gérer les migrations de base de données PostgreSQL.

Cependant, son intégration complète n’a pas encore été finalisée dans la version actuelle du projet.

À ce stade, la structure de la base peut être créée ou maintenue via :

* les scripts SQL ;
* la configuration JPA/Hibernate ;
* les sauvegardes/restaurations PostgreSQL ;
* les commandes manuelles d’administration.

Flyway est donc identifié comme une amélioration future importante.

---

# 10. Perspectives d’évolution

## 10.1 Finalisation de Flyway

Une évolution prévue consiste à finaliser l’intégration de Flyway afin de gérer proprement les migrations SQL.

Objectifs :

* versionner les évolutions de la base PostgreSQL ;
* automatiser la création des tables ;
* sécuriser les modifications de schéma ;
* faciliter les déploiements sur un nouveau serveur ;
* améliorer la reproductibilité de l’environnement.

Exemple de structure future :

```text
src/main/resources/db/migration/
├── V1__create_users_table.sql
├── V2__create_tickets_table.sql
├── V3__create_ticket_gains_table.sql
└── V4__create_refresh_tokens_table.sql
```

## 10.2 Amélioration CI/CD

Une autre évolution possible est la mise en place d’un pipeline GitHub Actions complet.

Objectifs :

* compiler automatiquement le backend ;
* exécuter les tests unitaires ;
* exécuter les tests d’intégration ;
* construire les images Docker ;
* vérifier la compatibilité PostgreSQL et MongoDB ;
* préparer un déploiement automatisé.

## 10.3 Supervision avancée

La supervision peut être renforcée avec :

* Prometheus ;
* Grafana ;
* AlertManager ;
* Loki pour les logs ;
* Uptime Kuma pour la disponibilité.

Objectif : détecter plus rapidement les erreurs, les ralentissements ou les interruptions de service.

## 10.4 Sauvegardes automatisées

Les sauvegardes peuvent être améliorées avec :

* scripts automatisés ;
* rotation des sauvegardes ;
* stockage distant ;
* test régulier de restauration ;
* notification en cas d’échec.

## 10.5 Sécurisation renforcée

Évolutions possibles :

* durcissement Apache ;
* limitation stricte des ports ouverts ;
* fail2ban ;
* rotation des secrets ;
* audit OWASP régulier ;
* gestion plus fine des rôles administrateur.

---

# 11. Conclusion

Cette documentation d’exploitation permet de comprendre, installer, maintenir et faire évoluer la plateforme **Loto Tracker API**.

Elle décrit les éléments essentiels du projet :

* architecture générale ;
* prérequis ;
* déploiement Docker ;
* configuration Apache ;
* sauvegardes ;
* maintenance courante ;
* limites actuelles ;
* perspectives d’évolution.

Elle facilite la reprise du projet par un autre développeur ou administrateur système et contribue à rendre l’environnement plus reproductible, maintenable et professionnel.
