package com.example.eduexamine.StudentActivityFragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.eduexamine.R
import com.google.firebase.firestore.FirebaseFirestore

class addedCourses : Fragment() {

    private lateinit var coursesRecyclerView: RecyclerView
    private lateinit var groupAdapter: GroupAdapter
    private val groupList = mutableListOf<Group>()
    private val db = FirebaseFirestore.getInstance()
    private lateinit var showCoursesButton: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_added_courses, container, false)

        coursesRecyclerView = view.findViewById(R.id.studentRecyclerView)
        coursesRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        groupAdapter = GroupAdapter(groupList)
        coursesRecyclerView.adapter = groupAdapter

        showCoursesButton = view.findViewById(R.id.showCoursesButton)

        // Fetch courses when the fragment is loaded
        fetchCourses()

        // Handle button click to pass selected course numbers to the next fragment
        showCoursesButton.setOnClickListener {
            val selectedCourses = getSelectedCourses() // This function should return a list of selected course IDs
            val bundle = Bundle()
            bundle.putStringArrayList("courseIds", ArrayList(selectedCourses))

            // Replace the current fragment with CourseDetails
            val courseDetailsFragment = CourseDetails()
            courseDetailsFragment.arguments = bundle

            val transaction = parentFragmentManager.beginTransaction()
            transaction.replace(R.id.fragment_container, courseDetailsFragment)
            transaction.addToBackStack(null)
            transaction.commit()
        }

        return view
    }

    // Fetch courses data from Firestore
    private fun fetchCourses() {
        val currentAdminEmail = "empedu01@admin.edu.in" // Fixed admin email

        db.collection("groups")
            .document(currentAdminEmail)
            .collection("studentGroups")
            .get()
            .addOnSuccessListener { groupDocuments ->
                for (groupDocument in groupDocuments) {
                    val groupName = groupDocument.getString("groupName") ?: "N/A"
                    val courses = groupDocument.get("courses") as? List<String> ?: emptyList()

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

                            groupList.add(Group(groupName, courses, studentList))
                            groupAdapter.notifyDataSetChanged()
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

    // This is a placeholder function to get the selected courses (you can implement based on your UI).
    private fun getSelectedCourses(): List<String> {
        val selectedCourses = mutableListOf<String>()
        for (group in groupList) {
            if (group.isSelected) {
                selectedCourses.addAll(group.courses)
            }
        }
        return selectedCourses
    }
}
