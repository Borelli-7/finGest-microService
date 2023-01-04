#!/bin/sh

curl localhost:8080/actuator/refresh -d {} -H "Content-Type: application/json"