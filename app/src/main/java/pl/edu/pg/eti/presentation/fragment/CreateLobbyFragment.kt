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


// u Ciebie widze ze akurat string.xml stosujesz wiec jest spoko, natomiast musimy ujednolicic nazewnictwo plikow xml i id-ków żeby to było spójne
// pozniej na discordzie wysle wam jakies zrodlo na ktorym bedziemy bazowac



@AndroidEntryPoint
class CreateLobbyFragment : Fragment() {

    private lateinit var binding: FragmentCreateLobbyBinding
    // to do wyjebania, kazdy fragment ma w sobie pole context
    // wiec wszedzie gdzie masz mContext to zmein po prostu na context
    private lateinit var mContext: Context

    //adapter akurat warto trzymac tutaj bo pozniej w razie potrzeby mozna do niego dodac np listener
    //private lateinit var adapter: ArrayAdapter<CharSequence>

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

    // do wyjebania
    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }

//    private fun setupAdapter() {
//        adapter = ArrayAdapter.createFromResource(requireContext(), R.array.timePerRound, android.R.layout.simple_spinner_item)
//        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
//        binding.TimePerRoundSpinner.adapter = adapter
//    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //tutaj uzywasz synthetic. to ogolnie dziala, ale nie jest juz wspierane i zalecane przez google, bo nie sprawdza typowania itp
        // zeby sie dostac do elementow ui uzywamy binding
        // czyli w 64 linijce bedzie binding.TimePreRound_spinner.adapter = adapter
        // ale ogolnie ta konstrukcje bym przebudowal zeby byla bardziej czytelna i wydzielil do osobnej funkcji, przyklad powyzej
        //i tu tylko wywoluje funkcje:
        //setupAdapter()

        val spinner: Spinner = TimePerRound_spinner;
        ArrayAdapter.createFromResource(
            mContext,
            R.array.timePerRound,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner.adapter = adapter
        }

        //tutaj to samo - synthetic na binding zmien i wydziel to do funkcji osobnych
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
