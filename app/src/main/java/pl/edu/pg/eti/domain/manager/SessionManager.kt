package pl.edu.pg.eti.domain.manager

import com.rabbitmq.client.Channel
import com.rabbitmq.client.Connection
import com.rabbitmq.client.ConnectionFactory

class SessionManager {
    private lateinit var factory: ConnectionFactory
    private lateinit var connection: Connection
    private lateinit var channel: Channel

    fun initSessionManager(
        sHost: String,
        sUsername: String,
        sPort: Int,
        sPassword: String,
        sVirtualHost: String
    ) {
        factory.apply {
            host = sHost                //"sparrow-01.rmq.cloudamqp.com"
            username = sUsername        //"ljgnrjzx"
            port = sPort                //5672
            password = sPassword        //"8_bkQrkcFCVHK0FpqEldQdu8sId5p7Xu"
            virtualHost = sVirtualHost  //"ljgnrjzx"
        }
        connection = factory.newConnection()
        channel = connection.createChannel()
    }

    fun declareNewQueue(queueName: String) {
        channel.queueDeclare(queueName, false, false, false, null)
    }

}