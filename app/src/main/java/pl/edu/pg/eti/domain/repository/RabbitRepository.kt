package pl.edu.pg.eti.domain.repository

import android.graphics.Paint
import com.rabbitmq.client.Channel
import com.rabbitmq.client.Connection
import com.rabbitmq.client.ConnectionFactory
import pl.edu.pg.eti.domain.manager.SessionManager
import pl.edu.pg.eti.domain.model.CanvaSingleLineMessageModel
import pl.edu.pg.eti.domain.model.MessageModel

interface RabbitRepository {
    fun sendDrawLine(
        canvaSingleLine: CanvaSingleLineMessageModel,
        sessionManager: SessionManager,
        exchangeName: String
    )

    fun sendGuess(
        sessionManager: SessionManager,
        messageModel: MessageModel,
        exchangeName: String
    )
}