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
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_create_lobby.*
import pl.edu.pg.eti.R
import pl.edu.pg.eti.databinding.FragmentCreateLobbyBinding
import pl.edu.pg.eti.presentation.viewmodel.CreateLobbyViewModel
import timber.log.Timber

@AndroidEntryPoint
class CreateLobbyFragment : Fragment() {

    private lateinit var binding: FragmentCreateLobbyBinding
    private val viewModel: CreateLobbyViewModel by viewModels()
    private lateinit var adapter: ArrayAdapter<CharSequence>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_create_lobby,
            container,
            false
        )
        return binding.root
    }

    private fun setupAdapter() {
        adapter = ArrayAdapter.createFromResource(requireContext(), R.array.timePerRound, android.R.layout.simple_spinner_item)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.TimePerRoundSpinner.adapter = adapter
    }

    private fun setupPlayersBar(){
        binding.numberOfPlayersValueTextView.text = binding.numberOfPlayersSeekBar.progress.toString()
        binding.numberOfPlayersSeekBar.setOnSeekBarChangeListener(object :
            SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(
                seek: SeekBar,
                progress: Int, fromUser: Boolean
            ) {
                binding.numberOfPlayersValueTextView.text = binding.numberOfPlayersSeekBar.progress.toString()
            }

            override fun onStartTrackingTouch(seek: SeekBar) {
            }

            override fun onStopTrackingTouch(seek: SeekBar) {
            }
        })
    }

    private fun setupRoundsBar(){
        binding.numberOfRoundsValueTextView.text = binding.numberOfRoundsSeekBar.progress.toString()
        binding.numberOfRoundsSeekBar.setOnSeekBarChangeListener(object :
            SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(
                seek: SeekBar,
                progress: Int, fromUser: Boolean
            ) {
                binding.numberOfRoundsValueTextView.text = binding.numberOfRoundsSeekBar.progress.toString()
            }

            override fun onStartTrackingTouch(seek: SeekBar) {
            }

            override fun onStopTrackingTouch(seek: SeekBar) {
            }
        })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupAdapter()

        setupPlayersBar()

        setupRoundsBar()

        setupListeners()
    }

    private fun setupListeners() {
        binding.CreateBtn.setOnClickListener {
            Timber.d(TimePerRound_spinner.selectedItem.toString())
            Timber.d(numberOfPlayers_seekBar.progress.toString())
            Timber.d(numberOfRounds_seekBar.progress.toString())
        }
    }
}
