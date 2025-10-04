#!/bin/bash

ENV_VARS=()
for arg in "$@"; do
  ENV_VARS+=("JAVA_TOOL_OPTIONS_${arg}=-agentlib:jdwp=transport=dt_socket,server=y,suspend=y,address=*:5005")
done

echo "Set ENV Vars: ${ENV_VARS[@]}"

env "${ENV_VARS[@]}" docker-compose --env-file .env.dev up --build