#!/bin/bash

mvn clean install -f ./bom/pom.xml

mvn clean install "$@"