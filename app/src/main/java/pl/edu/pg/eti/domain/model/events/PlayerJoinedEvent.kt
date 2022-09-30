package pl.edu.pg.eti.domain.model.events

import pl.edu.pg.eti.domain.model.enums.RoutingKey

class PlayerJoinedEvent: RabbitEvent {
    constructor(csv: String) {
        val arr = csv.split(",")
        this.id = arr[1].toLong()
        this.nickname = arr[2]
    }

    constructor(
        id: Long,
        nick: String
    ) {
        this.id = id
        this.nickname = nick
    }

    val id: Long
    val nickname: String

    override fun toCSV() = "PJE,${id},${nickname}"
    override val routingKey: RoutingKey = RoutingKey.TO_ALL
}