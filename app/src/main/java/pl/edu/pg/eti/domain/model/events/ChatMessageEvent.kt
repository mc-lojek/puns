package pl.edu.pg.eti.domain.model.events

import pl.edu.pg.eti.domain.model.enums.RoutingKey

class ChatMessageEvent : RabbitEvent {
    constructor(csv: String) {
        val arr = csv.split(",")
        this.nickname = arr[1]
        this.content = arr[2]
        this.level = arr[3].toLong()
    }

    constructor(
        nickname: String,
        content: String,
        level: Long
    ) {
        this.nickname = nickname
        this.content = content
        this.level = level
    }

    val nickname: String
    val content: String
    val level: Long

    override fun toCSV(): String {
        val output = "CME,${nickname},${content},${level}"
        return output
    }

    override val routingKey = RoutingKey.TO_CLIENT
}