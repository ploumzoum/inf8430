#!/bin/bash
for i in {1..30}
do
   curl -s http://132.207.12.225:8080/?nom=Yvon && echo "request $i done" &
done

wait
