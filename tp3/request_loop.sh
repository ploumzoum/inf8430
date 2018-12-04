#!/bin/bash
for i in {1..30}
do
# POUR TESTER: Adresse IP flottante a recuperer dans outputs de la vue densemble du stack 
   curl -s http://132.207.12.106:8080/?nom=Yvonne && echo "request $i done" &
done

wait
