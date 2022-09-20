package pl.edu.pg.eti.data.repository

import android.graphics.Paint
import android.util.Log
import com.rabbitmq.client.Channel
import com.rabbitmq.client.Connection
import com.rabbitmq.client.ConnectionFactory
import okio.ByteString.Companion.toByteString
import pl.edu.pg.eti.domain.manager.SessionManager
import pl.edu.pg.eti.domain.model.CanvaSingleLineMessageModel
import pl.edu.pg.eti.domain.model.MessageModel
import pl.edu.pg.eti.domain.repository.RabbitRepository
import timber.log.Timber
import java.io.IOException
import java.nio.charset.StandardCharsets
import java.sql.Time

class RabbitRepositoryImpl : RabbitRepository {
    override fun sendDrawLine(
        canvaSingleLine: CanvaSingleLineMessageModel,
        sessionManager: SessionManager,
        exchangeName: String
    ) {
        sessionManager.channel.basicPublish(
            exchangeName,
            "",
            null,
            canvaSingleLine.toCSV().toByteArray()
        )
    }

    override fun sendGuess(
        sessionManager: SessionManager,
        messageModel: MessageModel,
        exchangeName: String
    ) {
        sessionManager.channel.basicPublish(
            exchangeName,
            "",
            null,
            messageModel.toCSV().toByteArray()
        )
    }
}