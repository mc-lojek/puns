package pl.edu.pg.eti.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import pl.edu.pg.eti.R
import pl.edu.pg.eti.data.network.Resource
import pl.edu.pg.eti.databinding.FragmentForgotPassBinding
import pl.edu.pg.eti.presentation.viewmodel.ForgotPassViewModel
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
        setupObserver()
    }

    private fun setupListeners() {
        binding.ForgotPassFunctionBtn.setOnClickListener {
            viewModel.loginUser(binding.emailEt.text.toString())
        }
    }

    private fun setupObserver() {
        viewModel.resetPasswordLiveData.observe(viewLifecycleOwner) {
            when(it) {
                is Resource.Error -> {
                    Snackbar.make(binding.root, it.message!!, Snackbar.LENGTH_LONG).show()
                }
                is Resource.Loading -> {

                }
                is Resource.Success -> {
                    Snackbar.make(binding.root, it.data ?: "Email sent", Snackbar.LENGTH_LONG).show()
                }
            }
        }
    }
}