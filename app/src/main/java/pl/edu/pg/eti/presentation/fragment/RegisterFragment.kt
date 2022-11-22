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
import pl.edu.pg.eti.databinding.FragmentRegisterBinding
import pl.edu.pg.eti.presentation.viewmodel.RegisterViewModel
import timber.log.Timber
import kotlinx.android.synthetic.main.fragment_register.*;


@AndroidEntryPoint
class RegisterFragment : Fragment() {

    private lateinit var binding: FragmentRegisterBinding

    private val viewModel: RegisterViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_register,
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
        binding.RegisterFunctionBtn.setOnClickListener {

            registerHint.text = ""

            val nick = nick_input.text.toString()
            val email = email_input.text.toString()
            val password = password_input.text.toString()
            val confirmPassword = confirm_password_input.text.toString()

            val patternIncludesComma = "[,]".toRegex()
            val patternAtLeast3Letters = "...".toRegex()
            val patternAtLeast1BigLetter = "[A-Z]".toRegex()
            val patternAtLeast1SmallLetter = "[a-z]".toRegex()
            val patternAtLeast1Number = "[0-9]".toRegex()
            val patternEmail = ".+[@].+[.].+".toRegex()
            val patternIncludesSpace = "[ ]".toRegex()

            //check nick
            if (!patternAtLeast3Letters.containsMatchIn (nick)) {
                registerHint.text = "nick too short"
                return@setOnClickListener
            }

            if(patternIncludesSpace.containsMatchIn(nick)){
                registerHint.text = "nick can't contain space"
                return@setOnClickListener
            }

            //check email
            if (!patternEmail.containsMatchIn (email)) {
                registerHint.text = "incorrect email"
                return@setOnClickListener
            }

            if(patternIncludesSpace.containsMatchIn(email)){
                registerHint.text = "email can't contain space"
                return@setOnClickListener
            }


            //check password
            if(patternIncludesComma.containsMatchIn(password)) {
                registerHint.text = "password can't contain ','"
                return@setOnClickListener
            }
            if(patternIncludesSpace.containsMatchIn(password)){
                registerHint.text = "password can't contain space"
                return@setOnClickListener
            }
            if (!patternAtLeast3Letters.containsMatchIn (password)) {
                registerHint.text = "password too short"
                return@setOnClickListener
            }
            if (!patternAtLeast1BigLetter.containsMatchIn (password)) {
                registerHint.text = "password need at least one big letter"
                return@setOnClickListener
            }
            if (!patternAtLeast1SmallLetter.containsMatchIn (password)) {
                registerHint.text = "password need at least one small letter"
                return@setOnClickListener
            }
            if (!patternAtLeast1Number.containsMatchIn (password)) {
                registerHint.text = "password need at least one number"
                return@setOnClickListener
            }

            //check confirm password

            if(!password.equals(confirmPassword)){
                registerHint.text = "confirmed password does not match"
                return@setOnClickListener
            }


            viewModel.registerUser(nick, password, email)
        }
    }

    private fun setupObserver(view: View){
        viewModel.registerLiveData.observe(viewLifecycleOwner){
            if(it.code() == 200) {
                //Timber.d(it.body().toString())
                Snackbar.make(view, "Register successful", Snackbar.LENGTH_SHORT).show()
                findNavController().navigateUp()
            }
            else{
                Timber.d(it.code().toString())
                //TODO: handle errors
            }
        }
    }
}