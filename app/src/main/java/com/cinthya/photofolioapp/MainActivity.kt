package com.cinthya.photofolioapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI.setupWithNavController
import com.cinthya.photofolioapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()

        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        navController = Navigation.findNavController(this, R.id.fragment_container_host)
        setupWithNavController(binding.bottomNavigation, navController)

        supportActionBar?.hide()

        navController.addOnDestinationChangedListener{_, destination,_ ->
            when (destination.id) {
                R.id.folderFragment -> {
                    visible()
                }
                R.id.allPhotoFragment -> {
                    visible()
                }
                R.id.favoriteFragment -> {
                    visible()
                }
                else -> {
                    invisible()
                }
            }
        }
    }

    private fun visible() {
        binding.bottomNavigation.visibility = View.VISIBLE
    }

    private fun invisible() {
        binding.bottomNavigation.visibility = View.GONE
    }
}