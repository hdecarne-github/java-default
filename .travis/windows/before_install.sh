#!/bin/bash -x

export PATH="$HOME/.jabba/bin/:$PATH"
powershell -Command "[Net.ServicePointManager]::SecurityProtocol = [Net.SecurityProtocolType]::Tls12; Invoke-Expression ( Invoke-WebRequest https://raw.githubusercontent.com/shyiko/jabba/master/install.ps1 -UseBasicParsing ).Content"
