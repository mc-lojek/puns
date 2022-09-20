package pl.edu.pg.eti.presentation.fragment

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver.OnGlobalLayoutListener
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.navGraphViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.rabbitmq.client.DeliverCallback
import com.rabbitmq.client.Delivery
import dagger.hilt.android.AndroidEntryPoint
import pl.edu.pg.eti.R
import pl.edu.pg.eti.databinding.FragmentGuessingBinding
import pl.edu.pg.eti.databinding.FragmentLobbyBinding
import pl.edu.pg.eti.domain.manager.SessionManager
import pl.edu.pg.eti.domain.model.CanvaSingleLineMessageModel
import pl.edu.pg.eti.domain.model.MessageModel
import pl.edu.pg.eti.presentation.adapter.MessageRecyclerViewAdapter
import pl.edu.pg.eti.presentation.viewmodel.DrawingViewModel
import pl.edu.pg.eti.presentation.viewmodel.GameViewModel
import pl.edu.pg.eti.presentation.viewmodel.GuessingViewModel
import java.nio.charset.StandardCharsets
import javax.inject.Inject
import kotlin.math.sin

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
        viewModel.initializeAndConsume(queueName!!)
        binding.drawing.setOnClickListener {
            findNavController().navigate(R.id.action_lobbyFragment_to_drawingFragment)
        }
        binding.guess.setOnClickListener {
            findNavController().navigate(R.id.action_lobbyFragment_to_guessingFragment)

        }
    }

}