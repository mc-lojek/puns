package pl.edu.pg.eti.presentation.activity

import android.os.Bundle
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.fragment.NavHostFragment
import dagger.hilt.android.AndroidEntryPoint
import pl.edu.pg.eti.databinding.ActivityMainBinding
import timber.log.Timber


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    private lateinit var currentDestination: NavDestination

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment = supportFragmentManager.findFragmentById(binding.navHostFragment.id) as NavHostFragment
        navController = navHostFragment.navController

        //navController = findNavController(binding.navHostFragment.id)
        navController.addOnDestinationChangedListener { controller, destination, arguments ->
            currentDestination = destination
            Timber.d(currentDestination.label.toString())
        }
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    override fun onBackPressed() {
        when (currentDestination.label){
            "LobbyFragment","GuessingFragment","DrawingFragment","ScoreboardFragment","EntryFragment"->{
                return
            }
        }
        super.onBackPressed()
    }
}