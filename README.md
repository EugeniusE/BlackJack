
![Zeichnung](https://github.com/EugeniusE/BlackJack/assets/118937027/7e714177-a3fe-4244-a607-18e91f327393)


Test Status
[![BuildStatus](https://github.com/EugeniusE/BlackJack/actions/workflows/scala.yml/badge.svg)](https://github.com/EugeniusE/BlackJack/actions/workflows/scala.yml)
## Coverage 

Coverage Status  [![Coverage Status](https://coveralls.io/repos/github/EugeniusE/BlackJack/badge.svg?branch=main)](https://coveralls.io/github/EugeniusE/BlackJack?branch=main)


for checking locally use ./runCoverage in root 

## sbt project compiled with Scala 3

### Installation

This is a normal sbt project. You can compile code with `sbt compile`, run it with `sbt run`, and `sbt console` will start a Scala 3 REPL.

## How to play

![Zeichnung](src/main/scala/resources/GUI.png)

### The red box is your Genereal information.

### Hit:
    you get a card when you press hit carefull!! (you can even hit on 21 and lose instantly)

### Stand

Dealer is drawing cards until the value of the hand is 17 or higher

You win if you have more Score than the Dealer 

You loose if you have less

Draw if your even 

### Betting

You can only place bets before pressing hit

you get your bet x2 when you win and lose your bet when you lose , draw you get your bet back

The reamaining cards are displayed on the left 


## Development

## Docker mit GUI
### build docker image
docker build -t blackjack:v2 .

### check and set correct display
echo $DISPLAY
export DISPLAY=:0

### run in xquartz terminal & check if prefrences -> security -> Allow connections from network clients is on
xhost +

### check ip adress of your host machine
ip=$(ifconfig en0 | grep inet | awk '$1=="inet" {print $2}')
echo $ip

### run docker container
docker run --name blackjack -e DISPLAY=$ip:0 -v /tmp/.X11-unix:/tmp/.X11-unix -it blackjack:v2 /bin/bash

### run sbt application
sbt -Djava.awt.headless=false -Dawt.useSystemAAFontSettings=lcd -Dsun.java2d.xrender=true run

### run docker again 
docker start -i blackjack

### play again
sbt run