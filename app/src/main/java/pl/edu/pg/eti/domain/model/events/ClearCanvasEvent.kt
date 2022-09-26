package pl.edu.pg.eti.domain.model.events

import pl.edu.pg.eti.domain.model.enums.RoutingKey

class ClearCanvasEvent : RabbitEvent{
    override fun toCSV(): String {
        return "CCE"
    }

    override val routingKey: RoutingKey = RoutingKey.TO_CLIENT
}