package pl.edu.pg.eti.domain.model

import pl.edu.pg.eti.domain.model.enums.RoutingKey

class ChatMessageEvent : RabbitEvent {
    constructor(csv: String) {
        val arr = csv.split(",")
        this.nickname = arr[1]
        this.content = arr[2]
        this.playerId = arr[3].toLong()
    }

    constructor(
        nickname: String,
        content: String,
        playerId: Long
    ) {
        this.nickname = nickname
        this.content = content
        this.playerId = playerId
    }

    val nickname: String
    val content: String
    val playerId: Long

    override fun toCSV(): String {
        val output = "CME,${nickname},${content},${playerId}"
        return output
    }

    override val routingKey = RoutingKey.TO_CLIENT
}