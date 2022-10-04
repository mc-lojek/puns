package pl.edu.pg.eti.domain.model.events

import pl.edu.pg.eti.domain.model.enums.RoutingKey

class StartGameEvent : RabbitEvent {
    constructor(csv: String) {
        val arr = csv.split(",")
        this.playersCount = arr[1].toInt()
    }

    constructor(
        playersCount: Int,
    ) {
        this.playersCount = playersCount
    }

    val playersCount: Int

    override fun toCSV() = "SGE,${playersCount}"

    override val routingKey: RoutingKey = RoutingKey.TO_CLIENT
}