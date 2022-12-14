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
import kotlinx.android.synthetic.main.fragment_register.*
import pl.edu.pg.eti.R
import pl.edu.pg.eti.data.network.Resource
import pl.edu.pg.eti.databinding.FragmentRegisterBinding
import pl.edu.pg.eti.presentation.viewmodel.RegisterViewModel


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
        binding.registerBtn.setOnClickListener {

            register_hint.text = ""

            val nick = nickname_et.text.toString()
            val email = email_et.text.toString()
            val password = password_et.text.toString()

            val patternIncludesComma = "[,]".toRegex()
            val patternAtLeast3Letters = "...".toRegex()
            val patternAtLeast1BigLetter = "[A-Z]".toRegex()
            val patternAtLeast1SmallLetter = "[a-z]".toRegex()
            val patternAtLeast1Number = "[0-9]".toRegex()
            val patternEmail = ".+[@].+[.].+".toRegex()

            //check nick
            if (!patternAtLeast3Letters.containsMatchIn (nick)) {
                register_hint.text = "nick too short"
                return@setOnClickListener
            }
            if(patternIncludesComma.containsMatchIn(nick)) {
                register_hint.text = "nick cannot contain ','"
                return@setOnClickListener
            }

            //check email
            if (!patternEmail.containsMatchIn (email)) {
                register_hint.text = "incorrect email"
                return@setOnClickListener
            }
            if(patternIncludesComma.containsMatchIn(email)) {
                register_hint.text = "email cannot contain ','"
                return@setOnClickListener
            }

            //check password
            if (!patternAtLeast3Letters.containsMatchIn (password)) {
                register_hint.text = "password too short"
                return@setOnClickListener
            }
            if (!patternAtLeast1BigLetter.containsMatchIn (password)) {
                register_hint.text = "password need at least one big letter"
                return@setOnClickListener
            }
            if (!patternAtLeast1SmallLetter.containsMatchIn (password)) {
                register_hint.text = "password need at least one small letter"
                return@setOnClickListener
            }
            if (!patternAtLeast1Number.containsMatchIn (password)) {
                register_hint.text = "password need at least one number"
                return@setOnClickListener
            }
            if(patternIncludesComma.containsMatchIn(password)) {
                register_hint.text = "password cannot contain ','"
                return@setOnClickListener
            }

            viewModel.registerUser(nick, password, email)
        }
    }

    private fun setupObserver(view: View){
        viewModel.registerLiveData.observe(viewLifecycleOwner){
            when (it){
                is Resource.Success -> {
                    Snackbar.make(view, "Register successful", Snackbar.LENGTH_SHORT).show()
                    findNavController().navigateUp()
                }
                is Resource.Error ->{
                    Snackbar.make(view, it.message.toString(), Snackbar.LENGTH_SHORT).show()
                }
                is Resource.Loading -> {}
            }
        }
    }
}