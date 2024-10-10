package com.example.eduexamine

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.eduexamine.databinding.ActivityWelcomeScreenBinding
import com.example.onlineexaminationapp.FacultySignUp

class WelcomeScreen : AppCompatActivity() {
    private val binding: ActivityWelcomeScreenBinding by lazy {
        ActivityWelcomeScreenBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge() // Enables edge-to-edge display
        setContentView(binding.root)

        // Adjusts the padding to accommodate system bars
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Button click listener for navigating to the Sign-Up Activity
        binding.button.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
//            finish() // Close the WelcomeScreen activity
        }

        // Button click listener for navigating to the Faculty Sign-Up Activity
//        binding.btn2.setOnClickListener {
//            val intent = Intent(this, FacultySignUp::class.java)
//            startActivity(intent)
////            finish() // Close the WelcomeScreen activity
//        }

        // TextView click listener for navigating to the Login Activity
        binding.txt4.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
//            finish() // Close the WelcomeScreen activity
        }
    }
}
