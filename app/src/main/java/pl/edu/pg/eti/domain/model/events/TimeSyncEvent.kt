package pl.edu.pg.eti.domain.model.events

import pl.edu.pg.eti.domain.model.enums.RoutingKey

class TimeSyncEvent : RabbitEvent {
    constructor(csv: String) {
        val arr = csv.split(",")
        this.timeLeft = arr[1].toLong()
    }

    constructor(timeLeft:Long) {
        this.timeLeft = timeLeft
    }

    val timeLeft: Long

    override fun toCSV(): String {
        val output = "TSE,${timeLeft}"
        return output
    }

    override val routingKey: RoutingKey = RoutingKey.TO_CLIENT
}