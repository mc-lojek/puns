package pl.edu.pg.eti.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rabbitmq.client.CancelCallback
import com.rabbitmq.client.DeliverCallback
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import pl.edu.pg.eti.domain.manager.SessionManager
import pl.edu.pg.eti.domain.model.CanvaSingleLineMessageModel
import pl.edu.pg.eti.domain.repository.RabbitRepository
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class DrawingViewModel @Inject constructor(
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



    fun sendSingleLineModel(
        floatStartX: Float,
        floatStartY: Float,
        floatEndX: Float,
        floatEndY: Float,
        paintColor: Int,
        paintSize: Float,
        queueName: String
    ) = viewModelScope.launch {
        withContext(Dispatchers.IO) {
            repo.sendDrawLine(
                CanvaSingleLineMessageModel(
                    floatStartX,
                    floatStartY,
                    floatEndX,
                    floatEndY,
                    paintColor,
                    paintSize
                ), sessionManager, queueName
            )
        }
    }
}