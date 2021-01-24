#!/bin/bash

cd "$(dirname "$(readlink -f "$0")")"
source ./common.sh

mvnCopyDependencies
