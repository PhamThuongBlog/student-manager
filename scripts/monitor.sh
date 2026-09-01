#!/bin/bash
while true; do
  STATUS=$(curl -s http://localhost:8082/api/students/health | python3 -c "import sys,json; print(json.load(sys.stdin)['status'])" 2>/dev/null || echo "DOWN")
  echo "[$(date '+%H:%M:%S')] Student Manager: $STATUS"
  sleep 5
done