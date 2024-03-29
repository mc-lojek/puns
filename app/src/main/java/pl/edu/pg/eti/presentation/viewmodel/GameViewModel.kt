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
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import pl.edu.pg.eti.data.network.Resource
import pl.edu.pg.eti.domain.manager.SessionManager
import pl.edu.pg.eti.domain.model.ScoreboardItemModel
import pl.edu.pg.eti.domain.model.events.ClearCanvasEvent
import pl.edu.pg.eti.domain.model.events.DrawLineEvent
import pl.edu.pg.eti.domain.model.events.HostStartEvent
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
        Timber.d("[$consumerTag] was canceled")
        _rabbitConnectionClosed.postValue("Rabbit closed")
    }
    val deliverCallback = DeliverCallback { consumerTag: String?, delivery: Delivery ->
        val message = String(delivery.body, StandardCharsets.UTF_8)
        callback?.let { it(message) }
        Timber.d("Received: '$message'")
    }

    var isInitialized = false
    var callback: ((String) -> Unit)? = null
    var keyword = ""
    var roundsPassed = 0
    var roundsLeft = 0
    var scoreboardList = listOf<ScoreboardItemModel>()
    var basicRoundTime = 0L


    private val _timeLeftLiveData: MutableLiveData<Long> = MutableLiveData()
    val timeLeftLiveData: LiveData<Long> = _timeLeftLiveData

    private val _roomLeaveLiveData: MutableLiveData<Resource<String>> = MutableLiveData()
    val roomLeaveLiveData: LiveData<Resource<String>> = _roomLeaveLiveData

    private val _rabbitConnectionClosed: MutableLiveData<String> = MutableLiveData()
    val rabbitConnectionClosed: LiveData<String> = _rabbitConnectionClosed

    fun initializeAndConsume(queueName: String, exchangeName: String,playerId:Long,playerNickname:String) =
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                sessionManager.exchangeName = exchangeName
                sessionManager.queueName = queueName
                sessionManager.playerId = playerId
                sessionManager.playerNickname = playerNickname
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
                    sessionManager.playerId,
                    sessionManager.playerNickname,
                    content
                )
            )
            Timber.d("Wysylam id ${sessionManager.playerId} i nick ${sessionManager.playerNickname}")
        }
    }

    fun sendClearCanvas() = viewModelScope.launch {
        withContext(Dispatchers.IO) {
            sessionManager.publish(ClearCanvasEvent())
        }
    }

    fun sendStartGameEvent(id:Long) = viewModelScope.launch {
        withContext(Dispatchers.IO) {
            sessionManager.publish(HostStartEvent(id))
        }
    }

    fun sendPlayerReady() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                Timber.d("Player ready event")
                sessionManager.publish(
                    PlayerReadyEvent(
                        sessionManager.playerId
                    )
                )
            }
        }
    }

    fun leaveRoom() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                _roomLeaveLiveData.postValue(Resource.Loading())
                val splittedQueue = sessionManager.queueName.split(".")
                val result = repo.leaveRoom(splittedQueue[1].toLong(),sessionManager.playerId,sessionManager.playerNickname)//todo id pokoju
                _roomLeaveLiveData.postValue(result)
            }
        }
    }
}