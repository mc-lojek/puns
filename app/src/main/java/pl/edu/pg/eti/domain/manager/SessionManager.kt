package pl.edu.pg.eti.domain.manager

import android.text.Spannable
import com.rabbitmq.client.*
import kotlinx.coroutines.*

class SessionManager(
    val sHost: String,
    val sUsername: String,
    val sPort: Int,
    val sPassword: String,
    val sVirtualHost: String,
) {
    private lateinit var factory: ConnectionFactory
    private lateinit var connection: Connection
    lateinit var exchangeName: String
    lateinit var queueName: String
    private lateinit var channel: Channel

    fun initSessionManager() {
        factory = ConnectionFactory()
        factory.apply {
            host = sHost
            username = sUsername
            port = sPort
            password = sPassword
            virtualHost = sVirtualHost
        }
        connection = factory.newConnection()
        channel = connection.createChannel()

    }

    fun publish(message: String) {
        channel.basicPublish(
            exchangeName,
            "s",
            null,
            message.toByteArray()
        )
    }

    fun consume(deliverCallback: DeliverCallback, cancelCallback: CancelCallback) {
        channel.basicConsume(
            queueName,
            true,
            "",
            deliverCallback,
            cancelCallback
        )
    }

}