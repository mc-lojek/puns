package pl.edu.pg.eti.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_main_menu.*
import pl.edu.pg.eti.R
import pl.edu.pg.eti.databinding.FragmentEntryBinding
import pl.edu.pg.eti.databinding.FragmentLoginBinding
import pl.edu.pg.eti.databinding.FragmentMainMenuBinding
import pl.edu.pg.eti.presentation.viewmodel.LoginViewModel
import pl.edu.pg.eti.presentation.viewmodel.MainMenuViewModel
import timber.log.Timber

@AndroidEntryPoint
class MainMenuFragment : Fragment() {

    private lateinit var binding: FragmentMainMenuBinding

    private val viewModel: MainMenuViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_main_menu,
            container,
            false
        )

        //Nie jestem pewny gdzie to wywołać
        //raczej nie opłaca się robić oddzielnego fragmentu, więc można pobierać dane logowania i ustawiać visible tego
        binding.StatisticsBtn.isVisible = false;


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupListeners()
    }

    private fun setupListeners() {
        binding.QuickGameBtn.setOnClickListener {
        }

        binding.CreateLobbyBtn.setOnClickListener {
            findNavController().navigate(R.id.action_mainMenuFragment_to_createLobbyFragment)
        }

        binding.OptionsBtn.setOnClickListener {
        }

        binding.CreatorsBtn.setOnClickListener {
        }

        binding.StatisticsBtn.setOnClickListener {
        }
    }
}