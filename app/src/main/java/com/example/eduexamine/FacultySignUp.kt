package com.example.onlineexaminationapp

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.eduexamine.LoginActivity
import com.example.eduexamine.R
import com.example.eduexamine.databinding.ActivityFacultySignUpBinding
import com.google.firebase.auth.FirebaseAuth

class FacultySignUp : AppCompatActivity() {

    private val binding: ActivityFacultySignUpBinding by lazy {
        ActivityFacultySignUpBinding.inflate(layoutInflater)
    }

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)

        // Initialize The Fire Base Variable
        auth = FirebaseAuth.getInstance()

        binding.button3.setOnClickListener {
            // Get Text From Edit Text Field
            val fullName = binding.editFullName.text.toString()
            val email = binding.email.text.toString()
            val password = binding.password.text.toString()
            val cnfPassword = binding.cnfpassword.text.toString()

            // Check if any field is blank
            if (fullName.isEmpty() || email.isEmpty() || password.isEmpty() || cnfPassword.isEmpty()) {
                Toast.makeText(this, "Please Fill All The Details", Toast.LENGTH_SHORT).show()
            } else if (password != cnfPassword) {
                Toast.makeText(
                    this,
                    "Password and Confirm Password Did Not Match",
                    Toast.LENGTH_SHORT
                ).show()
            } else if (!email.startsWith("EMP@EDU")) {
                Toast.makeText(this, "Enter Your Service Email", Toast.LENGTH_SHORT).show()
            } else {
                auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(this, "Registration Successful", Toast.LENGTH_SHORT)
                                .show()
                            startActivity(Intent(this, LoginActivity::class.java))
                            finish()
                        } else {
                            Toast.makeText(
                                this,
                                "Registration Failed ${task.exception?.message}",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
            }
        }

        // Handle Edge-to-Edge insets
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}
