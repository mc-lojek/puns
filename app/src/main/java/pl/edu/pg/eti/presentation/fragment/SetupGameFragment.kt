package pl.edu.pg.eti.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import pl.edu.pg.eti.R
import pl.edu.pg.eti.data.network.Resource
import pl.edu.pg.eti.databinding.FragmentEntryBinding
import pl.edu.pg.eti.databinding.FragmentSetupGameBinding
import pl.edu.pg.eti.domain.model.RoomConfig
import pl.edu.pg.eti.domain.model.events.StartGameEvent
import pl.edu.pg.eti.presentation.viewmodel.EntryViewModel
import pl.edu.pg.eti.presentation.viewmodel.SetupGameViewModel
import timber.log.Timber
import kotlin.random.Random

@AndroidEntryPoint
class SetupGameFragment : Fragment() {
    private val viewModel: SetupGameViewModel by viewModels()
    private lateinit var binding: FragmentSetupGameBinding
    private var id = -1L
    private var nickname = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_setup_game,
            container,
            false
        )
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        nickname = requireArguments().getString("nickname")!!
        id = requireArguments().getLong("id")
        setupListeners()
        setupObservers()
    }

    private fun setupListeners() {
        binding.btnBack.setOnClickListener {
            findNavController().navigateUp()
        }
        binding.btnStartLobby.setOnClickListener {
            viewModel.joinRoom(id,nickname, RoomConfig(1,3,1_000_000_000))
        }
    }

    private fun setupObservers() {
        viewModel.roomJoinLiveData.observe(viewLifecycleOwner) {
            Timber.d("elo ${it}")
            when (it) {
                is Resource.Success -> {
                    val bundle = Bundle()
                    bundle.putString("queue_name", it.data!!.queueName)
                    bundle.putString("exchange_name", it.data!!.exchangeName)
                    bundle.putString("hash", it.data!!.hash)
                    Timber.d(it.data!!.hash)
                    findNavController().navigate(
                        R.id.action_setupGameFragment_to_game_nav_graph,
                        bundle
                    )


                    viewModel.clearLiveData()
                }
                is Resource.Error -> {
                    Snackbar.make(binding.root, it.message.toString(), Snackbar.LENGTH_SHORT)
                }
                is Resource.Loading -> {}
            }
        }


    }
}