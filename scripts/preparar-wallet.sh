#!/usr/bin/env sh
set -eu

project_dir=$(CDPATH= cd -- "$(dirname -- "$0")/.." && pwd)
wallet_zip="${1:-$project_dir/../../Semana 2/Desarrollo/semana2/Wallet_ProcesoBaseDatos.zip}"
wallet_dir="$project_dir/.local/oracle-wallet"

if [ ! -f "$wallet_zip" ]; then
  printf 'No se encontro el wallet: %s\n' "$wallet_zip" >&2
  printf 'Uso: %s /ruta/Wallet_ProcesoBaseDatos.zip\n' "$0" >&2
  exit 1
fi

mkdir -p "$wallet_dir"
unzip -oq "$wallet_zip" -d "$wallet_dir"
chmod 600 "$wallet_dir"/*
printf 'Wallet preparado en %s\n' "$wallet_dir"
