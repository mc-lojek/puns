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
import pl.edu.pg.eti.presentation.viewmodel.EntryViewModel
import kotlin.random.Random

@AndroidEntryPoint
class EntryFragment : Fragment() {
    private val viewModel: EntryViewModel by viewModels()
    private lateinit var binding: FragmentEntryBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_entry,
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
        binding.loginBtn.setOnClickListener {
            findNavController().navigate(R.id.action_entryFragment_to_scoreboardFragment2)
        }
        binding.guessingBtn.setOnClickListener {
            val bundle = Bundle()
            bundle.putString("queue_name", "room-14-15")
            findNavController().navigate(R.id.action_entryFragment_to_game_nav_graph, bundle)
        }
        binding.etID.setText(Random.nextInt().toString())
        binding.btnFastGame.setOnClickListener {
            val id = binding.etID.text.toString().toLong()
            viewModel.joinRoom(id)
        }
    }

    private fun setupObservers() {
        viewModel.roomJoinLiveData.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Success -> {
                    val bundle = Bundle()
                    bundle.putString("queue_name", it.data!!.queueName)
                    bundle.putString("exchange_name", it.data!!.exchangeName)
                    findNavController().navigate(R.id.action_entryFragment_to_game_nav_graph, bundle)
                }
                is Resource.Error -> {
                    Snackbar.make(binding.root, it.message.toString(), Snackbar.LENGTH_SHORT)
                }
                is Resource.Loading -> {}
            }
        }


    }
}