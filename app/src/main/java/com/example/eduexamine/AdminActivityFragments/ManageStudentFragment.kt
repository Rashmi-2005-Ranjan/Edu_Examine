package com.example.eduexamine.AdminActivityFragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.eduexamine.Group
import com.example.eduexamine.R
import com.example.eduexamine.Student
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QueryDocumentSnapshot

class ManageStudentFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: GroupAdapter
    private val db = FirebaseFirestore.getInstance()
    private val groups = mutableListOf<Group>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_manage_student, container, false)

        // Initialize RecyclerView
        recyclerView = view.findViewById(R.id.rectangles_recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        adapter = GroupAdapter(groups, ::onUpdateStudent, ::onDeleteStudent)
        recyclerView.adapter = adapter

        // Fetch groups and students from Firebase
        fetchGroups()

        return view
    }

    private fun fetchGroups() {
        val currentAdminEmail = FirebaseAuth.getInstance().currentUser?.email
        if (currentAdminEmail == null) {
            Toast.makeText(requireContext(), "User is not logged in", Toast.LENGTH_SHORT).show()
            return
        }

        db.collection("groups")
            .document(currentAdminEmail)
            .collection("studentGroups")
            .get()
            .addOnSuccessListener { result ->
                groups.clear()
                for (document in result) {
                    val groupName = document.getString("groupName") ?: continue
                    val students = mutableListOf<Student>()

                    // Fetch students for each group
                    fetchStudents(groupName, students)
                    groups.add(Group(groupName, students))
                }
                adapter.notifyDataSetChanged()
            }
            .addOnFailureListener { e ->
                Toast.makeText(requireContext(), "Error fetching groups: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun fetchStudents(groupName: String, students: MutableList<Student>) {
        val currentAdminEmail = FirebaseAuth.getInstance().currentUser?.email
        db.collection("groups")
            .document(currentAdminEmail!!)
            .collection("studentGroups")
            .document(groupName)
            .collection("students")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    val student = document.toObject(Student::class.java)
                    students.add(student)
                }
                adapter.notifyDataSetChanged()
            }
            .addOnFailureListener { e ->
                Toast.makeText(requireContext(), "Error fetching students: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun onUpdateStudent(student: Student) {
        // Implement update logic here (show dialog to update student)
        Toast.makeText(requireContext(), "Update Student: ${student.registrationNumber}", Toast.LENGTH_SHORT).show()
    }

    private fun onDeleteStudent(student: Student) {
        val currentAdminEmail = FirebaseAuth.getInstance().currentUser?.email
        db.collection("groups")
            .document(currentAdminEmail!!)
            .collection("studentGroups")
            .document(student.groupName)
            .collection("students")
            .document(student.registrationNumber)
            .delete()
            .addOnSuccessListener {
                Toast.makeText(requireContext(), "Student deleted", Toast.LENGTH_SHORT).show()
                fetchGroups() // Refresh data after deletion
            }
            .addOnFailureListener { e ->
                Toast.makeText(requireContext(), "Error deleting student: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }
}
