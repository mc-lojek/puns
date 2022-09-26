package pl.edu.pg.eti.domain.model.events

import pl.edu.pg.eti.domain.model.enums.RoutingKey

class PlayerHitEvent : RabbitEvent {
    constructor(csv: String) {
        val arr = csv.split(",")
        this.nickname = arr[1]
    }

    constructor(nickname: String, notCsv: Boolean) {
        this.nickname = nickname
    }

    val nickname: String

    override fun toCSV(): String {
        val output = "PHE,${nickname}"
        return output
    }

    override val routingKey: RoutingKey = RoutingKey.TO_SERVER
}