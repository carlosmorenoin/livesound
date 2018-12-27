#!/usr/bin/env bash

set -e

cd `dirname $0`
r=`pwd`
echo $r

function stop_service {
    echo "Stopping Service " $1
    cd $r/$1
    if [ -z $(cat pid.file) ]
    then
        echo "PDI not found for service " $1
    else
        kill $(cat pid.file) && rm pid.file || echo "Could not kill process" $(cat pid.file)
    fi

}

stop_service livesound-gateway

stop_service livesound-venue-query

stop_service livesound-venue-command

stop_service livesound-profiles

stop_service livesound-users

stop_service livesound-authserver

stop_service livesound-eureka

stop_service livesound-config
