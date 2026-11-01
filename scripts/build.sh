#!/usr/bin/env bash
set -euo pipefail
cd "$(dirname "$0")/.."
if [[ -n "${JAVA_HOME:-}" ]]; then
    jdk="$JAVA_HOME"
elif [[ -x /opt/homebrew/opt/openjdk@25/bin/javac ]]; then
    jdk=/opt/homebrew/opt/openjdk@25
else
    jdk=$(/usr/libexec/java_home -v 25)
fi
if [[ "$("$jdk/bin/javac" -version 2>&1)" != javac\ 25* ]]; then
    echo 'JDK 25 is required. Set JAVA_HOME to a JDK 25 installation.' >&2
    exit 2
fi
for jar in lib/model-1.0.jar lib/logic-1.0.jar; do
    if [[ ! -f "$jar" ]]; then
        echo "Required API JAR missing: $jar" >&2
        exit 2
    fi
done
# Clean only generated module output; do not compile or package test sources.
rm -rf build/model-classes build/logic-classes
mkdir -p build/model-classes build/logic-classes
model_sources=()
while IFS= read -r -d '' file; do
    model_sources+=("$file")
done < <(find model/src -type f -name '*.java' -print0)
logic_sources=()
while IFS= read -r -d '' file; do
    logic_sources+=("$file")
done < <(find logic/src -type f -name '*.java' -print0)
"$jdk/bin/javac" --release 25 -cp lib/model-1.0.jar \
    -d build/model-classes "${model_sources[@]}"
"$jdk/bin/javac" --release 25 -cp 'lib/model-1.0.jar:lib/logic-1.0.jar:build/model-classes' \
    -d build/logic-classes "${logic_sources[@]}"
"$jdk/bin/jar" --create --file build/workshop-model-impl.jar -C build/model-classes .
"$jdk/bin/jar" --create --file build/workshop-logic-impl.jar -C build/logic-classes .
# Publish artifacts only once compilation and packaging of both modules succeeds.
mkdir -p artifacts
cp build/workshop-model-impl.jar artifacts/workshop-model-impl.jar
cp build/workshop-logic-impl.jar artifacts/workshop-logic-impl.jar
printf '%s\n' 'Built artifacts/workshop-model-impl.jar and artifacts/workshop-logic-impl.jar'
