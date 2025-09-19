#!/usr/bin/env bash
set -euo pipefail
NS=${NS:-pos}
helm upgrade --install infra deploy/helm/infra -n "$NS" --create-namespace
