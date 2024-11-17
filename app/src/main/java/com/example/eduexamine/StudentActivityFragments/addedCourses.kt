package com.example.eduexamine.StudentActivityFragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.eduexamine.R
import com.google.firebase.firestore.FirebaseFirestore

class addedCourses : Fragment() {

    private lateinit var coursesRecyclerView: RecyclerView
    private lateinit var groupAdapter: GroupAdapter
    private val groupList = mutableListOf<Group>()
    private val db = FirebaseFirestore.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_added_courses, container, false)

        coursesRecyclerView = view.findViewById(R.id.studentRecyclerView)
        coursesRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        groupAdapter = GroupAdapter(groupList)
        coursesRecyclerView.adapter = groupAdapter

        fetchCourses()

        return view
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun fetchCourses() {
        val currentAdminEmail = "empedu01@admin.edu.in" // Fixed admin email

        // Access the admin's studentGroups collection
        db.collection("groups")
            .document(currentAdminEmail)
            .collection("studentGroups")
            .get()
            .addOnSuccessListener { groupDocuments ->
                for (groupDocument in groupDocuments) {
                    // Fetch courses directly from the `courses` field or subcollection
                    val groupName = groupDocument.getString("groupName") ?: "N/A"
                    val courses = groupDocument.get("courses") as? List<String> ?: emptyList()

                    // Access the students for each group
                    db.collection("groups")
                        .document(currentAdminEmail)
                        .collection("studentGroups")
                        .document(groupDocument.id)
                        .collection("students")
                        .get()
                        .addOnSuccessListener { studentDocuments ->
                            val studentList = mutableListOf<Student>()
                            for (studentDocument in studentDocuments) {
                                val email = studentDocument.getString("email")
                                val registrationNumber = studentDocument.getString("registrationNumber")
                                if (email != null && registrationNumber != null) {
                                    studentList.add(Student(email, registrationNumber))
                                }
                            }

                            // Add group to the list
                            groupList.add(Group(groupName, courses, studentList))
                            groupAdapter.notifyDataSetChanged() // Notify adapter that data has changed
                        }
                        .addOnFailureListener { exception ->
                            Log.e("FetchCourses", "Error fetching students: ${exception.message}")
                        }
                }
            }
            .addOnFailureListener { exception ->
                Log.e("FetchCourses", "Error fetching student groups: ${exception.message}")
            }
    }
}
