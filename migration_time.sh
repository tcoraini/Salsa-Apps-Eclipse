#!/bin/bash

uan=uan://localhost/actor
ual=rmsp://localhost/actor
if [ $# -gt 1 ]; then
    uan="uan://$2/actor"
    if [ $# -gt 2 ]; then
	ual="rmsp://$3/actor"
    else
	ual="rmsp://$2/actor"
    fi
fi

java -cp salsa1.1.5.jar:bin apps.MigrationTime $1 $uan $ual