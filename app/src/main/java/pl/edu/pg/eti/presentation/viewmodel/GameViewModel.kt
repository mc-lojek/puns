package pl.edu.pg.eti.presentation.viewmodel

import PlayerGuessEvent
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rabbitmq.client.CancelCallback
import com.rabbitmq.client.DeliverCallback
import com.rabbitmq.client.Delivery
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import pl.edu.pg.eti.data.network.Resource
import pl.edu.pg.eti.domain.manager.SessionManager
import pl.edu.pg.eti.domain.model.RoomJoin
import pl.edu.pg.eti.domain.model.ScoreboardItemModel
import pl.edu.pg.eti.domain.model.events.ClearCanvasEvent
import pl.edu.pg.eti.domain.model.events.DrawLineEvent
import pl.edu.pg.eti.domain.model.events.PlayerReadyEvent
import pl.edu.pg.eti.domain.repository.GameRepository
import timber.log.Timber
import java.io.IOException
import java.nio.charset.StandardCharsets
import javax.inject.Inject

@HiltViewModel
class GameViewModel @Inject constructor(
    val sessionManager: SessionManager,
    val repo: GameRepository
) : ViewModel() {
    val cancelCallback = CancelCallback { consumerTag: String? ->
        println("[$consumerTag] was canceled")
    }
    val deliverCallback = DeliverCallback { consumerTag: String?, delivery: Delivery ->
        val message = String(delivery.body, StandardCharsets.UTF_8)
        callback?.let { it(message) }
        println("Received: '$message'")
    }

    var isInitialized = false
    var callback: ((String) -> Unit)? = null
    var keyword = ""
    var roundsPassed = 0
    var roundsLeft = 0
    var scoreboardList = listOf<ScoreboardItemModel>()


    private val _timeLeftLiveData: MutableLiveData<Long> = MutableLiveData()
    val timeLeftLiveData: LiveData<Long> = _timeLeftLiveData

    private val _roomLeaveLiveData: MutableLiveData<Resource<String>> = MutableLiveData()
    val roomLeaveLiveData: LiveData<Resource<String>> = _roomLeaveLiveData

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
                isInitialized = true
                //delay(12000)
                //sendPlayerReady()
            }
        }


    fun sendDrawLineEvent(
        floatStartX: Float,
        floatStartY: Float,
        floatEndX: Float,
        floatEndY: Float,
        paintColor: Int,
        paintSize: Float,
        screenRatio: Float
    ) = viewModelScope.launch {
        withContext(Dispatchers.IO) {
            sessionManager.publish(
                DrawLineEvent(
                    floatStartX/screenRatio,
                    floatStartY/screenRatio,
                    floatEndX/screenRatio,
                    floatEndY/screenRatio,
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
                    sessionManager.queueName.substringAfterLast("-").toLong(),//todo id gracza
                    sessionManager.queueName.substringAfterLast("-"),//todo nickname gracza
                    content
                )
            )
        }
    }

    fun sendClearCanvas() = viewModelScope.launch {
        withContext(Dispatchers.IO) {
            sessionManager.publish(ClearCanvasEvent())
        }
    }

    fun sendPlayerReady() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                Timber.d("Player ready event")
                sessionManager.publish(
                    PlayerReadyEvent(
                        sessionManager.queueName.substringAfterLast("-").toLong()//todo id gracza
                    )
                )
            }
        }
    }

    fun leaveRoom() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                _roomLeaveLiveData.postValue(Resource.Loading())
                val splittedQueue = sessionManager.queueName.split("-")
                val result = repo.leaveRoom(splittedQueue[1].toLong(),splittedQueue[2].toLong(),"user1")//todo id gracza, name gracza
                _roomLeaveLiveData.postValue(result)
            }
        }
    }
}