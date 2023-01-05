#!/bin/sh

curl localhost:8081/actuator/refresh -d {} -H "Content-Type: application/json"