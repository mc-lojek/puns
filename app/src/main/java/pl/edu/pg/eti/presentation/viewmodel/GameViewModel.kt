package pl.edu.pg.eti.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rabbitmq.client.CancelCallback
import com.rabbitmq.client.DeliverCallback
import com.rabbitmq.client.Delivery
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import pl.edu.pg.eti.domain.manager.SessionManager
import pl.edu.pg.eti.domain.model.DrawLineEvent
import pl.edu.pg.eti.domain.model.PlayerGuessEvent
import java.io.IOException
import java.nio.charset.StandardCharsets
import javax.inject.Inject

@HiltViewModel
class GameViewModel @Inject constructor(
    val sessionManager: SessionManager
) : ViewModel() {
    val consumerTag = "SimpleConsumer"

    val cancelCallback = CancelCallback { consumerTag: String? ->
        println("[$consumerTag] was canceled")
    }
    val deliverCallback = DeliverCallback { consumerTag: String?, delivery: Delivery ->
        val message = String(delivery.body, StandardCharsets.UTF_8)
        callback?.let { it(message) }
        println("[$consumerTag] Received: '$message'")
    }

    var callback: ((String) -> Unit)? = null


    fun initializeAndConsume(queueName: String, exchangeName: String) =
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                sessionManager.exchangeName = exchangeName
                sessionManager.queueName = queueName
                try {
                    sessionManager.initSessionManager()
                    sessionManager.consume(deliverCallback, cancelCallback)

                } catch (ex: IOException) {
                    print(ex.stackTrace)
                }

            }
        }


    fun sendDrawLineEvent(
        floatStartX: Float,
        floatStartY: Float,
        floatEndX: Float,
        floatEndY: Float,
        paintColor: Int,
        paintSize: Float
    ) = viewModelScope.launch {
        withContext(Dispatchers.IO) {
            sessionManager.publish(
                DrawLineEvent(
                    floatStartX,
                    floatStartY,
                    floatEndX,
                    floatEndY,
                    paintColor,
                    paintSize
                )
            )
        }
    }

    fun sendGuess(content: String) = viewModelScope.launch {
        withContext(Dispatchers.IO) {
            sessionManager.publish(
                PlayerGuessEvent(
                    "guessing person nick",//todo nickname gracza
                    content
                )
            )
        }
    }

}