package com.example.eduexamine

import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class AcademicInfoStudent : AppCompatActivity() {

    private lateinit var specializationField: TextInputEditText
    private lateinit var certificationsField: TextInputEditText
    private lateinit var projectField: TextInputEditText
    private lateinit var githubField: TextInputEditText
    private lateinit var internshipsField: TextInputEditText
    private lateinit var firestore: FirebaseFirestore
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_academic_info_student)

        // Initialize Firebase
        firestore = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()

        // Initialize Views
        specializationField = findViewById(R.id.specialization)
        certificationsField = findViewById(R.id.branch)
        projectField = findViewById(R.id.id)
        githubField = findViewById(R.id.cgpa)
        internshipsField = findViewById(R.id.gender)
        val submitButton: Button = findViewById(R.id.submit)

        // Load existing data
        loadAcademicInfo()

        // Set onClickListener for Save Info button
        submitButton.setOnClickListener {
            saveAcademicInfo()
        }
    }

    private fun loadAcademicInfo() {
        val userEmail = auth.currentUser?.email ?: return
        val documentId = userEmail.replace("_", ".")

        firestore.collection("studentAcademicInfo").document(documentId)
            .get()
            .addOnSuccessListener { document ->
                if (document != null && document.exists()) {
                    specializationField.setText(document.getString("specialization"))
                    certificationsField.setText(document.getString("certifications"))
                    projectField.setText(document.getString("project"))
                    githubField.setText(document.getString("github"))
                    internshipsField.setText(document.getString("internships"))
                } else {
                    Toast.makeText(this, "No academic info found for this user", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Error retrieving academic info: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun saveAcademicInfo() {
        val specialization = specializationField.text.toString()
        val certifications = certificationsField.text.toString()
        val project = projectField.text.toString()
        val github = githubField.text.toString()
        val internships = internshipsField.text.toString()

        if (specialization.isEmpty() || certifications.isEmpty() || project.isEmpty() || github.isEmpty() || internships.isEmpty()) {
            Toast.makeText(this, "Please fill out all required fields", Toast.LENGTH_SHORT).show()
            return
        }

        val academicInfo = hashMapOf(
            "specialization" to specialization,
            "certifications" to certifications,
            "project" to project,
            "github" to github,
            "internships" to internships
        )

        // Get the current user's email and replace "_" with "."
        val userEmail = auth.currentUser?.email?.replace("_", ".") ?: return

        firestore.collection("studentAcademicInfo")
            .document(userEmail)
            .set(academicInfo)
            .addOnSuccessListener {
                Toast.makeText(this, "Academic Info saved successfully", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Failed to save Academic Info: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }
}
