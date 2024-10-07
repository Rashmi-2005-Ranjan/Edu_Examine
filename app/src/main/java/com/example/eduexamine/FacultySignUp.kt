package com.example.onlineexaminationapp

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Patterns
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.eduexamine.LoginActivity
import com.example.eduexamine.R
import com.google.android.material.textfield.TextInputLayout

class FacultySignUp : AppCompatActivity() {

    private lateinit var fullNameInput: TextInputLayout
    private lateinit var employeeIdInput: TextInputLayout
    private lateinit var emailInput: TextInputLayout
    private lateinit var passwordInput: TextInputLayout
    private lateinit var signUpButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_faculty_sign_up)

        // Initialize views
        fullNameInput = findViewById(R.id.material1)
        employeeIdInput = findViewById(R.id.material)
        emailInput = findViewById(R.id.materialEmail)
        passwordInput = findViewById(R.id.material21)
        signUpButton = findViewById(R.id.button3)

        // Set onClickListener for sign-up button
        signUpButton.setOnClickListener {
            if (validateInputs()) {
                Toast.makeText(this, "Congrats! You signed up successfully!", Toast.LENGTH_LONG).show()
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
        // Handle Edge-to-Edge insets
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun validateInputs(): Boolean {
        val fullName = fullNameInput.editText?.text.toString().trim()
        val employeeId = employeeIdInput.editText?.text.toString().trim()
        val email = emailInput.editText?.text.toString().trim()
        val password = passwordInput.editText?.text.toString().trim()

        if (TextUtils.isEmpty(fullName)) {
            fullNameInput.error = "Full Name is required"
            return false
        } else {
            fullNameInput.error = null
        }

        if (TextUtils.isEmpty(employeeId)) {
            employeeIdInput.error = "Employee ID is required"
            return false
        } else {
            employeeIdInput.error = null
        }

        if (TextUtils.isEmpty(email) || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailInput.error = "Invalid Email Address"
            return false
        } else {
            emailInput.error = null
        }

        if (TextUtils.isEmpty(password)) {
            passwordInput.error = "Password is required"
            return false
        } else {
            passwordInput.error = null
        }

        return true
    }


}


