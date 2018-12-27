#!/usr/bin/env bash

set -e

cd `dirname $0`
r=`pwd`
echo $r

function start_after_service {
    echo "Starting Service " . $1
    cd $r/$1
    bash $r/dockerutil/wait-for.sh localhost:$2 -t 120 -- mvn -q clean spring-boot:run & echo $! > ./pid.file &
}

# Config
cd $r/livesound-config
echo "Starting Config Service..."
mvn -q clean spring-boot:run & echo $! > ./pid.file &

# Eureka
#echo "Starting Eureka..."
start_after_service livesound-eureka 8888

# Auth Service
start_after_service livesound-authserver 1111

## Users Service
start_after_service livesound-users 1111

## Profile Service
start_after_service livesound-profiles 1111

## Venues Service
start_after_service livesound-venue-query 1111
start_after_service livesound-venue-command 1111

## Gateway Service
start_after_service livesound-gateway 1111


