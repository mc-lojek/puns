package pl.edu.pg.eti.domain.model.events

import pl.edu.pg.eti.domain.model.enums.RoutingKey

class BlockInteractionEvent : RabbitEvent {
    constructor(csv: String) {
        val arr = csv.split(",")
        this.keyword = arr[1]
    }

    constructor(keyword: String, notCsv: Boolean) {
        this.keyword = keyword
    }

    val keyword: String

    override fun toCSV(): String {
        return "BIE,$keyword"
    }

    override val routingKey: RoutingKey = RoutingKey.TO_CLIENT
}