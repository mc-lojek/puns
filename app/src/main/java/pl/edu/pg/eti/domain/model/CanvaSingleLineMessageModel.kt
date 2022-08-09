package pl.edu.pg.eti.domain.model

import android.graphics.Paint
import java.io.Serializable

data class CanvaSingleLineMessageModel(
    val startX: Float,
    val startY: Float,
    val endX: Float,
    val endY: Float,
    val paint: Paint
): Serializable {
    override fun toString(): String {
        return "CanvaSingleLineMessageModel(startX=$startX, startY=$startY, endX=$endX, endY=$endY, paint=$paint)"
    }

    fun toCSV():String{
        val output = "${startX.toInt()},${startY.toInt()},${endX.toInt()},${endY.toInt()}"//,${paint.color},${paint.strokeWidth}"
        return output
    }

}