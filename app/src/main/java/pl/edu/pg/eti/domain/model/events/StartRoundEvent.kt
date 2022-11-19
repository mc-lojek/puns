package pl.edu.pg.eti.domain.model.events

import pl.edu.pg.eti.domain.model.enums.RoutingKey

class StartRoundEvent : RabbitEvent {
    constructor(csv: String) {
        val arr = csv.split(",")
        this.drawingId = arr[1].toLong()
        this.drawingNick = arr[2]
        this.keyword = arr[3]
        this.roundsLeft = arr[4].toInt()
        this.roundsPassed = arr[5].toInt()
    }

    constructor(
        drawingId: Long,
        drawingNick: String,
        keyword: String,
        roundsLeft: Int,
        roundsPassed: Int
    ) {
        this.drawingId = drawingId
        this.drawingNick = drawingNick
        this.keyword = keyword
        this.roundsLeft = roundsLeft
        this.roundsPassed = roundsPassed
    }

    val drawingId: Long
    val drawingNick: String
    val keyword: String
    val roundsLeft: Int
    val roundsPassed: Int

    override fun toCSV(): String {
        val output = "SRE,${drawingId},${drawingNick},${keyword},${roundsLeft},${roundsPassed}"
        return output
    }

    override val routingKey: RoutingKey = RoutingKey.TO_CLIENT
}