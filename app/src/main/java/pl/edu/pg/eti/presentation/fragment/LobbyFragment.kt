package pl.edu.pg.eti.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.navGraphViewModels
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import pl.edu.pg.eti.R
import pl.edu.pg.eti.databinding.FragmentLobbyBinding
import pl.edu.pg.eti.domain.model.events.StartRoundEvent
import pl.edu.pg.eti.presentation.viewmodel.GameViewModel
import timber.log.Timber

@AndroidEntryPoint
class LobbyFragment : Fragment() {

    private lateinit var binding: FragmentLobbyBinding
    private val viewModel: GameViewModel by navGraphViewModels(R.id.game_nav_graph) { defaultViewModelProviderFactory }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_lobby,
            container,
            false
        )
        return binding.root
    }


    fun consumeMessages() {
        viewModel.callback = {
            when (it.substring(0, 3)) {
                "SRE" -> {
                    val startRoundEvent = StartRoundEvent(it)
                    lifecycleScope.launch{
                        delay(2000)
                        if (startRoundEvent.drawingId == viewModel.sessionManager.queueName.substringAfterLast(
                                "-"
                            ).toLong()
                        ) {
                            findNavController().navigate(R.id.action_lobbyFragment_to_drawingFragment)
                        } else {
                            findNavController().navigate(R.id.action_lobbyFragment_to_guessingFragment)
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
        val queueName = requireArguments().getString("queue_name")
        val exchangeName = requireArguments().getString("exchange_name")!!
        Timber.d("queue ${queueName} exchange: ${exchangeName}")
        viewModel.initializeAndConsume(queueName!!, exchangeName)
        consumeMessages()


        binding.drawing.setOnClickListener {
            findNavController().navigate(R.id.action_lobbyFragment_to_drawingFragment)
        }
        binding.guess.setOnClickListener {
            findNavController().navigate(R.id.action_lobbyFragment_to_guessingFragment)

        }
    }

}