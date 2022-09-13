package pl.edu.pg.eti.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rabbitmq.client.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import pl.edu.pg.eti.domain.manager.SessionManager
import pl.edu.pg.eti.domain.model.MessageModel
import pl.edu.pg.eti.domain.repository.RabbitRepository
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class GuessingViewModel @Inject constructor(
    val repo: RabbitRepository
) : ViewModel() {
    lateinit var sessionManager: SessionManager
    val consumerTag = "SimpleConsumer"

    val cancelCallback = CancelCallback { consumerTag: String? ->
        println("[$consumerTag] was canceled")
    }

    fun initializeSessionManager() = viewModelScope.launch {
        withContext(Dispatchers.IO) {
            sessionManager = SessionManager()
            sessionManager.initSessionManager(
                "sparrow-01.rmq.cloudamqp.com",
                "ljgnrjzx",
                5672,
                "8_bkQrkcFCVHK0FpqEldQdu8sId5p7Xu",
                "ljgnrjzx"
            )
        }
    }

    fun initializeAndConsume(queueName: String, deliveryCallback: DeliverCallback) =
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                sessionManager = SessionManager()
                sessionManager.initSessionManager(
                    "sparrow-01.rmq.cloudamqp.com",
                    "ljgnrjzx",
                    5672,
                    "8_bkQrkcFCVHK0FpqEldQdu8sId5p7Xu",
                    "ljgnrjzx"
                )
                delay(5000)
                try {
                    val message =
                        sessionManager.channel.basicConsume(
                            queueName,
                            true,
                            consumerTag,
                            deliveryCallback,
                            cancelCallback
                        )
                } catch (ex: IOException) {
                    print(ex.stackTrace)
                }
            }
        }

    fun sendGuess(content: String) = viewModelScope.launch {
        withContext(Dispatchers.IO) {
            repo.sendGuess(
                sessionManager,
                MessageModel("guessingActor", content),
                "test_mess"
            )
        }
    }

}


