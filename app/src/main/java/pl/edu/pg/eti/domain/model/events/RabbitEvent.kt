package pl.edu.pg.eti.domain.model.events

import pl.edu.pg.eti.domain.model.enums.RoutingKey

interface RabbitEvent {
    fun toCSV(): String
    val routingKey: RoutingKey
}

