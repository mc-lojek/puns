package pl.edu.pg.eti.presentation.fragment

import android.content.Context
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
import kotlinx.android.synthetic.main.fragment_login.*
import kotlinx.android.synthetic.main.fragment_login.email_input
import kotlinx.android.synthetic.main.fragment_login.password_input
import kotlinx.android.synthetic.main.fragment_register.*
import pl.edu.pg.eti.R
import pl.edu.pg.eti.databinding.FragmentEntryBinding
import pl.edu.pg.eti.databinding.FragmentLoginBinding
import pl.edu.pg.eti.domain.model.User
import pl.edu.pg.eti.domain.model.UserWithoutNick
import pl.edu.pg.eti.presentation.viewmodel.LoginViewModel
import timber.log.Timber

@AndroidEntryPoint
class LoginFragment : Fragment() {

    private lateinit var binding: FragmentLoginBinding

    private val viewModel: LoginViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_login,
            container,
            false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupListeners()
        setupObserver(view)
    }

    private fun setupListeners() {

        loginHint.text = ""

        binding.LoginFunctionBtn.setOnClickListener {
            viewModel.loginUser(email_input.text.toString(), password_input.text.toString())
        }

        binding.ForgotPassBtn.setOnClickListener {
            findNavController().navigate(R.id.action_LoginFragment_to_forgotPassFragment)
        }
    }

    private fun setupObserver(view: View){
        viewModel.loginLiveData.observe(viewLifecycleOwner){
            if(it.code() == 200) {

                val sharedPref = activity?.getPreferences(Context.MODE_PRIVATE) ?: return@observe
                with (sharedPref.edit()) {
                    val tokens = it.body()
                    if(tokens?.access_token == null || tokens?.refresh_token == null){
                        //SOMETHING IS KAPUTT
                        return@observe
                    }
                    //Timber.d(tokens?.refresh_token)
                    //Timber.d(tokens?.access_token)
                    putString("access_token", tokens?.access_token)
                    putString("refresh_token", tokens?.refresh_token)
                    apply()
                }

                Snackbar.make(view, "Login successful", Snackbar.LENGTH_SHORT).show()
                findNavController().navigate(R.id.action_LoginFragment_to_mainMenuFragment)
            }
            else{
                Timber.d(it.code().toString())
                //TODO: handle errors
                loginHint.text = "user not found"
            }
        }
    }
}