package pl.edu.pg.eti.domain.model.events

import pl.edu.pg.eti.domain.model.enums.RoutingKey

class PlayerReadyEvent : RabbitEvent {
    constructor(csv: String) {
        val arr = csv.split(",")
        this.playerId = arr[1].toLong()
    }

    constructor(playerId: Long) {
        this.playerId = playerId
    }

    val playerId: Long

    override fun toCSV(): String {
        val output = "PRE,${playerId}"
        return output
    }

    override val routingKey: RoutingKey = RoutingKey.TO_SERVER
}