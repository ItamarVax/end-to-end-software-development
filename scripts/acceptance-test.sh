#!/usr/bin/env bash
set -euo pipefail
cd "$(dirname "$0")/.."
bash scripts/build.sh
if [[ -n "${JAVA_HOME:-}" ]]; then
    jdk="$JAVA_HOME"
elif [[ -x /opt/homebrew/opt/openjdk@25/bin/javac ]]; then
    jdk=/opt/homebrew/opt/openjdk@25
else
    jdk=$(/usr/libexec/java_home -v 25)
fi
"$jdk/bin/java" -cp 'lib/model-1.0.jar:lib/logic-1.0.jar:lib/workshop-tester.jar:artifacts/workshop-model-impl.jar:artifacts/workshop-logic-impl.jar' \
    patmal.workshops.main.Main
