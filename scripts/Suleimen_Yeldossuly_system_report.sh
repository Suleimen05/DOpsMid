#!/usr/bin/env bash
set -euo pipefail

STUDENT_NAME='Suleimen Yeldossuly'
PROJECT_ROOT="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"
LOG_FILE="$PROJECT_ROOT/logs/Suleimen_Yeldossuly_system_report.log"

{
  printf 'Student: %s\n' "$STUDENT_NAME"
  printf 'Date and time: %s\n' "$(date '+%Y-%m-%d %H:%M:%S %Z')"
  printf 'Username: %s\n' "$(whoami)"
  printf 'Operating system: %s\n' "$(uname -s)"
  printf 'Kernel: %s\n' "$(uname -r)"
  printf '\nCPU information:\n'
  if command -v lscpu >/dev/null 2>&1; then lscpu | sed -n '1,12p'; else uname -m; fi
  printf '\nMemory information:\n'
  free -h
  printf '\nDisk usage and available space:\n'
  df -h
} | tee "$LOG_FILE"

