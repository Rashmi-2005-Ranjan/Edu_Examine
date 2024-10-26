package com.example.eduexamine

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class ProfessionalInfoAdmin : AppCompatActivity() {
    private lateinit var employeeId: EditText
    private lateinit var role: EditText
    private lateinit var department: EditText
    private lateinit var experience: EditText
    private lateinit var expertise: EditText
    private lateinit var saveButton: Button
    private lateinit var firestore: FirebaseFirestore
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_professional_info_admin)

        // Initialize Firestore and FirebaseAuth
        firestore = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()

        // Find views by ID
        employeeId = findViewById(R.id.edit_FullName)
        role = findViewById(R.id.email)
        department = findViewById(R.id.phno)
        experience = findViewById(R.id.dobEditText)
        expertise = findViewById(R.id.gender)
        saveButton = findViewById(R.id.button6)

        // Load existing data when the activity starts
        loadProfessionalInfo()

        // Set up click listener for saving information
        saveButton.setOnClickListener {
            saveProfessionalInfo()
        }
    }

    private fun loadProfessionalInfo() {
        // Get the logged-in user's email
        val userEmail = auth.currentUser?.email ?: return

        // Retrieve user data from Firestore using email as document ID
        firestore.collection("adminProfessionalInfo").document(userEmail)
            .get()
            .addOnSuccessListener { document ->
                if (document != null && document.exists()) {
                    // Populate fields with the retrieved data
                    employeeId.setText(document.getString("employeeId"))
                    role.setText(document.getString("role"))
                    department.setText(document.getString("department"))
                    experience.setText(document.getString("experience"))
                    expertise.setText(document.getString("expertise"))
                } else {
                    Toast.makeText(this, "No data found for this user", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Error retrieving information: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun saveProfessionalInfo() {
        // Retrieve user input
        val empId = employeeId.text.toString()
        val roleText = role.text.toString()
        val dept = department.text.toString()
        val exp = experience.text.toString()
        val expArea = expertise.text.toString()
        val userEmail = auth.currentUser?.email ?: return

        // Ensure all fields are filled
        if (empId.isEmpty() || roleText.isEmpty() || dept.isEmpty() || exp.isEmpty() || expArea.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
            return
        }

        // Create a data map
        val professionalInfo = hashMapOf(
            "employeeId" to empId,
            "role" to roleText,
            "department" to dept,
            "experience" to exp,
            "expertise" to expArea
        )

        // Save to Firestore with user email as document ID
        firestore.collection("adminProfessionalInfo").document(userEmail)
            .set(professionalInfo)
            .addOnSuccessListener {
                Toast.makeText(this, "Information saved successfully!", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Error saving information: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }
}
