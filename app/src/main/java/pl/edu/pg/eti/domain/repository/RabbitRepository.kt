package pl.edu.pg.eti.domain.repository

import android.graphics.Paint
import com.rabbitmq.client.Channel
import com.rabbitmq.client.Connection
import com.rabbitmq.client.ConnectionFactory
import pl.edu.pg.eti.data.util.RabbitConnectionParameters
import pl.edu.pg.eti.domain.model.CanvaSingleLineMessageModel

interface RabbitRepository {
    //fun createServerConnection():RabbitConnectionParameters

    fun sendDrawLine(
        canvaSingleLine: CanvaSingleLineMessageModel,
        //rabbitConnectionParameters: RabbitConnectionParameters
        channel: Channel,
        queueName: String
    )
}