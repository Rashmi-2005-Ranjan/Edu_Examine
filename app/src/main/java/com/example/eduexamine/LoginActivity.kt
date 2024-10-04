package com.example.eduexamine

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class LoginActivity : AppCompatActivity() {

    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var rememberMeCheckBox: CheckBox
    private lateinit var loginButton: Button
    private lateinit var signUpTextView: TextView
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_main)

        emailEditText = findViewById(R.id.editTextTextEmailAddress2)
        passwordEditText = findViewById(R.id.editTextTextPassword2)
        rememberMeCheckBox = findViewById(R.id.checkBox)
        loginButton = findViewById(R.id.button)
        signUpTextView = findViewById(R.id.textView6)

        sharedPreferences = getSharedPreferences("loginPrefs", Context.MODE_PRIVATE)

        // Load saved email and password if "Remember me" was checked
        loadUserCredentials()

        loginButton.setOnClickListener {
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()

            if (email.isBlank() || password.isBlank()) {
                showMessage("Email and Password cannot be empty")
                return@setOnClickListener
            }

            // Perform login action here (e.g., authenticate user with backend)
            authenticateUser(email, password)
        }

        signUpTextView.setOnClickListener {
            goToSignUp()
        }
    }

    private fun authenticateUser(email: String, password: String) {
        // TODO: Add authentication logic here

        // For now, assuming authentication is successful
        val isAuthenticated = true // Replace with actual authentication result

        if (isAuthenticated) {
            if (rememberMeCheckBox.isChecked) {
                saveUserCredentials(email, password)
            } else {
                clearUserCredentials()
            }

            showMessage("Login Successful")
            startActivity(Intent(this, MainActivity::class.java))
        } else {
            showMessage("Login Failed. Please check your credentials.")
        }
    }

    private fun saveUserCredentials(email: String, password: String) {
        with(sharedPreferences.edit()) {
            putString("email", email)
            putString("password", password)
            putBoolean("rememberMe", true)
            apply()
        }
        showMessage("Credentials saved")
    }

    private fun loadUserCredentials() {
        val rememberMe = sharedPreferences.getBoolean("rememberMe", false)
        if (rememberMe) {
            val savedEmail = sharedPreferences.getString("email", "")
            val savedPassword = sharedPreferences.getString("password", "")
            emailEditText.setText(savedEmail)
            passwordEditText.setText(savedPassword)
            rememberMeCheckBox.isChecked = true
        }
    }

    private fun clearUserCredentials() {
        with(sharedPreferences.edit()) {
            clear()
            apply()
        }
        showMessage("Credentials cleared")
    }

    private fun goToSignUp() {
        val intent = Intent(this, SignUpActivity::class.java)
        startActivity(intent)
        showMessage("Navigating to Sign Up")
    }

    private fun showMessage(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
