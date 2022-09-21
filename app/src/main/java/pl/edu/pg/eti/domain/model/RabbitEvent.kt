package pl.edu.pg.eti.domain.model

interface RabbitEvent {
    fun toCSV(): String
    val routingKey: String
}

