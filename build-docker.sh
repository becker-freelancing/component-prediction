#!/bin/bash

./build.sh "-Pdocker-build $@"

docker image prune -f
