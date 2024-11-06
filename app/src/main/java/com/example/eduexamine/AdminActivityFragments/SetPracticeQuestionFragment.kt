package com.example.eduexamine.AdminActivityFragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.eduexamine.R
import com.example.eduexamine.Student
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class SetPracticeQuestionFragment : Fragment() {

    private lateinit var mySpinner: Spinner
    private lateinit var registrationEditText: EditText
    private lateinit var emailEditText: EditText
    private lateinit var addButton: Button

    private val groups: MutableMap<String, MutableList<Student>> = mutableMapOf() // Store groups and their students
    private val db = FirebaseFirestore.getInstance() // Firebase Firestore instance

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_set_practice_question, container, false)

        // Initialize UI components
        mySpinner = view.findViewById(R.id.mySpinner)
        registrationEditText = view.findViewById(R.id.edit_FullName)
        emailEditText = view.findViewById(R.id.mailid)
        addButton = view.findViewById(R.id.button11)

        // Create a list of items for the Spinner
        val spinnerItems = listOf(
            "Select Group", "B.Tech", "B.E", "M.Tech", "M.E", "BCA", "MCA", "B.Sc",
            "M.Sc", "B.Com", "M.Com", "B.A", "M.A", "BBA", "MBA", "BDS", "MBBS",
            "B.Pharm", "M.Pharm", "B.Arch", "M.Arch", "BHM", "MHM", "BBA", "MBA",
            "BBA LLB", "LLB", "LLM", "B.Ed", "M.Ed", "B.P.Ed", "M.P.Ed", "B.Lib",
            "M.Lib", "BDS", "MDS", "BHMS", "DHMS", "BUMS", "BAMS", "BNYS", "BPT",
            "MPT", "BOT", "Class Group"
        )

        // Create an ArrayAdapter using the string list
        val adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item, spinnerItems
        )

        // Set the dropdown view layout
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        // Apply the adapter to the spinner
        mySpinner.adapter = adapter

        // Handle the add student button click
        addButton.setOnClickListener {
            addStudentToGroup()
        }

        return view
    }

    private fun addStudentToGroup() {
        val selectedGroup = mySpinner.selectedItem.toString()
        val registrationNumber = registrationEditText.text.toString().trim()
        val email = emailEditText.text.toString().trim()

        if (selectedGroup == "Select Group" || registrationNumber.isEmpty() || email.isEmpty()) {
            Toast.makeText(requireContext(), "Please fill in all fields", Toast.LENGTH_SHORT).show()
            return
        }

        // Check if the group exists or create a new one
        if (!groups.containsKey(selectedGroup)) {
            groups[selectedGroup] = mutableListOf() // Create new group if it doesn't exist
            saveGroupToFirebase(selectedGroup) // Save the new group to Firebase
        }

        // Add the new student to the group
        val student = Student(registrationNumber, email, selectedGroup)
        groups[selectedGroup]?.add(student) // Add student to the local group list

        // Update the Firebase with the new student in the group
        updateGroupInFirebase(selectedGroup, student)

        // Clear the input fields after adding
        registrationEditText.text.clear()
        emailEditText.text.clear()

        Toast.makeText(requireContext(), "Student added to $selectedGroup group", Toast.LENGTH_SHORT).show()
    }

    private fun saveGroupToFirebase(groupName: String) {
        val currentAdminEmail = FirebaseAuth.getInstance().currentUser?.email // Get the current user's email
        if (currentAdminEmail == null) {
            Toast.makeText(requireContext(), "User is not logged in", Toast.LENGTH_SHORT).show()
            return
        }

        val groupData = hashMapOf(
            "groupName" to groupName,
            "adminEmail" to currentAdminEmail
        )

        db.collection("groups")
            .document(currentAdminEmail)
            .collection("studentGroups")
            .document(groupName)
            .set(groupData)
            .addOnSuccessListener {
                // Group successfully saved
            }
            .addOnFailureListener { e ->
                Toast.makeText(requireContext(), "Error saving group: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun updateGroupInFirebase(groupName: String, student: Student) {
        val currentAdminEmail = FirebaseAuth.getInstance().currentUser?.email // Get the current user's email
        if (currentAdminEmail == null) {
            Toast.makeText(requireContext(), "User is not logged in", Toast.LENGTH_SHORT).show()
            return
        }

        // Add the new student to the corresponding group's document
        db.collection("groups")
            .document(currentAdminEmail) // Reference to the admin's document
            .collection("studentGroups") // Access the student groups collection
            .document(groupName) // Access the specific group document
            .collection("students") // Create or reference the students subcollection
            .document(student.registrationNumber) // Using registration number as unique ID for student
            .set(student)
            .addOnSuccessListener {
                // Student successfully added
            }
            .addOnFailureListener { e ->
                Toast.makeText(requireContext(), "Error adding student: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }
}
