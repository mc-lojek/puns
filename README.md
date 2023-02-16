
# Puns

Native Android app as an online multiplayer turn game which main goal is to draw the appropriate picture and guess others pictures. 

## Insights

Mobile app comunicates with dedicated [server application](https://github.com/mc-lojek/puns-server) through HTTP protocol and also AMQP with a RabbitMQ instance as a message broker. 

Users are able to register and login to their accounts and also playing in guest mode. There are two variants of the game: 

 1. Fast game - play with random players
 2. Private game - play with friends by sharing a secret code

Fast game starts automatically when the lobby is full and in case of private game the host of the lobby should start it manually when all players are ready. Private game variant also allows to set properties like rounds count, and turn duration.

On each turn one player is the drawer. His responsibility is to draw the given keyword. It is possible to change the brush color, size and clear the whole canvas. The rest of the players are guessing what the picture shows. They can type their guesses on the chat. The turn ends if the countdown timer will run out or every player will guess the keyword. After each turn the scoreboard is shown. The app is able to detect whether the user is AFK and kick them from the game if necessary. 

## Contributors
* [Patryk Marciniak](https://github.com/Ciniaq)
* [Radosław Baziak](https://github.com/Creero)
* [Maciej Łojek](https://github.com/mc-lojek)

## Stack

* Kotlin
* Android framework
* MVVM pattern
* Jetpack
* Coroutines
* Room
* Retrofit
* Dagger Hilt
