package pl.edu.pg.eti.presentation.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import pl.edu.pg.eti.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}