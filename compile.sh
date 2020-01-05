#!/usr/bin/env bash
mvn clean package
sudo cp target/bingo-1.0.jar /srv/papermc/plugins/
papermc command reload confirm