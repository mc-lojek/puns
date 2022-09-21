package pl.edu.pg.eti.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.navGraphViewModels
import dagger.hilt.android.AndroidEntryPoint
import pl.edu.pg.eti.R
import pl.edu.pg.eti.databinding.FragmentLobbyBinding
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val queueName = requireArguments().getString("queue_name")
        val exchangeName = requireArguments().getString("exchange_name")!!
        Timber.d("queue ${queueName} exchange: ${exchangeName}")
        viewModel.initializeAndConsume(queueName!!, exchangeName)

        binding.drawing.setOnClickListener {
            findNavController().navigate(R.id.action_lobbyFragment_to_drawingFragment)
        }
        binding.guess.setOnClickListener {
            findNavController().navigate(R.id.action_lobbyFragment_to_guessingFragment)

        }
    }

}