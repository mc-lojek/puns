package pl.edu.pg.eti.presentation.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.SeekBar
import android.widget.Spinner
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_create_lobby.*
import kotlinx.android.synthetic.main.fragment_login.*
import pl.edu.pg.eti.R
import pl.edu.pg.eti.databinding.FragmentCreateLobbyBinding
import pl.edu.pg.eti.presentation.viewmodel.CreateLobbyViewModel
import timber.log.Timber

@AndroidEntryPoint
class CreateLobbyFragment : Fragment() {

    private lateinit var binding: FragmentCreateLobbyBinding
    private lateinit var mContext: Context

    private val viewModel: CreateLobbyViewModel by viewModels()

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

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val spinner: Spinner = TimePerRound_spinner;
        ArrayAdapter.createFromResource(
            mContext,
            R.array.timePerRound,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner.adapter = adapter
        }

        numberOfPlayersValue_textView.setText(numberOfPlayers_seekBar.getProgress().toString());
        val seek1 = numberOfPlayers_seekBar
        seek1?.setOnSeekBarChangeListener(object :
            SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(
                seek1: SeekBar,
                progress: Int, fromUser: Boolean
            ) {
                numberOfPlayersValue_textView.setText(numberOfPlayers_seekBar.getProgress().toString());
            }

            override fun onStartTrackingTouch(seek1: SeekBar) {
            }

            override fun onStopTrackingTouch(seek1: SeekBar) {
            }
        })

        numberOfRoundsValue_textView.setText(numberOfRounds_seekBar.getProgress().toString());
        val seek2 = numberOfRounds_seekBar
        seek2?.setOnSeekBarChangeListener(object :
            SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(
                seek2: SeekBar,
                progress: Int, fromUser: Boolean
            ) {
                numberOfRoundsValue_textView.setText(numberOfRounds_seekBar.getProgress().toString());
            }

            override fun onStartTrackingTouch(seek2: SeekBar) {
            }

            override fun onStopTrackingTouch(seek2: SeekBar) {
            }
        })

        setupListeners()
    }



    private fun setupListeners() {
        binding.CreateBtn.setOnClickListener {
            Timber.d(TimePerRound_spinner.getSelectedItem().toString())
            Timber.d(numberOfPlayers_seekBar.getProgress().toString())
            Timber.d(numberOfRounds_seekBar.getProgress().toString())
        }
    }
}
