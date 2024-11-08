package com.example.eduexamine

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class UpdateGroupActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: gStudentAdapter
    private val studentList = ArrayList<gStudent>()
    private val db = FirebaseFirestore.getInstance()
    private var groupName: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_group)

        groupName = intent.getStringExtra("groupName")

        recyclerView = findViewById(R.id.rectangles_recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Set up adapter with delete functionality
        adapter = gStudentAdapter(studentList) { student ->
            onDeleteStudent(student)
        }
        recyclerView.adapter = adapter

        // Fetch students for the group
        fetchStudentsInGroup()
    }

    private fun fetchStudentsInGroup() {
        val groupName = groupName ?: return

        val currentAdminEmail = FirebaseAuth.getInstance().currentUser?.email
        if (currentAdminEmail == null) {
            Toast.makeText(this, "Admin is not logged in", Toast.LENGTH_SHORT).show()
            return
        }

        // Log the groupName and admin email for debugging purposes
        Log.d("FetchStudents", "Fetching students for group: $groupName under admin: $currentAdminEmail")

        // Query the students under the specific group and admin
        db.collection("groups")
            .document(currentAdminEmail)  // Using the current admin's email
            .collection("studentGroups")
            .document(groupName)  // Ensure this is fetching the group document by name
            .collection("students")  // Query the students collection inside the group
            .get()
            .addOnSuccessListener { result ->
                studentList.clear()

                if (result.isEmpty) {
                    Toast.makeText(this, "No students found in this group", Toast.LENGTH_SHORT).show()
                } else {
                    for (document in result) {
                        val registrationNo = document.id  // Use the document ID as the registration number
                        val email = document.getString("email") ?: continue
                        val group = document.getString("groupName") ?: continue

                        // Add the student to the list
                        studentList.add(gStudent(registrationNo, email))                    }
                    adapter.notifyDataSetChanged()  // Notify the adapter to update the UI
                }
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Error fetching students: ${e.message}", Toast.LENGTH_SHORT).show()
                e.printStackTrace()  // Print the stack trace for debugging
            }
    }



    private fun onDeleteStudent(student: gStudent) {
        val groupName = groupName ?: return

        // Get the current admin's email
        val currentAdminEmail = FirebaseAuth.getInstance().currentUser?.email
        if (currentAdminEmail == null) {
            Toast.makeText(this, "Admin is not logged in", Toast.LENGTH_SHORT).show()
            return
        }

        // Delete the student from the group in Firestore
        db.collection("groups")
            .document(currentAdminEmail) // Using the current admin's email
            .collection("studentGroups")
            .document(groupName)
            .collection("students")
            .document(student.registrationNo) // Assuming registration number is unique
            .delete()
            .addOnSuccessListener {
                Toast.makeText(this, "Student deleted successfully", Toast.LENGTH_SHORT).show()
                fetchStudentsInGroup() // Refresh the list after deletion
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Error deleting student: ${e.message}", Toast.LENGTH_SHORT).show()
                e.printStackTrace() // Print the stack trace for detailed error
            }
    }
}
