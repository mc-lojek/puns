package pl.edu.pg.eti.presentation.viewmodel

import android.graphics.Paint
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rabbitmq.client.CancelCallback
import com.rabbitmq.client.DeliverCallback
import com.rabbitmq.client.Delivery
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import pl.edu.pg.eti.domain.manager.SessionManager
import pl.edu.pg.eti.domain.model.CanvaSingleLineMessageModel
import pl.edu.pg.eti.domain.model.MessageModel
import pl.edu.pg.eti.domain.repository.RabbitRepository
import timber.log.Timber
import java.io.IOException
import java.nio.charset.StandardCharsets
import javax.inject.Inject

@HiltViewModel
class GameViewModel @Inject constructor(
    val repo: RabbitRepository
) : ViewModel() {
    lateinit var sessionManager: SessionManager
    val consumerTag = "SimpleConsumer"

    val cancelCallback = CancelCallback { consumerTag: String? ->
        println("[$consumerTag] was canceled")
    }
    val deliverCallback = DeliverCallback { consumerTag: String?, delivery: Delivery ->
        val message = String(delivery.body, StandardCharsets.UTF_8)
        callback?.let { it(message) }
        println("[$consumerTag] Received: '$message'")
    }

    var callback : ((String)->Unit)?=null



    fun initializeAndConsume(queueName: String) =//, deliveryCallback: DeliverCallback) =
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                sessionManager = SessionManager()
                sessionManager.initSessionManager(
                    "51.83.130.165",
                    "admin",
                    5672,
                    "lamper123",
                    "/puns"
                )
                try {
                    val message =
                        sessionManager.channel.basicConsume(
                            queueName,
                            true,
                            consumerTag,
                            deliverCallback,
                            cancelCallback
                        )
                } catch (ex: IOException) {
                    print(ex.stackTrace)
                }
            }
        }


    fun sendSingleLineModel(
        floatStartX: Float,
        floatStartY: Float,
        floatEndX: Float,
        floatEndY: Float,
        paintColor: Int,
        paintSize: Float
    ) = viewModelScope.launch {
        withContext(Dispatchers.IO) {

            val tempCanvaMessage = CanvaSingleLineMessageModel(
                floatStartX,
                floatStartY,
                floatEndX,
                floatEndY,
                paintColor,
                paintSize
            )

            repo.sendDrawLine(
                tempCanvaMessage, sessionManager, "room-11"//todo exchange name
            )
        }
    }

    fun sendGuess(content: String) = viewModelScope.launch {
        withContext(Dispatchers.IO) {
            repo.sendGuess(
                sessionManager,
                MessageModel("guessingActor", content),
                "room-11"//todo exchange name
            )
        }
    }

}