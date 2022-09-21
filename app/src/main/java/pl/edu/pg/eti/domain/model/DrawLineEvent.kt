package pl.edu.pg.eti.domain.model

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import java.io.Serializable

class DrawLineEvent : RabbitEvent, Serializable {
    constructor(csv: String) {
        this.startX = 1F
        this.startY = 1F
        this.endX = 1F
        this.endY = 1F
        this.paintColor = 1
        this.paintSize = 1F
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
    override val routingKey = "X.C"//todo wrzucić do enuma
    val startX: Float
    val startY: Float
    val endX: Float
    val endY: Float
    val paintColor: Int
    val paintSize: Float

    override fun toCSV(): String {
        val output =
            "DRW,${startX.toInt()},${startY.toInt()},${endX.toInt()},${endY.toInt()},${paintColor},${paintSize}"
        return output
    }


}

