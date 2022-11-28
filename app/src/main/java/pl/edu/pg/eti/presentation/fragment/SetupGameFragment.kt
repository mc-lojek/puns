package pl.edu.pg.eti.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.SeekBar
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import pl.edu.pg.eti.R
import pl.edu.pg.eti.data.network.Resource
import pl.edu.pg.eti.databinding.FragmentSetupGameBinding
import pl.edu.pg.eti.domain.model.RoomConfig
import pl.edu.pg.eti.presentation.viewmodel.SetupGameViewModel
import timber.log.Timber

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

        ArrayAdapter.createFromResource(
            requireContext(),
            R.array.time_array,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
            binding.spinnerTime.adapter = adapter
            binding.spinnerTime.setSelection(2)
        }


        setupListeners()
        setupObservers()
    }

    private fun setupListeners() {
        binding.backBtn.setOnClickListener {
            findNavController().navigateUp()
        }
        binding.btnStartLobby.setOnClickListener {

            viewModel.joinRoom(
                id,
                nickname,
                RoomConfig(
                    binding.sbNumRounds.progress,
                    binding.sbNumPlayers.progress,
                    binding.spinnerTime.selectedItem.toString().toLong() * 1_000_000_000
                )
            )
        }
        binding.sbNumPlayers.setOnSeekBarChangeListener(object :
            SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(
                seek: SeekBar,
                progress: Int, fromUser: Boolean
            ) {
                binding.tvPlayerNumber.text = progress.toString()
            }

            override fun onStartTrackingTouch(seek: SeekBar) {
            }

            override fun onStopTrackingTouch(seek: SeekBar) {
            }
        })
        binding.sbNumRounds.setOnSeekBarChangeListener(object :
            SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(
                seek: SeekBar,
                progress: Int, fromUser: Boolean
            ) {
                binding.tvRoundsNumber.text = progress.toString()
            }

            override fun onStartTrackingTouch(seek: SeekBar) {
            }

            override fun onStopTrackingTouch(seek: SeekBar) {
            }
        })
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
                    bundle.putLong("time",it.data!!.roundTime/1_000_000)
                    bundle.putInt("playersCount",it.data!!.playersCount)
                    val arrayList = ArrayList(it.data!!.playersInRoom)
                    bundle.putStringArrayList("playersInRoom",arrayList)
                    Timber.d("w pokoju sa: ${arrayList}")
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