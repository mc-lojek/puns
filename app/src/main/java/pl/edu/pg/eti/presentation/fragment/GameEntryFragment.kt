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
import pl.edu.pg.eti.databinding.FragmentGameEntryBinding
import pl.edu.pg.eti.presentation.viewmodel.GameEntryViewModel
import timber.log.Timber
import kotlin.random.Random

@AndroidEntryPoint
class GameEntryFragment : Fragment() {
    private val viewModel: GameEntryViewModel by viewModels()
    private lateinit var binding: FragmentGameEntryBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_game_entry,
            container,
            false
        )
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupListeners()
        setupObservers()
    }

    private fun setupListeners() {
        binding.etID.setText((Random.nextInt(5,7)).toString())
        binding.btnFastGame.setOnClickListener {
            val id = binding.etID.text.toString().toLong()
            viewModel.joinRoom(id,"name1")//todo nickname gracza
        }
        binding.btnJoinRoom.setOnClickListener {
            val hash = binding.etRoomHash.text.toString().uppercase()
            binding.etRoomHash.setText("")
            //walidacja czy wpisany hash ma 6 znaków 0-9, A-Z

            val id = binding.etID.text.toString().toLong()
            viewModel.joinRoom(id,"name1", hash)//todo nickname gracza

        }

        binding.btnCreateRoom.setOnClickListener {
            val id = binding.etID.text.toString().toLong()
            val bundle = Bundle()
            bundle.putLong("id", id)
            bundle.putString("nickname", "name1")//todo nickname gracza
            findNavController().navigate(R.id.action_entryFragment_to_setupGameFragment,bundle)
        }
    }

    private fun setupObservers() {
        viewModel.roomJoinLiveData.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Success -> {
                    val bundle = Bundle()
                    bundle.putString("queue_name", it.data!!.queueName)
                    bundle.putString("exchange_name", it.data!!.exchangeName)
                    bundle.putString("hash",it.data!!.hash)
                    bundle.putLong("time",it.data!!.roundTime/1_000_000)
                    bundle.putInt("playersCount",it.data!!.playersCount)
                    bundle.putInt("maxPlayers",it.data!!.maxPlayers)
                    findNavController().navigate(R.id.action_entryFragment_to_game_nav_graph, bundle)


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