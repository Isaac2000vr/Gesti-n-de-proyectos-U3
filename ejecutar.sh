#!/usr/bin/env bash
# Compila y ejecuta el Sistema de gestión de proyectos (Caso 26).
set -euo pipefail

cd "$(dirname "$0")"

if ! command -v javac >/dev/null 2>&1; then
    echo "Error: no se encontró 'javac'. Instale un JDK 17 o superior." >&2
    exit 1
fi

rm -rf out
mkdir -p out

echo "Compilando..."
javac -encoding UTF-8 -Xlint:all -d out $(find src -name '*.java')

echo "Ejecutando..."
java -cp out com.unicartagena.gestionproyectos.Main
