#!/bin/bash -x

curl -sL https://github.com/shyiko/jabba/raw/master/install.sh | bash -s -- --skip-rc && . ~/.jabba/jabba.sh
jabba install adopt@1.11.0-6
./gradlew -v
