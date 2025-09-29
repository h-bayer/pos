#!/usr/bin/env bash
set -euo pipefail
#./scripts/helm-install-ingress.sh
./scripts/helm-install-infra.sh
./scripts/helm-deps.sh
./scripts/helm-install-apps.sh
#./scripts/helm-install-gateway.sh
