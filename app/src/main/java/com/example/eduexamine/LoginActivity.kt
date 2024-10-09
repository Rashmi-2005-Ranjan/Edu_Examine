package com.example.eduexamine

import android.content.Intent
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
                    Toast.makeText(this,"Redirecting You To Dashboard",Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this, adminHome::class.java))
                    finish()
                } else {
                    Toast.makeText(this,"Redirecting You To Dashboard",Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this, StudentHome::class.java))
                    finish()
                }
                finish()
            }
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)

        // Initialization of Firebase Auth
        auth = FirebaseAuth.getInstance()

        // Apply window insets for immersive mode
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Set click listener for the login button
        binding.button.setOnClickListener {
            val email = binding.editTextTextEmailAddress2.text.toString().trim()
            val password = binding.editTextTextPassword2.text.toString().trim()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please fill all the details", Toast.LENGTH_SHORT).show()
            } else {
                // Sign in with email and password
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

    // Check if the email belongs to an admin
    private fun isAdminEmail(email: String): Boolean {
        // Define admin email criteria (you can change this as needed)
        return email.endsWith("@admin.edu.in") // Example: admin email ends with '@admin.edu'
    }
}
