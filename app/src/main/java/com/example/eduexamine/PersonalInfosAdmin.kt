package com.example.eduexamine

import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class PersonalInfosAdmin : AppCompatActivity() {

    private lateinit var editFullName: TextInputEditText
    private lateinit var emailField: TextInputEditText
    private lateinit var phnoField: TextInputEditText
    private lateinit var dobEditText: TextInputEditText
    private lateinit var genderField: TextInputEditText

    private val db = FirebaseFirestore.getInstance()
    private val currentUserEmail = FirebaseAuth.getInstance().currentUser?.email

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_personal_infos_admin)

        editFullName = findViewById(R.id.edit_FullName)
        emailField = findViewById(R.id.email)
        phnoField = findViewById(R.id.phno)
        dobEditText = findViewById(R.id.dobEditText)
        genderField = findViewById(R.id.gender)

        // Load data when the activity is created
        loadUserData()

        // Save data on button click
        findViewById<Button>(R.id.button6).setOnClickListener {
            saveUserData()
        }
    }

    private fun loadUserData() {
        currentUserEmail?.let { email ->
            db.collection("adminPersonalInfo") // Ensure consistent collection name
                .document(email).get()
                .addOnSuccessListener { document ->
                    if (document.exists()) {
                        // Retrieve fields and set them to UI components
                        editFullName.setText(document.getString("EmergencyContact") ?: "")
                        emailField.setText(document.getString("Address") ?: "")
                        phnoField.setText(document.getString("Hobbies") ?: "")
                        dobEditText.setText(document.getString("PreferredContact") ?: "")
                        genderField.setText(document.getString("MotherTongue") ?: "")
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
            "EmergencyContact" to editFullName.text.toString(),
            "Address" to emailField.text.toString(),
            "Hobbies" to phnoField.text.toString(),
            "PreferredContact" to dobEditText.text.toString(),
            "MotherTongue" to genderField.text.toString()
        )

        currentUserEmail?.let { email ->
            db.collection("adminPersonalInfo").document(email).set(userData)
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
