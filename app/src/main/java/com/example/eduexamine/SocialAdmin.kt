package com.example.eduexamine

import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class SocialAdmin : AppCompatActivity() {

    private lateinit var linkedinField: TextInputEditText
    private lateinit var twitterField: TextInputEditText
    private lateinit var facebookField: TextInputEditText
    private lateinit var telegramField: TextInputEditText
    private lateinit var instagramField: TextInputEditText

    private val db = FirebaseFirestore.getInstance()
    private val currentUserEmail = FirebaseAuth.getInstance().currentUser?.email

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_social_admin)

        linkedinField = findViewById(R.id.linkedin)
        twitterField = findViewById(R.id.twitter)
        facebookField = findViewById(R.id.facebook)
        telegramField = findViewById(R.id.telegram)
        instagramField = findViewById(R.id.instagram)

        loadUserData()

        findViewById<Button>(R.id.button7).setOnClickListener {
            saveUserData()
        }
    }

    private fun loadUserData() {
        currentUserEmail?.let { email ->
            db.collection("adminSocialMedia").document(email).get()
                .addOnSuccessListener { document ->
                    if (document.exists()) {
                        linkedinField.setText(document.getString("LinkedIn") ?: "")
                        twitterField.setText(document.getString("Twitter") ?: "")
                        facebookField.setText(document.getString("Facebook") ?: "")
                        telegramField.setText(document.getString("Telegram") ?: "")
                        instagramField.setText(document.getString("Instagram") ?: "")
                    } else {
                        Toast.makeText(this, "No data found for this user", Toast.LENGTH_SHORT).show()
                    }
                }
                .addOnFailureListener {
                    Toast.makeText(this, "Failed to load data", Toast.LENGTH_SHORT).show()
                }
        } ?: run {
            Toast.makeText(this, "User not authenticated", Toast.LENGTH_SHORT).show()
        }
    }

    private fun saveUserData() {
        val userData = hashMapOf(
            "LinkedIn" to linkedinField.text.toString(),
            "Twitter" to twitterField.text.toString(),
            "Facebook" to facebookField.text.toString(),
            "Telegram" to telegramField.text.toString(),
            "Instagram" to instagramField.text.toString()
        )

        currentUserEmail?.let { email ->
            db.collection("adminSocialMedia").document(email).set(userData)
                .addOnSuccessListener {
                    Toast.makeText(this, "Data saved successfully", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener {
                    Toast.makeText(this, "Failed to save data", Toast.LENGTH_SHORT).show()
                }
        } ?: run {
            Toast.makeText(this, "User not authenticated", Toast.LENGTH_SHORT).show()
        }
    }
}
