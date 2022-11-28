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
import pl.edu.pg.eti.data.network.Resource
import pl.edu.pg.eti.databinding.FragmentMainMenuBinding
import pl.edu.pg.eti.domain.manager.TokenManager
import pl.edu.pg.eti.presentation.viewmodel.MainMenuViewModel
import timber.log.Timber
import javax.inject.Inject
import kotlin.random.Random

@AndroidEntryPoint
class MainMenuFragment : Fragment() {
    private val viewModel: MainMenuViewModel by viewModels()
    private lateinit var binding: FragmentMainMenuBinding

    @Inject
    lateinit var tokenManager: TokenManager

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
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupListeners()
        setupObservers()
    }

    private fun setupListeners() {
        binding.fastGameBtn.setOnClickListener {
            viewModel.joinRoom(tokenManager.userId!!,tokenManager.username!!)
        }
        binding.joinBtn.setOnClickListener {
            val hash = binding.hashEt.text.toString().uppercase()
            binding.hashEt.setText("")
            //walidacja czy wpisany hash ma 6 znaków 0-9, A-Z

            viewModel.joinRoom(tokenManager.userId!!,tokenManager.username!!, hash)

        }

        binding.createBtn.setOnClickListener {
            Timber.d("guest: ${tokenManager.isGuest!!}")
            if(tokenManager.isGuest!!){
                val snackbar = Snackbar.make(
                    requireView(), "Only registered players can create private rooms",
                    Snackbar.LENGTH_LONG
                ).setAction("Action", null)
                snackbar.show()
            }
            else{
                val bundle = Bundle()
                bundle.putLong("id", tokenManager.userId!!)
                bundle.putString("nickname", tokenManager.username!!)
                findNavController().navigate(R.id.action_mainMenuFragment_to_setupGameFragment,bundle)
            }
        }
    }

    private fun setupObservers() {
        viewModel.roomJoinLiveData.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Success -> {
                    val bundle = Bundle()
                    bundle.putString("queue_name", it.data!!.queueName)
                    bundle.putString("exchange_name", it.data!!.exchangeName)
                    bundle.putString("hash",it.data!!.hash)
                    bundle.putLong("time",it.data!!.roundTime/1_000_000)
                    bundle.putInt("playersCount",it.data!!.playersCount)
                    bundle.putInt("maxPlayers",it.data!!.maxPlayers)
                    val arrayList = ArrayList(it.data!!.playersInRoom)
                    bundle.putStringArrayList("playersInRoom",arrayList)
                    findNavController().navigate(R.id.action_mainMenuFragment_to_game_nav_graph, bundle)


                    viewModel.clearLiveData()
                }
                is Resource.Error -> {
                    Snackbar.make(binding.root, it.message.toString(), Snackbar.LENGTH_SHORT)
                }
                is Resource.Loading -> {}
            }
        }


    }
}