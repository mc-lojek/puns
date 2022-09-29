package pl.edu.pg.eti.domain.model.events

import pl.edu.pg.eti.domain.model.enums.RoutingKey

class ScoreboardEvent : RabbitEvent {
    constructor(csv: String) {
        scoreboard = arrayListOf()
        val arr = csv.split(",")
        for (i in 1 until arr.size step 2) {
            this.scoreboard.add(ScoreboardRow(arr[i], arr[i + 1].toInt()))
        }
    }

    constructor(array: List<ScoreboardRow>) {
        this.scoreboard = array
    }

    val scoreboard: List<ScoreboardRow>

    override fun toCSV(): String {
        return "SBE," + scoreboard.joinToString (",")
    }

    override val routingKey: RoutingKey = RoutingKey.TO_CLIENT
}