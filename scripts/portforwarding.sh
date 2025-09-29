#!/usr/bin/env bash
set -euo pipefail

NAMESPACE="pos"

# Kill old port forwardings
echo "Killing old port-forward processes..."
if pkill -f "kubectl port-forward"; then
  echo "✅ Old port-forward processes killed."
else
  echo "ℹ️ No old port-forward processes found."
fi

# Port forward Postgres (K8s service: postgres, local: 5432 → pod: 5432)
echo "Forwarding Postgres on localhost:5432 ..."
kubectl port-forward svc/postgres -n $NAMESPACE 5432:5432 > /tmp/portfwd-postgres.log 2>&1 &

# Port forward Kafka (assuming Redpanda or Kafka service runs on 9092 inside cluster)
echo "Forwarding Kafka on localhost:19092 ..."
kubectl port-forward svc/redpanda -n $NAMESPACE 19092:9092 > /tmp/portfwd-kafka.log 2>&1 &

# Port forward Redis (K8s service: redis, local: 6379 → pod: 6379)
echo "Forwarding Redis on localhost:6379 ..."
kubectl port-forward svc/redis -n $NAMESPACE 6379:6379 > /tmp/portfwd-redis.log 2>&1 &

echo "All port forwards started."
echo "Postgres → localhost:5432"
echo "Kafka    → localhost:19092"
echo "Redis    → localhost:6379"
echo "Use CTRL+C to stop foreground, or kill background processes."