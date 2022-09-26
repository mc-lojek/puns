package pl.edu.pg.eti.domain.model.events

import pl.edu.pg.eti.domain.model.enums.RoutingKey
import java.io.Serializable

class DrawLineEvent : RabbitEvent, Serializable {
    constructor(csv: String) {
        val arr = csv.split(",")
        this.startX = arr[1].toFloat()
        this.startY = arr[2].toFloat()
        this.endX = arr[3].toFloat()
        this.endY = arr[4].toFloat()
        this.paintColor = arr[5].toInt()
        this.paintSize = arr[6].toFloat()
    }

    constructor(
        startX: Float,
        startY: Float,
        endX: Float,
        endY: Float,
        paintColor: Int,
        paintSize: Float
    ) {
        this.startX = startX
        this.startY = startY
        this.endX = endX
        this.endY = endY
        this.paintColor = paintColor
        this.paintSize = paintSize
    }
    override val routingKey = RoutingKey.TO_CLIENT
    val startX: Float
    val startY: Float
    val endX: Float
    val endY: Float
    val paintColor: Int
    val paintSize: Float

    override fun toCSV(): String {
        val output =
            "DLE,${startX.toInt()},${startY.toInt()},${endX.toInt()},${endY.toInt()},${paintColor},${paintSize}"
        return output
    }


}

