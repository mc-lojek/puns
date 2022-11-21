package pl.edu.pg.eti.domain.model.events

import pl.edu.pg.eti.domain.model.enums.RoutingKey

class HostStartEvent : RabbitEvent {
    constructor(csv: String) {
        val arr = csv.split(",")
        this.hostId = arr[1].toLong()
    }

    constructor(reason: Long) {
        this.hostId = reason
    }

    val hostId: Long

    override fun toCSV() = "HSE,$hostId"

    override val routingKey = RoutingKey.TO_ALL
}