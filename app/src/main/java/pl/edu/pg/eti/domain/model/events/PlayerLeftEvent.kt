package pl.edu.pg.eti.domain.model.events

import pl.edu.pg.eti.domain.model.enums.RoutingKey

class PlayerLeftEvent: RabbitEvent {
    constructor(csv: String) {
        val arr = csv.split(",")
        this.id = arr[1].toLong()
        this.nickname = arr[2]
        this.playersCount = arr[3].toInt()
        this.hostId = arr[4].toLong()
    }

    constructor(
        id: Long,
        nick: String,
        playersCount: Int,
        hostId:Long
    ) {
        this.id = id
        this.nickname = nick
        this.playersCount = playersCount
        this.hostId=hostId
    }

    val id: Long
    val nickname: String
    val playersCount: Int
    val hostId: Long

    override fun toCSV() = "PLE,${id},${nickname},${playersCount},${hostId}"
    override val routingKey: RoutingKey = RoutingKey.TO_ALL
}