package pl.edu.pg.eti.domain.model

import pl.edu.pg.eti.domain.model.enums.RoutingKey

class FinalScoreboardEvent  : RabbitEvent {
    constructor(csv: String) {
        scoreboard = arrayListOf()
        val arr = csv.split(",")
        for (i in 1 until arr.size step 2) {
            this.scoreboard.add(ScoreboardRow(arr[i], arr[i + 1].toInt()))
        }
    }

    constructor(array: ArrayList<ScoreboardRow>) {
        this.scoreboard = array
    }

    val scoreboard: MutableList<ScoreboardRow>

    override fun toCSV(): String {
        var output = "FSE"
        scoreboard.forEach {
            output += ",${it.nickname},${it.score}"
        }
        return output
    }

    override val routingKey: RoutingKey = RoutingKey.TO_CLIENT
}