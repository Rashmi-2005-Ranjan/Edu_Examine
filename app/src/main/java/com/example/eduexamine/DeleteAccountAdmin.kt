package com.example.eduexamine

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class DeleteAccountAdmin : AppCompatActivity() {

    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var deleteButton: Button
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_delete_account_admin)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        emailEditText = findViewById(R.id.passwordupdatestudent) // Email input
        passwordEditText = findViewById(R.id.dpassword) // Password input
        deleteButton = findViewById(R.id.button10)
        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        deleteButton.setOnClickListener {
            val email = emailEditText.text.toString().trim()
            val password = passwordEditText.text.toString().trim()
            if (email.isNotEmpty() && password.isNotEmpty()) {
                deleteAccount(email, password)
                val intent = Intent(this, WelcomeScreen::class.java)
                startActivity(intent)
            } else {
                Toast.makeText(this, "Please enter your email and password", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun deleteAccount(email: String, password: String) {
        val user = auth.currentUser
        user?.let {
            // Create credentials for re-authentication
            val credential = EmailAuthProvider.getCredential(email, password)

            // Re-authenticate the user
            it.reauthenticate(credential).addOnCompleteListener { reAuthTask ->
                if (reAuthTask.isSuccessful) {
                    // Now delete the user account from Firebase Authentication
                    it.delete().addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            // Now delete data from Firestore for the specific email
                            deleteUserCollections(user.email ?: "")
                        } else {
                            Toast.makeText(this, "Error deleting account: ${task.exception?.message}", Toast.LENGTH_LONG).show()
                        }
                    }
                } else {
                    Toast.makeText(this, "Re-authentication failed: ${reAuthTask.exception?.message}", Toast.LENGTH_LONG).show()
                }
            }
        } ?: run {
            Toast.makeText(this, "No authenticated user found", Toast.LENGTH_SHORT).show()
        }
    }

    private fun deleteUserCollections(email: String) {
        // Specify the collection names to delete documents associated with the email
        val collections = listOf("adminBasicInfo", "adminPersonalInfo", "adminProfessionalInfo", "adminSocialMedia")

        // Use a batch operation to ensure atomicity (all or nothing)
        val batch = db.batch()

        // Loop through each collection and delete the document associated with the email
        for (collection in collections) {
            val docRef = db.collection(collection).document(email)
            batch.delete(docRef)
        }

        // Commit the batch
        batch.commit().addOnCompleteListener { batchTask ->
            if (batchTask.isSuccessful) {
                Toast.makeText(this, "Account and associated data deleted successfully", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Error deleting user data: ${batchTask.exception?.message}", Toast.LENGTH_LONG).show()
            }
        }
    }
}
