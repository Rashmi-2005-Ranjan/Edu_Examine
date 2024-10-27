package com.example.eduexamine

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class EducationalDetails : AppCompatActivity() {

    private lateinit var instituteEditText: EditText
    private lateinit var branchEditText: EditText
    private lateinit var rollNumberEditText: EditText
    private lateinit var cgpaEditText: EditText
    private lateinit var graduationYearEditText: EditText
    private lateinit var saveButton: Button
    private lateinit var firestore: FirebaseFirestore
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_educational_details)

        // Initialize Firestore and FirebaseAuth
        firestore = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()

        // Find views by ID
        instituteEditText = findViewById(R.id.institutename)
        branchEditText = findViewById(R.id.branch)
        rollNumberEditText = findViewById(R.id.id)
        cgpaEditText = findViewById(R.id.cgpa)
        graduationYearEditText = findViewById(R.id.gender)
        saveButton = findViewById(R.id.button6)

        // Load existing data
        loadEducationalInfo()

        // Set up the click listener for the save button
        saveButton.setOnClickListener {
            saveEducationalInfo()
        }
    }

    private fun loadEducationalInfo() {
        val email = auth.currentUser?.email ?: return
        val documentId = email.replace("_", ".") // Replace '.' as Firestore doesn't allow it in IDs

        firestore.collection("studentEducationalInfo").document(documentId)
            .get()
            .addOnSuccessListener { document ->
                if (document != null && document.exists()) {
                    instituteEditText.setText(document.getString("institute"))
                    branchEditText.setText(document.getString("branch"))
                    rollNumberEditText.setText(document.getString("rollNumber"))
                    cgpaEditText.setText(document.getString("cgpa"))
                    graduationYearEditText.setText(document.getString("graduationYear"))
                } else {
                    Toast.makeText(this, "No data found for this user", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Error retrieving information: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun saveEducationalInfo() {
        val institute = instituteEditText.text.toString()
        val branch = branchEditText.text.toString()
        val rollNumber = rollNumberEditText.text.toString()
        val cgpa = cgpaEditText.text.toString()
        val graduationYear = graduationYearEditText.text.toString()
        val email = auth.currentUser?.email ?: return

        if (institute.isEmpty() || branch.isEmpty() || rollNumber.isEmpty() || cgpa.isEmpty() || graduationYear.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
            return
        }

        val educationalInfo = hashMapOf(
            "institute" to institute,
            "branch" to branch,
            "rollNumber" to rollNumber,
            "cgpa" to cgpa,
            "graduationYear" to graduationYear
        )

        val documentId = email.replace("_", ".")
        firestore.collection("studentEducationalInfo").document(documentId)
            .set(educationalInfo)
            .addOnSuccessListener {
                Toast.makeText(this, "Information saved successfully!", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Error saving information: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }
}
