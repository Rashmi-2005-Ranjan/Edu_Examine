package com.example.eduexamine

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.eduexamine.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class LoginActivity : AppCompatActivity() {

    private val binding: ActivityLoginBinding by lazy {
        ActivityLoginBinding.inflate(layoutInflater)
    }

    private lateinit var auth: FirebaseAuth
    private lateinit var sharedPreferences: android.content.SharedPreferences


    override fun onStart() {
        super.onStart()

        // Check if user is already logged in
        val curUser: FirebaseUser? = auth.currentUser

        if (curUser != null) {
            // Check if the current user's email is that of an admin or a student
            val email = curUser.email
            if (email != null) {
                // Redirect to admin or student home based on email criteria
                if (isAdminEmail(email)) {
                    Toast.makeText(this, "Redirecting You To Dashboard", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this, adminHome::class.java))
                    finish()
                } else {
                    Toast.makeText(this, "Redirecting You To Dashboard", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this, StudentHome::class.java))
                    finish()
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        // Initialization of Firebase Auth and SharedPreferences
        auth = FirebaseAuth.getInstance()
        sharedPreferences = getSharedPreferences("loginPrefs", Context.MODE_PRIVATE)

        // Load saved login data, if available
        loadLoginData()

        // Set up window insets for immersive mode
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Set click listener for the login button
        binding.button.setOnClickListener {
            val email = binding.editTextTextEmailAddress2.text.toString().trim()
            val password = binding.editTextTextPassword2.text.toString().trim()
            val rememberMe = binding.checkBox.isChecked

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please fill all the details", Toast.LENGTH_SHORT).show()
            } else {
                // Sign in with email and password
                auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            // Check if the email belongs to an admin or student
                            if (isAdminEmail(email)) {
                                Toast.makeText(this, "Sign In Successful - Admin", Toast.LENGTH_SHORT).show()
                                startActivity(Intent(this, adminHome::class.java))
                            } else {
                                Toast.makeText(this, "Sign In Successful - Student", Toast.LENGTH_SHORT).show()
                                startActivity(Intent(this, StudentHome::class.java))
                            }

                            // Save login data if "Remember Me" is checked
                            if (rememberMe) {
                                saveLoginData(email, password)
                            } else {
                                clearLoginData() // Clear data if "Remember Me" is not checked
                            }
                            finish()
                        } else {
                            Toast.makeText(
                                this,
                                "Sign In Failed: ${task.exception?.message}",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
            }
        }

        // Set click listener for the 'Forgot Password' or 'Update Profile' TextView
        binding.textView4.setOnClickListener {
            val intent = Intent(this, WelcomeScreen::class.java)
            startActivity(intent)
            Toast.makeText(this, "Navigating you to update your profile", Toast.LENGTH_SHORT).show()
        }

        // Set click listener for the 'Sign Up' TextView
        binding.textView6.setOnClickListener {
            val intent = Intent(this, WelcomeScreen::class.java)
            startActivity(intent)
            Toast.makeText(this, "Navigating you to sign up", Toast.LENGTH_SHORT).show()
        }
    }

    // Function to check if the email belongs to an admin
    private fun isAdminEmail(email: String?): Boolean {
        // Null check to ensure email is not null
        if (email.isNullOrBlank()) return false

        // Define admin email criteria (case insensitive)
        return email.lowercase().startsWith("empedu") && email.lowercase().endsWith("@admin.edu.in")
    }

    // Function to save login data using SharedPreferences
    private fun saveLoginData(email: String, password: String) {
        val editor = sharedPreferences.edit()
        editor.putString("email", email)
        editor.putString("password", password)
        editor.putBoolean("rememberMe", true)
        editor.apply()
    }

    // Function to load saved login data from SharedPreferences
    private fun loadLoginData() {
        val email = sharedPreferences.getString("email", "")
        val password = sharedPreferences.getString("password", "")
        val rememberMe = sharedPreferences.getBoolean("rememberMe", false)

        if (rememberMe) {
            binding.editTextTextEmailAddress2.setText(email)
            binding.editTextTextPassword2.setText(password)
            binding.checkBox.isChecked = true
        }
    }

    // Function to clear saved login data from SharedPreferences
    private fun clearLoginData() {
        val editor = sharedPreferences.edit()
        editor.clear()
        editor.apply()
    }
}
