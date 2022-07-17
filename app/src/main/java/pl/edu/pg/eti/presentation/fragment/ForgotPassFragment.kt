package pl.edu.pg.eti.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_forgot_pass.*
import pl.edu.pg.eti.R
import pl.edu.pg.eti.databinding.FragmentEntryBinding
import pl.edu.pg.eti.databinding.FragmentForgotPassBinding
import pl.edu.pg.eti.databinding.FragmentLoginBinding
import pl.edu.pg.eti.presentation.viewmodel.ForgotPassViewModel
import pl.edu.pg.eti.presentation.viewmodel.LoginViewModel
import timber.log.Timber

@AndroidEntryPoint
class ForgotPassFragment : Fragment() {

    private lateinit var binding: FragmentForgotPassBinding

    private val viewModel: ForgotPassViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_forgot_pass,
            container,
            false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupListeners()
    }

    private fun setupListeners() {
        binding.ForgotPassFunctionBtn.setOnClickListener {
            Timber.d(email_input.text.toString())
        }
    }
}