#!/bin/bash
# docker compose up -d --build
# docker compose -f docker-compose.test.yml up --build
# docker compose -f docker-compose.test.yml up \
#   --build \
#   --exit-code-from tests \
#   --no-attach mongo_test


# docker compose -f docker-compose.test.yml up \
#   --build \
#   --exit-code-from tests

docker compose -f docker-compose.test.yml up \
  --build \
  --exit-code-from tests \
  --no-attach mongo_test \
  --remove-orphans
