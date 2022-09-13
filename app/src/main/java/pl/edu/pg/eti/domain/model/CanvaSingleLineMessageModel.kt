package pl.edu.pg.eti.domain.model

import android.graphics.Color
import android.graphics.Paint
import java.io.Serializable

data class CanvaSingleLineMessageModel(
    val startX: Float,
    val startY: Float,
    val endX: Float,
    val endY: Float,
    val paintColor: Int,
    val paintSize: Float
): Serializable {
    fun toCSV():String{
        val output = "draw,${startX.toInt()},${startY.toInt()},${endX.toInt()},${endY.toInt()},${paintColor},${paintSize}"
        return output
    }

}