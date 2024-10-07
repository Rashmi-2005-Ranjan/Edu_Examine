package com.example.eduexamine

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Patterns
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputLayout

class SignUpActivity : AppCompatActivity() {

    private lateinit var fullNameInput: TextInputLayout
    private lateinit var usernameInput: TextInputLayout
    private lateinit var emailInput: TextInputLayout
    private lateinit var passwordInput: TextInputLayout
    private lateinit var signUpButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        // Initialize views
        fullNameInput = findViewById(R.id.material1)
        usernameInput = findViewById(R.id.material)
        emailInput = findViewById(R.id.materialEmail)
        passwordInput = findViewById(R.id.material21)
        signUpButton = findViewById(R.id.button3)

        // Set onClickListeners
        signUpButton.setOnClickListener {
            if (validateInputs()) {
                val intent = Intent(this, LoginActivity::class.java)
                Toast.makeText(this, "Congrats! You signed up successfully!", Toast.LENGTH_SHORT).show()
                startActivity(intent)
                finish()
            }
        }
    }

    // Override the onBackPressed method to finish the current activity and go back
    override fun onBackPressed() {
        super.onBackPressed()
        finish() // This will close the current activity and navigate back to the previous one.
    }

    private fun validateInputs(): Boolean {
        val fullName = fullNameInput.editText?.text.toString().trim()
        val username = usernameInput.editText?.text.toString().trim()
        val email = emailInput.editText?.text.toString().trim()
        val password = passwordInput.editText?.text.toString().trim()

        if (TextUtils.isEmpty(fullName)) {
            fullNameInput.error = "Full Name is required"
            showMessage("Please enter your Full Name")
            return false
        } else {
            fullNameInput.error = null
        }

        if (TextUtils.isEmpty(username)) {
            usernameInput.error = "Username is required"
            showMessage("Please enter your Username")
            return false
        } else {
            usernameInput.error = null
        }

        if (TextUtils.isEmpty(email) || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailInput.error = "Invalid Email Address"
            showMessage("Please enter a valid Email Address")
            return false
        } else {
            emailInput.error = null
        }

        if (TextUtils.isEmpty(password)) {
            passwordInput.error = "Password is required"
            showMessage("Please enter your Password")
            return false
        } else {
            passwordInput.error = null
        }

        return true
    }

    private fun showMessage(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
