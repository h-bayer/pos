#!/usr/bin/env bash
set -euo pipefail
NS=${NS:-ingress-nginx}
helm repo add ingress-nginx https://kubernetes.github.io/ingress-nginx >/dev/null 2>&1 || true
helm repo update >/dev/null 2>&1 || true
helm upgrade --install ingress-nginx ingress-nginx/ingress-nginx \
  -n "$NS" --create-namespace \
  --set controller.publishService.enabled=true \
  --set controller.service.type=LoadBalancer
