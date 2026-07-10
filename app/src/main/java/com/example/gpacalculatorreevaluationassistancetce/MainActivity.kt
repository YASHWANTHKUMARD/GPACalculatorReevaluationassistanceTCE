package com.example.gpacalculatorreevaluationassistancetce

import android.os.Bundle
import android.view.animation.AnticipateInterpolator
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.gpacalculatorreevaluationassistancetce.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)

        // Add custom exit animation for the splash screen
        splashScreen.setOnExitAnimationListener { splashScreenView ->
            val slideUp = splashScreenView.iconView.animate()
                .translationY(-splashScreenView.iconView.height.toFloat())
                .setInterpolator(AnticipateInterpolator())
                .setDuration(1000L)
            
            slideUp.withEndAction { splashScreenView.remove() }
            slideUp.start()
            
            // Also animate the background alpha
            splashScreenView.view.animate()
                .alpha(0f)
                .setDuration(1000L)
                .start()
        }

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Ensure the toolbar is treated as the primary action bar
        setSupportActionBar(binding.toolbar)

        // Find the Navigation Host
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as? NavHostFragment
        
        if (navHostFragment != null) {
            val navController = navHostFragment.navController

            // Set up titles for the three main screens
            val appBarConfiguration = AppBarConfiguration(
                setOf(R.id.sgpaFragment, R.id.cgpaFragment, R.id.goalFragment, R.id.aboutFragment)
            )

            setupActionBarWithNavController(navController, appBarConfiguration)
            binding.bottomNavigation.setupWithNavController(navController)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as? NavHostFragment
        return navHostFragment?.navController?.navigateUp() ?: super.onSupportNavigateUp()
    }
}