#!/bin/bash
docker compose up -d --build
# docker compose -f docker-compose.test.yml up --build
# docker compose -f docker-compose.test.yml up \
#   --build \
#   --exit-code-from tests \
#   --no-attach mongo_test


# docker compose -f docker-compose.test.yml up \
#   --build \
#   --exit-code-from tests

# docker compose -f docker-compose.test.yml up \
#   --build \
#   --exit-code-from tests \
#   --no-attach mongo_test \
#   --remove-orphans



# root@UID7E:/mnt/d/Users/steph/Documents/6ème trimestre/LOTO_API_v7# ./start_docker.sh
# WARN[0000] /mnt/d/Users/steph/Documents/6ème trimestre/LOTO_API_v7/docker-compose.yml: the attribute `version` is obsolete, it will be ignored, please remove it to avoid potential confusion
# [+] Building 7.7s (29/29) FINISHED                                            docker:default
#  => [backend internal] load build definition from Dockerfile.backend                    0.2s
#  => => transferring dockerfile: 1.23kB                                                  0.2s
#  => [backend internal] load metadata for docker.io/library/eclipse-temurin:21-jre-jamm  0.0s
#  => [backend internal] load metadata for docker.io/library/maven:3.9.9-eclipse-temurin  0.0s
#  => [backend internal] load .dockerignore                                               0.2s
#  => => transferring context: 2B                                                         0.1s
#  => [backend build 1/9] FROM docker.io/library/maven:3.9.9-eclipse-temurin-21           0.0s
#  => [backend internal] load build context                                               3.0s
#  => => transferring context: 15.21MB                                                    2.9s
#  => [backend stage-1 1/3] FROM docker.io/library/eclipse-temurin:21-jre-jammy           0.0s
#  => CACHED [backend stage-1 2/3] WORKDIR /app                                           0.0s
#  => CACHED [backend build 2/9] WORKDIR /build                                           0.0s
#  => CACHED [backend build 3/9] COPY pom.xml .                                           0.0s
#  => CACHED [backend build 4/9] COPY .mvn .mvn                                           0.0s
#  => CACHED [backend build 5/9] COPY mvnw .                                              0.0s
#  => CACHED [backend build 6/9] RUN chmod +x mvnw                                        0.0s
#  => CACHED [backend build 7/9] RUN ./mvnw -B -DskipTests dependency:go-offline          0.0s
#  => CACHED [backend build 8/9] COPY src src                                             0.0s
#  => CACHED [backend build 9/9] RUN ./mvnw -B -DskipTests clean package                  0.0s
#  => CACHED [backend stage-1 3/3] COPY --from=build /build/target/*.jar app.jar          0.0s
#  => [backend] exporting to image                                                        0.0s
#  => => exporting layers                                                                 0.0s
#  => => writing image sha256:12c65833e33b6286f2bafd4765bfd7827192bd6418a640bb54a69350cb  0.0s
#  => => naming to docker.io/library/loto_api_v7-backend                                  0.0s
#  => [backend] resolving provenance for metadata file                                    0.0s
#  => [frontend internal] load build definition from Dockerfile.frontend                  0.2s
#  => => transferring dockerfile: 411B                                                    0.1s
#  => [frontend internal] load metadata for docker.io/library/node:18-alpine              1.5s
#  => [frontend internal] load .dockerignore                                              0.2s
#  => => transferring context: 2B                                                         0.1s
#  => [frontend 1/4] FROM docker.io/library/node:18-alpine@sha256:8d6421d663b4c28fd3ebc4  0.0s
#  => [frontend internal] load build context                                              1.6s
#  => => transferring context: 11.22kB                                                    1.6s
#  => CACHED [frontend 2/4] RUN npm install -g http-server                                0.0s
#  => CACHED [frontend 3/4] WORKDIR /app                                                  0.0s
#  => CACHED [frontend 4/4] COPY src/main/resources/static /app                           0.0s
#  => [frontend] exporting to image                                                       0.0s
#  => => exporting layers                                                                 0.0s
#  => => writing image sha256:9e7c5d7f30323bdf9ee8992b5e2a1ecdcf54b3bbfc4183a0f576c459a4  0.0s
#  => => naming to docker.io/library/loto_api_v7-frontend                                 0.0s
#  => [frontend] resolving provenance for metadata file                                   0.0s
# [+] Running 4/4
#  ✔ Container loto_api_v7-postgres-1  Healthy                                           11.6s
#  ✔ Container loto_api_v7-mongodb-1   Started                                            1.1s
#  ✔ Container loto_api_v7-backend-1   Started                                           12.0s
#  ✔ Container loto_api_v7-frontend-1  Started                                           12.4s
# root@UID7E:/mnt/d/Users/steph/Documents/6ème trimestre/LOTO_API_v7#

# root@UID7E:/mnt/d/Users/steph/Documents/6ème trimestre/LOTO_API_v7# ./stop_docker.sh
# WARN[0000] /mnt/d/Users/steph/Documents/6ème trimestre/LOTO_API_v7/docker-compose.yml: the attribute `version` is obsolete, it will be ignored, please remove it to avoid potential confusion
# [+] Running 5/4
#  ✔ Container loto_api_v7-frontend-1  Removed                                            0.6s
#  ✔ Container loto_api_v7-backend-1   Removed                                            0.8s
#  ✔ Container loto_api_v7-mongodb-1   Removed                                            1.1s
#  ✔ Container loto_api_v7-postgres-1  Removed                                            1.2s
#  ! Network loto_api_v7_default       Resource is still...                               0.0s
# root@UID7E:/mnt/d/Users/steph/Documents/6ème trimestre/LOTO_API_v7#

# root@UID7E:/mnt/d/Users/steph/Documents/6ème trimestre/LOTO_API_v7# docker stop $(docker ps -aq)
# 516eb67855b8
# 670f7dea842d
# 57a6df8c165a
# 0a43a0f4ae58
# f7afc6337aef
# 39294b09f914
# 2cdf9fb146db
# 21e28f4f3931
# 4d86ef45440d
# 61971b46d947
# 87b4714bc395
# b29849eccd60
# 2861bf795bae
# 560b2f8b3a1d
# 362b4e095dfc
# root@UID7E:/mnt/d/Users/steph/Documents/6ème trimestre/LOTO_API_v7# docker compose down
# WARN[0000] /mnt/d/Users/steph/Documents/6ème trimestre/LOTO_API_v7/docker-compose.yml: the attribute `version` is obsolete, it will be ignored, please remove it to avoid potential confusion
# [+] Running 1/1
#  ✔ Network loto_api_v7_default  Removed                                                 0.7s
# root@UID7E:/mnt/d/Users/steph/Documents/6ème trimestre/LOTO_API_v7#
