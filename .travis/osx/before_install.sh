#!/bin/bash -x

curl -sL https://github.com/shyiko/jabba/raw/master/install.sh | bash
export JABBA_BEFORE_INSTALL=".. ~/.jabba/jabba.sh; jabba ls-remote"
