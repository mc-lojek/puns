package pl.edu.pg.eti.presentation.viewmodel

import android.graphics.Paint
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rabbitmq.client.Channel
import com.rabbitmq.client.Connection
import com.rabbitmq.client.ConnectionFactory
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import pl.edu.pg.eti.data.util.RabbitConnectionParameters
import pl.edu.pg.eti.domain.model.CanvaSingleLineMessageModel
import pl.edu.pg.eti.domain.repository.LoginRepository
import pl.edu.pg.eti.domain.repository.RabbitRepository
import pl.edu.pg.eti.presentation.adapter.MessageRecyclerViewAdapter
import java.io.IOException
import java.nio.charset.StandardCharsets
import java.util.concurrent.TimeoutException
import javax.inject.Inject

@HiltViewModel
class DrawingViewModel @Inject constructor(
    val repo: RabbitRepository
) : ViewModel() {
    private lateinit var rabbitConnectionParameters: RabbitConnectionParameters

    fun createServerConnection() = viewModelScope.launch {
        withContext(Dispatchers.IO) {
            try {
                rabbitConnectionParameters = repo.createServerConnection()
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
        paint: Paint
    ) = viewModelScope.launch {
        withContext(Dispatchers.IO) {
            repo.sendDrawLine(
                CanvaSingleLineMessageModel(
                    floatStartX,
                    floatStartY,
                    floatEndX,
                    floatEndY,
                    paint
                ), rabbitConnectionParameters
            )
        }
    }
}