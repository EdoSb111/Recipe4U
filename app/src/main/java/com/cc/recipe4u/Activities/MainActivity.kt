package com.cc.recipe4u.Activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.widget.Toolbar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.edit
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.cc.recipe4u.R
import com.cc.recipe4u.ViewModels.AuthViewModel
import com.cc.recipe4u.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(){

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    private val authViewModel: AuthViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        checkUserStatus()
        initNavigation()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.action_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_sign_out -> {
                // Handle sign out action here
                authViewModel.signOut()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun checkUserStatus() {
        authViewModel.isUserSignedIn.observe(this) { isSignedIn ->
            if (!isSignedIn) {
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

    private fun initNavigation() {
        val toolbar = findViewById<View>(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)
        // Set up Navigation component
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController

        // Set up ActionBar with NavController
        setupActionBarWithNavController(navController)

        // Set up Bottom Navigation View
        binding.bottomNavigationView.setupWithNavController(navController)

        // Handle item selection manually
        binding.bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_home,
                R.id.navigation_add,
                R.id.navigation_favorites,
                R.id.navigation_profile -> {
                    // Check if the selected destination is different from the current one
                    if (binding.bottomNavigationView.selectedItemId != item.itemId) {
                        navController.popBackStack(
                            R.id.navigation_profile,
                            item.itemId == R.id.navigation_profile
                        )
                        navController.navigate(item.itemId)
                    }
                    true
                }
                else -> false
            }
        }
    }
}
