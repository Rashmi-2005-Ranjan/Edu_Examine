package com.example.eduexamine

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class SocialStudents : AppCompatActivity() {

    private lateinit var linkedinEditText: EditText
    private lateinit var twitterEditText: EditText
    private lateinit var facebookEditText: EditText
    private lateinit var telegramEditText: EditText
    private lateinit var instagramEditText: EditText
    private lateinit var saveButton: Button
    private lateinit var firestore: FirebaseFirestore
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_social_students)

        // Initialize Firestore and FirebaseAuth
        firestore = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()

        // Find views by ID
        linkedinEditText = findViewById(R.id.linkedin)
        twitterEditText = findViewById(R.id.twitter)
        facebookEditText = findViewById(R.id.facebook)
        telegramEditText = findViewById(R.id.telegram)
        instagramEditText = findViewById(R.id.instagram)
        saveButton = findViewById(R.id.button7)

        // Load existing data
        loadSocialInfo()

        // Set up the click listener for the save button
        saveButton.setOnClickListener {
            saveSocialInfo()
        }
    }

    private fun loadSocialInfo() {
        val email = auth.currentUser?.email ?: return
        val documentId = email.replace("_", ".")

        firestore.collection("studentSocialMediaInfo").document(documentId)
            .get()
            .addOnSuccessListener { document ->
                if (document != null && document.exists()) {
                    linkedinEditText.setText(document.getString("linkedin"))
                    twitterEditText.setText(document.getString("twitter"))
                    facebookEditText.setText(document.getString("facebook"))
                    telegramEditText.setText(document.getString("telegram"))
                    instagramEditText.setText(document.getString("instagram"))
                } else {
                    Toast.makeText(this, "No social media data found for this user", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Error retrieving social media info: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun saveSocialInfo() {
        val linkedin = linkedinEditText.text.toString()
        val twitter = twitterEditText.text.toString()
        val facebook = facebookEditText.text.toString()
        val telegram = telegramEditText.text.toString()
        val instagram = instagramEditText.text.toString()
        val email = auth.currentUser?.email ?: return

        if (linkedin.isEmpty() || twitter.isEmpty() || facebook.isEmpty() || telegram.isEmpty() || instagram.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
            return
        }

        val socialInfo = hashMapOf(
            "linkedin" to linkedin,
            "twitter" to twitter,
            "facebook" to facebook,
            "telegram" to telegram,
            "instagram" to instagram
        )

        val documentId = email.replace("_", ".")
        firestore.collection("studentSocialMediaInfo").document(documentId)
            .set(socialInfo)
            .addOnSuccessListener {
                Toast.makeText(this, "Social Media Information saved successfully!", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Error saving social media info: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }
}
