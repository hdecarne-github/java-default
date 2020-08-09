#!/bin/bash -x

export PATH="$HOME/.jabba/bin/:$PATH"
powershell -ExecutionPolicy Bypass -Command "[Net.ServicePointManager]::SecurityProtocol = [Net.SecurityProtocolType]::Tls12; Invoke-Expression ( Invoke-WebRequest https://raw.githubusercontent.com/shyiko/jabba/master/install.ps1 -UseBasicParsing ).Content"
export JABBA_BEFORE_INSTALL="jabba ls-remote"
