package pl.edu.pg.eti.data.repository

import android.graphics.Paint
import android.util.Log
import com.rabbitmq.client.Channel
import com.rabbitmq.client.Connection
import com.rabbitmq.client.ConnectionFactory
import okio.ByteString.Companion.toByteString
import pl.edu.pg.eti.data.util.RabbitConnectionParameters
import pl.edu.pg.eti.domain.model.CanvaSingleLineMessageModel
import pl.edu.pg.eti.domain.repository.RabbitRepository
import timber.log.Timber
import java.io.IOException
import java.nio.charset.StandardCharsets
import java.sql.Time

class RabbitRepositoryImpl : RabbitRepository {
    /*
     override fun createServerConnection(): RabbitConnectionParameters {
         val factory = ConnectionFactory()
         Timber.i("Creating connection to rabbit")
         factory.host = "sparrow-01.rmq.cloudamqp.com"
         factory.username = "ljgnrjzx"
         factory.port = 5672
         factory.password = "8_bkQrkcFCVHK0FpqEldQdu8sId5p7Xu"
         factory.virtualHost = "ljgnrjzx"
         val connection = factory.newConnection()
         val channel = connection.createChannel()
         //channel.queueDeclare("test_queue", false, false, false, null)
        // channel.queueDeclare("test_queue1", false, false, false, null)
         //channel.queueDeclare("test_queue2", false, false, false, null)
         Timber.i("Connection to rabbit created")
         return RabbitConnectionParameters(factory, connection, channel)
     }*/

    override fun sendDrawLine(
        canvaSingleLine: CanvaSingleLineMessageModel,
        channel: Channel,
        queueName: String
    ) {

        channel.basicPublish(
            "",
            queueName,
            null,
            canvaSingleLine.toCSV().toByteArray()
        )
        Timber.i(
            canvaSingleLine.toCSV().toString()
        )

    }
}