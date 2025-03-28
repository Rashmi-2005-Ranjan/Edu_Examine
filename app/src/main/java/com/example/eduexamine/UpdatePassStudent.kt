package com.example.eduexamine

import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth

class UpdatePassStudent : AppCompatActivity() {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_update_pass_student)

        // Handle edge-to-edge insets
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Initialize views
        val oldPasswordInput = findViewById<TextInputEditText>(R.id.passwordupdatestudent) // Update ID
        val newPasswordInput = findViewById<TextInputEditText>(R.id.passwordupdatestudent2) // Update ID
        val updateButton = findViewById<Button>(R.id.button10) // Update ID

        updateButton.setOnClickListener {
            val dialog=AlertDialog.Builder(this)
            dialog.setTitle("Update Password")
            dialog.setMessage("Are You sure To Update Your Password?")
            dialog.setIcon(R.drawable.update)
            dialog.setPositiveButton("YES") { dialog, which ->
                val oldPassword = oldPasswordInput.text.toString()
                val newPassword = newPasswordInput.text.toString()

                if (oldPassword.isNotEmpty() && newPassword.isNotEmpty()) {
                    reauthenticateAndChangePassword(oldPassword, newPassword)
                } else {
                    Toast.makeText(this, "Please fill out all fields", Toast.LENGTH_SHORT).show()
                }
            }
            dialog.setNegativeButton("NO") { dialog, which ->
                Toast.makeText(this, "You Clicked No", Toast.LENGTH_SHORT).show()
            }
            dialog.setNeutralButton("CANCEL") { dialog, which ->
                Toast.makeText(this, "You Clicked Cancel", Toast.LENGTH_SHORT).show()
            }
            val alertDialog = dialog.create()
            alertDialog.setCancelable(false)
            alertDialog.show()
        }
    }

    private fun reauthenticateAndChangePassword(oldPassword: String, newPassword: String) {
        val user = auth.currentUser
        if (user != null) {
            val credential = EmailAuthProvider.getCredential(user.email!!, oldPassword)

            // Re-authenticate the user with the old password
            user.reauthenticate(credential)
                .addOnCompleteListener { reauthTask ->
                    if (reauthTask.isSuccessful) {
                        // Update to the new password if re-authentication is successful
                        user.updatePassword(newPassword)
                            .addOnCompleteListener { updateTask ->
                                if (updateTask.isSuccessful) {
                                    Toast.makeText(this, "Password updated successfully", Toast.LENGTH_SHORT).show()
                                    finish() // Optionally, navigate to another activity
                                } else {
                                    Toast.makeText(this, "Failed to update password", Toast.LENGTH_SHORT).show()
                                }
                            }
                    } else {
                        Toast.makeText(this, "Re-authentication failed. Check your old password", Toast.LENGTH_SHORT).show()
                    }
                }
        } else {
            Toast.makeText(this, "User not authenticated", Toast.LENGTH_SHORT).show()
        }
    }
}
