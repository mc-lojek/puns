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
import kotlinx.coroutines.launch
import pl.edu.pg.eti.R
import pl.edu.pg.eti.databinding.FragmentScoreboardBinding
import pl.edu.pg.eti.domain.model.events.StartRoundEvent
import pl.edu.pg.eti.presentation.viewmodel.GameViewModel

@AndroidEntryPoint
class ScoreboardFragment : Fragment() {

    private lateinit var binding: FragmentScoreboardBinding
    private val viewModel: GameViewModel by navGraphViewModels(R.id.game_nav_graph) { defaultViewModelProviderFactory }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_scoreboard,
            container,
            false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.scoreboard.bind(viewModel.scoreboardList)
        val isFinal = requireArguments().getBoolean("isFinal")
        if(isFinal){
            binding.btnExitFinal.visibility = View.VISIBLE
        }
        viewModel.sendPlayerReady()
        consumeMessages()

        binding.btnExitFinal.setOnClickListener {
            findNavController().popBackStack(R.id.mainMenuFragment, false)
        }

        viewModel.rabbitConnectionClosed.observe(viewLifecycleOwner) {
            if(it.equals("Rabbit closed")){
                binding.btnExitFinal.visibility = View.VISIBLE
            }
        }
    }

    fun consumeMessages() {
        viewModel.callback = {
            when (it.substring(0, 3)) {
                "SRE" -> {
                    val startRoundEvent = StartRoundEvent(it)
                    lifecycleScope.launch {
                        viewModel.roundsPassed = startRoundEvent.roundsPassed
                        viewModel.roundsLeft = startRoundEvent.roundsLeft
                        if (startRoundEvent.drawingId == viewModel.sessionManager.queueName.substringAfterLast(
                                "."
                            ).toLong()
                        ) {
                            viewModel.keyword = startRoundEvent.keyword
                            findNavController().navigate(R.id.action_scoreboardFragment_to_drawingFragment)
                        } else {
                            findNavController().navigate(R.id.action_scoreboardFragment_to_guessingFragment)
                        }
                    }
                }
                else -> {
                    null
                }
            }
        }
    }
}