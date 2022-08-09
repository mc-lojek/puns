package pl.edu.pg.eti.presentation.viewmodel

import android.graphics.Paint
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import com.rabbitmq.client.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import pl.edu.pg.eti.data.util.RabbitConnectionParameters
import pl.edu.pg.eti.domain.model.CanvaSingleLineMessageModel
import pl.edu.pg.eti.domain.repository.RabbitRepository
import timber.log.Timber
import java.io.IOException
import java.nio.charset.StandardCharsets
import java.util.concurrent.Flow
import javax.inject.Inject

@HiltViewModel
class GuessingViewModel @Inject constructor(
    val repo: RabbitRepository
) : ViewModel() {
    private lateinit var rabbitConnectionParameters: RabbitConnectionParameters
    val consumerTag = "SimpleConsumer"

    val cancelCallback = CancelCallback { consumerTag: String? ->
        println("[$consumerTag] was canceled")
    }

    fun createServerConnection(deliveryCallback: DeliverCallback) = viewModelScope.launch {
        withContext(Dispatchers.IO) {
            try {
                rabbitConnectionParameters = repo.createServerConnection()
                val message =
                    rabbitConnectionParameters.channel.basicConsume(
                        "test_queue",
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


}


