package pl.edu.pg.eti.domain.model

import pl.edu.pg.eti.domain.model.enums.RoutingKey


class PlayerGuessEvent : RabbitEvent {
    constructor(csv: String) {
        val arr = csv.split(",")
        this.nickname = arr[1]
        this.content = arr[2]
    }

    constructor(
        nickname: String,
        content: String
    ) {
        this.nickname=nickname
        this.content=content
    }


    override val routingKey = RoutingKey.TO_SERVER
    val nickname: String
    val content: String


    override fun toCSV(): String {
        return "PGE,${nickname},${content}"
    }
}