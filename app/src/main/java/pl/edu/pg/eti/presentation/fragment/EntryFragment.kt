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
import pl.edu.pg.eti.R
import pl.edu.pg.eti.databinding.FragmentEntryBinding
import pl.edu.pg.eti.domain.manager.TokenManager
import pl.edu.pg.eti.presentation.viewmodel.EntryViewModel
import pl.edu.pg.eti.presentation.viewmodel.LoginViewModel
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class EntryFragment : Fragment() {

    @Inject
    lateinit var tokenManager: TokenManager


    private lateinit var binding: FragmentEntryBinding

    private val viewModel: EntryViewModel by viewModels()
    private val viewModelLogin: LoginViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_entry,
            container,
            false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tryAutoLogin()
        setupListeners()
        setupObserver(view)
    }

    private fun setupListeners() {
        binding.loginBtn.setOnClickListener {
            findNavController().navigate(R.id.action_entryFragment_to_loginFragment)
        }
        binding.registerBtn.setOnClickListener {
            findNavController().navigate(R.id.action_entryFragment_to_registerFragment)
        }
        binding.guestBtn.setOnClickListener{
            viewModel.loginGuest();


            //val sharedPref = activity?.getPreferences(Context.MODE_PRIVATE) ?: return@setOnClickListener
            //Timber.d(sharedPref.getString("access_token", "empty"))
            //Timber.d(sharedPref.getString("refresh_token", "empty"))
        }
    }

    private fun tryAutoLogin(){
        //check if keys are set
        val sharedPref = activity?.getPreferences(Context.MODE_PRIVATE) ?: return
        val accessToken = sharedPref.getString("access_token", "empty")
        val refreshToken = sharedPref.getString("refresh_token", "empty")
        if(accessToken == "empty" || accessToken == null || refreshToken == "empty" || refreshToken == null)
            return

        //get email and pass from them
        //try to log in
        //viewModelLogin.loginUser(email_input.text.toString(), password_input.text.toString())


        tokenManager.initialize(accessToken, refreshToken)
        Snackbar.make(requireView(), "Login successful", Snackbar.LENGTH_SHORT).show()
        findNavController().navigate(R.id.action_entryFragment_to_mainMenuFragment)
    }

    private fun setupObserver(view: View){
        viewModel.guestLiveData.observe(viewLifecycleOwner){
            if(it.code() == 200){
                val sharedPref = activity?.getPreferences(Context.MODE_PRIVATE) ?: return@observe
                with (sharedPref.edit()){
                    val tokens = it.body()
                    if(tokens?.guestData == null){
                        Timber.d("data is broken")
                        return@observe
                    }

                    //Timber.d(tokens?.guestData.username)
                    //Timber.d(tokens?.guestData.email)
                    putString("access_token", tokens?.guestData.accessToken)
                    putString("refresh_token", tokens?.guestData.refreshToken)
                    apply()

                    tokenManager.initialize(tokens.guestData.accessToken, tokens.guestData.refreshToken)
                }

                Snackbar.make(view, "Logged as guest", Snackbar.LENGTH_SHORT).show()
                findNavController().navigate(R.id.action_entryFragment_to_mainMenuFragment)
            }
            else{
                Timber.d(it.code().toString())
                //TODO: handle error
            }
        }
    }
}