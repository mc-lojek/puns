package pl.edu.pg.eti.data.util

import com.rabbitmq.client.Channel
import com.rabbitmq.client.Connection
import com.rabbitmq.client.ConnectionFactory

data class RabbitConnectionParameters(
    var factory: ConnectionFactory,
    var connection: Connection,
    var channel: Channel
) {
}