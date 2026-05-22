-- =====================================
-- Création utilisateur de test
-- =====================================

DO
$$
BEGIN
   IF NOT EXISTS (
      SELECT FROM pg_roles WHERE rolname = 'postgrestest'
   ) THEN
      CREATE USER postgrestest WITH PASSWORD 'postgrestest';
   END IF;
END
$$;

-- =====================================
-- Création base de données de test
-- =====================================

DO
$$
BEGIN
   IF NOT EXISTS (
      SELECT FROM pg_database WHERE datname = 'lotodb_test'
   ) THEN
      CREATE DATABASE lotodb_test OWNER postgrestest;
   END IF;
END
$$;

-- =====================================
-- Permissions
-- =====================================

GRANT ALL PRIVILEGES ON DATABASE lotodb_test TO postgrestest;
