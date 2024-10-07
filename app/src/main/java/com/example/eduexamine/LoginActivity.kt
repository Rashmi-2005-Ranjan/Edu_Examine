package com.example.eduexamine

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
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
    private lateinit var sharedPreferences: SharedPreferences
    private val SESSION_TIMEOUT = 5 * 60 * 1000L // 5 minutes in milliseconds

    override fun onStart() {
        super.onStart()
        // Initialize SharedPreferences
        sharedPreferences = getSharedPreferences("SessionPrefs", MODE_PRIVATE)

        // Check if user is already logged in and if session is valid
        val curUser: FirebaseUser? = auth.currentUser
        val loginTime = sharedPreferences.getLong("LOGIN_TIME", 0)
        val currentTime = System.currentTimeMillis()

        if (curUser != null && (currentTime - loginTime) < SESSION_TIMEOUT) {
            // If session is still valid, redirect to main activity
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        } else if (curUser != null) {
            // If session has expired, log out user and clear session data
            auth.signOut()
            sharedPreferences.edit().clear().apply()
            Toast.makeText(this, "Session expired. Please log in again.", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)

        // Initialization Of Firebase Auth
        auth = FirebaseAuth.getInstance()

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.button.setOnClickListener {
            val email = binding.editTextTextEmailAddress2.text.toString().trim()
            val password = binding.editTextTextPassword2.text.toString().trim()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please Fill All The Details", Toast.LENGTH_SHORT).show()
            } else {
                auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            // Check if the email belongs to an admin or student
                            if (isAdminEmail(email)) {
                                startActivity(Intent(this, adminHome::class.java))
                                Toast.makeText(this, "Sign In Successful - Admin", Toast.LENGTH_SHORT).show()
                            } else {
                                startActivity(Intent(this, StudentHome::class.java))
                                Toast.makeText(this, "Sign In Successful - Student", Toast.LENGTH_SHORT).show()
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

        binding.textView4.setOnClickListener {
            val intent = Intent(this, WelcomeScreen::class.java)
            startActivity(intent)
            Toast.makeText(this, "Navigating You For Update Your Profile Again", Toast.LENGTH_SHORT).show()
        }
        binding.textView6.setOnClickListener {
            val intent = Intent(this, WelcomeScreen::class.java)
            startActivity(intent)
            Toast.makeText(this, "Navigating You For Sign Up", Toast.LENGTH_SHORT).show()
        }
    }

    private fun isAdminEmail(email: String): Boolean {
        // Define admin email criteria (you can change this as needed)
        return email.endsWith("@admin.edu") // Example: admin email ends with '@admin.edu'
    }
}
