# POS (Maven multi-module, Spring Boot 3.5.5, Java 24)

Multi-module services with Postgres/JPA, Redis, Kafka demos, Docker & Helm.
Each service exposes:
- `GET /api/hello`
- DB: `POST /api/db/greet?message=Hi`, `GET /api/db/all`
- Redis: `POST /api/redis/set?key=k&value=v`, `GET /api/redis/get?key=k`
- Kafka: `POST /api/kafka/publish?value=foo`, `GET /api/kafka/last`

## Local development (Docker Compose)
```bash
docker compose up -d             # start Postgres, Redis, Redpanda (persistent volumes)
./scripts/build-all.sh           # build all jars
cd edge-pos-api && mvn spring-boot:run
# http://localhost:8080/api/hello
```
## Portforwarding 
```bash
kubectl -n pos port-forward svc/postgres 5432:5432 & #portforward as background job in bash. 
# Only as long as shell is open. first port is localhost:5432, second in pod 5432

jobs #shows the running jobs in shell

kill %ID #kills the job with the id ID  
```
for Redis
```bash
kubectl -n pos port-forward svc/redis 6379:6379 &
```
for Kafka
```bash
kubectl -n pos port-forward svc/redpanda 19092:9092 &
```
## deploy all of the serices
```bash
helm upgrade --install pos deploy/helm/pos -n pos -f deploy/helm/pos/values.yaml
```

## Build single service and deploy using helm
```bash
# 1) go to the module folder
cd edge-pos-api

# 2) set the new version just for this module
mvn -q versions:set -DnewVersion=0.0.2-SNAPSHOT -DgenerateBackupPoms=false
mvn -q versions:commit

# 3) verify
mvn -q -DforceStdout help:evaluate -Dexpression=project.version
# -> 0.0.2-SNAPSHOT

# from the project root
# build only this module
mvn -q -DskipTests -pl edge-pos-api -am package

APP_VER=0.0.3
docker build -f docker/Dockerfile.jvm \
  --build-arg MODULE=edge-pos-api \
  -t edge-pos-api:${APP_VER} .

helm upgrade --install pos deploy/helm/pos -n pos \
  -f deploy/helm/pos/values-dockerdesktop.yaml \
  --set edge-pos-api.image.repository="edge-pos-api" \
  --set edge-pos-api.image.tag="${APP_VER}" \
  --set edge-pos-api.image.pullPolicy="Never"
  
kubectl -n pos rollout status deploy/pos-edge-pos-api

curl http://pos.localtest.me/edge/api/hello
```
list all Helm installs
```bash
helm list -A
# Columns:
# NAME | NAMESPACE | REVISION | UPDATED | STATUS | CHART (name-version) | APP VERSION

## Build container images
```bash
./scripts/build-all.sh
./scripts/docker-build.sh        # creates <service>:0.0.1 locally
```

list deployed workloads and what is running
```bash
kubectl get deploy,sts,ds -A \
  -o custom-columns=KIND:.kind,NS:.metadata.namespace,NAME:.metadata.name,IMAGES:.spec.template.spec.containers[*].image

```

## Kubernetes on Docker Desktop (Helm)
```bash
# one-shot installer: ingress + infra + apps + gateway
./scripts/helm-install-all.sh

# or step-by-step
./scripts/helm-install-ingress.sh      # nginx ingress controller
./scripts/helm-install-infra.sh        # Postgres/Redis/Redpanda (PVCs)
./scripts/helm-deps.sh
./scripts/helm-install-apps.sh         # app Deployments/Services
./scripts/helm-install-gateway.sh      # path-based Ingress
```

### Access
- Host: http://pos.localtest.me
- Examples:
  - http://pos.localtest.me/edge/api/hello
  - http://pos.localtest.me/inventory/api/hello

## Persistence & credentials
- Docker volumes: `pgdata`, `redisdata`, `redpandadata`.
- Helm infra PVCs: Postgres (1Gi), Redis (1Gi), Redpanda (5Gi).
- Postgres creds: `admin` / `admin` (dev only).
- Each service has a unique DB (e.g., `pos_edge_pos_api`, `pos_pricing_service`, ...).

## Using a remote registry (optional)
```bash
REGISTRY=registry.example.com/pharmacy VERSION=0.0.1 ./scripts/docker-build.sh
REGISTRY=registry.example.com/pharmacy VERSION=0.0.1 ./scripts/docker-push.sh
helm upgrade --install pos deploy/helm/pos -n pos -f deploy/helm/pos/values.yaml   --set global.image.registry=registry.example.com/pharmacy
```

# Kafka
```bash

#create a test event and send it to Kafka
 printf 'test-key\ttest-value\n' | kcat -b localhost:19092 -t productEvents -K $'\t' -P

#check is the event is send to kafka and processed
kcat -b localhost:19092 -t productEvents -C -o beginning -e

#redpanda commands
rpk topic list
rpk topic delete productEvents
rpk topic create productEvents -p 6 -r 1

#for kafka running in pod (we are using redpanda so far)
kubectl exec -it <kafka-pod-name> -n <namespace> -- bash
cd /opt/kafka/bin
./kafka-topics.sh --bootstrap-server localhost:9092 --list


# Letzte N Nachrichten holen
rpk topic consume productEvents --brokers localhost:19092 --num 10

# Live mitlesen (folgt neuen Nachrichten)
rpk topic consume productEvents --brokers localhost:19092 -f

# Welche Gruppen gibt’s?
rpk group list --brokers localhost:19092

# Deine Gruppe ansehen (z. B. inventory-cg)
rpk group describe inventory-cg --brokers localhost:19092

# Offsets für productEvents an den Anfang setzen
rpk group seek inventory-cg --to-start -t productEvents --brokers localhost:19092

# erase all data but keep topic
rpk topic alter-config productEvents --set retention.ms=0 --brokers localhost:19092
sleep 5   # allow background deletion
rpk topic alter-config productEvents --set retention.ms=604800000 --brokers localhost:19092


```
