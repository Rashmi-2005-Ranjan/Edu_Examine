package com.example.eduexamine

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.*

class BasicInfosAdmin : AppCompatActivity() {

    private lateinit var fullNameEditText: EditText
    private lateinit var emailEditText: EditText
    private lateinit var phoneEditText: EditText
    private lateinit var dobEditText: EditText
    private lateinit var genderEditText: EditText
    private lateinit var saveButton: Button
    private lateinit var firestore: FirebaseFirestore
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_basic_infos_admin)

        // Initialize Firestore and FirebaseAuth
        firestore = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()

        // Find views by ID
        fullNameEditText = findViewById(R.id.edit_FullName)
        emailEditText = findViewById(R.id.email)
        phoneEditText = findViewById(R.id.phno)
        dobEditText = findViewById(R.id.dobEditText)
        genderEditText = findViewById(R.id.gender)
        saveButton = findViewById(R.id.button6)

        // Load existing data
        loadBasicInfo()

        // Set up the click listener for the save button
        saveButton.setOnClickListener {
            saveBasicInfo()
        }

        // Set up the click listener for the Date of Birth EditText
        dobEditText.setOnClickListener {
            showDatePickerDialog()
        }
    }

    private fun showDatePickerDialog() {
        // Get the current date
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        // Create DatePickerDialog
        val datePickerDialog = DatePickerDialog(
            this,
            { _, selectedYear, selectedMonth, selectedDay ->
                // Format the selected date and set it to the EditText
                val selectedDate = "$selectedDay/${selectedMonth + 1}/$selectedYear"
                dobEditText.setText(selectedDate)
            },
            year, month, day
        )

        // Show the DatePickerDialog
        datePickerDialog.show()
    }

    private fun loadBasicInfo() {
        // Get the logged-in user's email
        val email = auth.currentUser?.email ?: return

        // Replace '.' in email to create document ID
        val documentId = email.replace(".", "_")

        // Retrieve user data from Firestore
        firestore.collection("adminBasicInfo").document(documentId)
            .get()
            .addOnSuccessListener { document ->
                if (document != null && document.exists()) {
                    // Populate fields with data
                    fullNameEditText.setText(document.getString("fullName"))
                    phoneEditText.setText(document.getString("phone"))
                    dobEditText.setText(document.getString("dob"))
                    genderEditText.setText(document.getString("gender"))
                    // Optionally, set email to emailEditText if you want to display it
                    emailEditText.setText(email)
                } else {
                    Toast.makeText(this, "No data found for this user", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Error retrieving information: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun saveBasicInfo() {
        // Get input values
        val fullName = fullNameEditText.text.toString()
        val email = auth.currentUser?.email ?: return // Get the logged-in user's email
        val phone = phoneEditText.text.toString()
        val dob = dobEditText.text.toString()
        val gender = genderEditText.text.toString()

        // Check if fields are not empty
        if (fullName.isEmpty() || phone.isEmpty() || dob.isEmpty() || gender.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
            return
        }

        // Create a data object
        val basicInfo = hashMapOf(
            "fullName" to fullName,
            "email" to email,
            "phone" to phone,
            "dob" to dob,
            "gender" to gender
        )

        // Save to Firestore with a unique document ID for each user (using email as an example)
        val documentId = email.replace(".", "_") // Firestore doesn't allow '.' in document IDs
        firestore.collection("adminBasicInfo").document(documentId)
            .set(basicInfo)
            .addOnSuccessListener {
                Toast.makeText(this, "Information saved successfully!", Toast.LENGTH_SHORT).show()
                // Optionally clear the fields after saving
//                clearFields()
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Error saving information: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun clearFields() {
        fullNameEditText.text.clear()
        phoneEditText.text.clear()
        dobEditText.text.clear()
        genderEditText.text.clear()
    }
}
