#!/bin/bash
docker compose --profile ai up -d
# docker exec -it loto_api_v7-ollama-1 ollama pull llama3.1:8b


# root@UID7E:/mnt/d/Users/steph/Documents/6ème trimestre/LOTO_API_v7# ./start_docker_ai.sh
# WARN[0000] /mnt/d/Users/steph/Documents/6ème trimestre/LOTO_API_v7/docker-compose.yml: the attribute `version` is obsolete, it will be ignored, please remove it to avoid potential confusion
# [+] Running 7/7
#  ✔ Network loto_api_v7_default       Created                                            0.2s
#  ✔ Container loto_api_v7-ollama-1    Started                                            1.1s
#  ✔ Container loto_api_v7-mongodb-1   Started                                            1.0s
#  ✔ Container loto_api_v7-postgres-1  Healthy                                           11.5s
#  ✔ Container loto_api_v7-backend-1   Started                                           11.8s
#  ✔ Container loto_api_v7-ai-1        Started                                            1.6s
#  ✔ Container loto_api_v7-frontend-1  Started                                           12.2s
# pulling manifest
# pulling 667b0c1932bc: 100% ▕███████████████████████████████▏ 4.9 GB
# pulling 948af2743fc7: 100% ▕███████████████████████████████▏ 1.5 KB
# pulling 0ba8f0e314b4: 100% ▕███████████████████████████████▏  12 KB
# pulling 56bb8bd477a5: 100% ▕███████████████████████████████▏   96 B
# pulling 455f34728c9b: 100% ▕███████████████████████████████▏  487 B
# verifying sha256 digest
# writing manifest
# success
# root@UID7E:/mnt/d/Users/steph/Documents/6ème trimestre/LOTO_API_v7#

# root@UID7E:/mnt/d/Users/steph/Documents/6ème trimestre/LOTO_API_v7# docker compose down --remove-orphans
# WARN[0000] /mnt/d/Users/steph/Documents/6ème trimestre/LOTO_API_v7/docker-compose.yml: the attribute `version` is obsolete, it will be ignored, please remove it to avoid potential confusion
# [+] Running 5/4
#  ✔ Container loto_api_v7-frontend-1  Removed                                            0.6s
#  ✔ Container loto_api_v7-backend-1   Removed                                            0.6s
#  ✔ Container loto_api_v7-mongodb-1   Removed                                            0.8s
#  ✔ Container loto_api_v7-postgres-1  Removed                                            0.7s
#  ! Network loto_api_v7_default       Resource is still...                               0.0s
# root@UID7E:/mnt/d/Users/steph/Documents/6ème trimestre/LOTO_API_v7#
