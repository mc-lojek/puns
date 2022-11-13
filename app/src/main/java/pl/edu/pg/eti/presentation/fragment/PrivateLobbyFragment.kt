package pl.edu.pg.eti.presentation.fragment

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.navGraphViewModels
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import pl.edu.pg.eti.R
import pl.edu.pg.eti.data.network.Resource
import pl.edu.pg.eti.databinding.FragmentPrivatelobbyBinding
import pl.edu.pg.eti.domain.model.events.PlayerJoinedEvent
import pl.edu.pg.eti.domain.model.events.PlayerLeftEvent
import pl.edu.pg.eti.domain.model.events.StartGameEvent
import pl.edu.pg.eti.domain.model.events.StartRoundEvent
import pl.edu.pg.eti.domain.util.TIME_TOAST_MESSAGE_LEFT_JOIN_EVENT
import pl.edu.pg.eti.presentation.viewmodel.GameViewModel
import timber.log.Timber

@AndroidEntryPoint
class PrivateLobbyFragment : Fragment() {

    private lateinit var binding: FragmentPrivatelobbyBinding
    private val viewModel: GameViewModel by navGraphViewModels(R.id.game_nav_graph) { defaultViewModelProviderFactory }
    private var playersCountSyncJob: Job? = null
    private var hash = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_privatelobby,
            container,
            false
        )
        return binding.root
    }

    fun setPlyerCount(count: Int) {
        //binding.tvPlayersJoined.text = count.toString()
    }


    fun consumeMessages() {
        viewModel.callback = {
            when (it.substring(0, 3)) {
                "PJE" -> {
                    val playerJoinedEvent = PlayerJoinedEvent(it)
                    playersCountSyncJob?.cancel()
                    playersCountSyncJob = lifecycleScope.launch(Dispatchers.Main) {
                        //setPlyerCount(playerJoinedEvent.playersCount)
                        Timber.d("Dostalem taki playerJoinedEvent: ${playerJoinedEvent}")
                    }
                    val snackbar = Snackbar.make(
                        requireView(), "Player ${playerJoinedEvent.nickname} joined",
                        Snackbar.LENGTH_LONG
                    ).setAction("Action", null)
                    snackbar.show()
                }
                "PLE" -> {
                    val playerLeftEvent = PlayerLeftEvent(it)
                    playersCountSyncJob?.cancel()
                    playersCountSyncJob = lifecycleScope.launch(Dispatchers.Main) {
                        //setPlyerCount(playerLeftEvent.playersCount)
                        Timber.d("Dostalem taki playerLeftEvent: ${playerLeftEvent}")
                    }
                    val snackbar = Snackbar.make(
                        requireView(), "Player ${playerLeftEvent.nickname} left",
                        Snackbar.LENGTH_LONG
                    ).setAction("Action", null)
                    snackbar.show()
                }
                "SGE" -> {
                    val startGameEvent = StartGameEvent(it)
                    playersCountSyncJob?.cancel()
                    playersCountSyncJob = lifecycleScope.launch(Dispatchers.Main) {
                        //setPlyerCount(startGameEvent.playersCount)
                    }
                    Timber.d("Dostalem taki startGameEvent: ${startGameEvent}")
                    viewModel.sendPlayerReady()
                }
                "SRE" -> {
                    val startRoundEvent = StartRoundEvent(it)
                    lifecycleScope.launch {
                        viewModel.roundsPassed = startRoundEvent.roundsPassed
                        viewModel.roundsLeft = startRoundEvent.roundsLeft
                        if (startRoundEvent.drawingId == viewModel.sessionManager.playerId
                        ) {
                            viewModel.keyword = startRoundEvent.keyword
                            findNavController().navigate(R.id.action_privateLobbyFragment_to_drawingFragment)
                        } else {
                            findNavController().navigate(R.id.action_privateLobbyFragment_to_guessingFragment)
                        }
                    }
                }
                else -> {
                    null
                }
            }
        }
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (!viewModel.isInitialized) {
            val queueName = requireArguments().getString("queue_name")
            val exchangeName = requireArguments().getString("exchange_name")!!
            Timber.d("queue ${queueName} exchange: ${exchangeName}")
            hash = requireArguments().getString("hash").toString()
            binding.tvHash.text = hash
            binding.btnHash.text = hash
            viewModel.initializeAndConsume(queueName!!, exchangeName)
            viewModel.basicRoundTime = requireArguments().getLong("time")
        } else {
            //viewModel.sendPlayerReady()
            Timber.d("ViewModel is already initialized")
        }
        consumeMessages()

        binding.btnHash.setOnClickListener {
            val clipboard =
                requireContext().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clip = ClipData.newPlainText("hash", hash)
            clipboard.setPrimaryClip(clip)

            val snackbar = Snackbar.make(
                requireView(), "Copied to clipboard",
                Snackbar.LENGTH_LONG
            ).setAction("Action", null)
            snackbar.show()
        }


        binding.btnBack3.setOnClickListener {
            viewModel.leaveRoom()
            viewModel.roomLeaveLiveData.observe(viewLifecycleOwner) {
                when (it) {
                    is Resource.Error -> {
                        Timber.d("error")
                    }
                    is Resource.Loading -> {
                        Timber.d("loading")
                    }
                    is Resource.Success -> {
                        findNavController().popBackStack(R.id.game_nav_graph, true)
                    }
                }

            }
        }
    }
}