#!/usr/bin/env bash
set -euo pipefail
MVN=${MVN:-mvn}
$MVN -q -DskipTests clean package
