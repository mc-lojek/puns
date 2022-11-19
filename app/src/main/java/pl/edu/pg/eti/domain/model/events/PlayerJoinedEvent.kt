package pl.edu.pg.eti.domain.model.events

import pl.edu.pg.eti.domain.model.enums.RoutingKey

class PlayerJoinedEvent: RabbitEvent {
    constructor(csv: String) {
        val arr = csv.split(",")
        this.id = arr[1].toLong()
        this.nickname = arr[2]
        this.playersCount = arr[3].toInt()
    }

    constructor(
        id: Long,
        nick: String,
        playersCount: Int
    ) {
        this.id = id
        this.nickname = nick
        this.playersCount = playersCount
    }

    val id: Long
    val nickname: String
    val playersCount: Int

    override fun toCSV() = "PJE,${id},${nickname},${playersCount}"
    override val routingKey: RoutingKey = RoutingKey.TO_ALL
}