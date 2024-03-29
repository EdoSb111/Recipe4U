package com.cc.recipe4u.Activities

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.cc.recipe4u.Objects.GlobalVariables
import com.cc.recipe4u.R
import com.cc.recipe4u.ViewModels.AuthViewModel
import com.cc.recipe4u.ViewModels.UserViewModel

class LoginActivity : AppCompatActivity() {

    private val authViewModel: AuthViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // Initialize UI components
        val usernameEditText: EditText = findViewById(R.id.et_username)
        val passwordEditText: EditText = findViewById(R.id.et_password)
        val loginButton: Button = findViewById(R.id.btn_login)
        val signUpTextView: TextView = findViewById(R.id.tv_signup)

        // Set onClickListener for the login button
        loginButton.setOnClickListener {
            val email = usernameEditText.text.toString().trim()
            val password = passwordEditText.text.toString().trim()

            if (email.isNotEmpty() && password.isNotEmpty()) {
                // Call the signIn method in AuthViewModel
                authViewModel.signIn(email, password, this)
            }
        }

        // Set onClickListener for the sign-up TextView
        signUpTextView.setOnClickListener {
            // Navigate to the SignUpActivity or any other activity for sign-up
            startActivity(Intent(this, SignupActivity::class.java))
            finish()
        }

        // Observe the isUserSignedIn LiveData to determine the authentication state
        authViewModel.isUserSignedIn.observe(this) { isSignedIn ->
            if (isSignedIn) {
                val userid = authViewModel.currentUser.value!!.uid
                val userViewModel = UserViewModel(userid)
                userViewModel.userLiveData.observe(this) { userdata ->
                    if (userdata != null) {
                        GlobalVariables.currentUser = userdata
                        // User is signed in, navigate to the MainActivity
                        startActivity(Intent(this, MainActivity::class.java))
                        finish()
                    }
                }
            }
        }
    }
}
