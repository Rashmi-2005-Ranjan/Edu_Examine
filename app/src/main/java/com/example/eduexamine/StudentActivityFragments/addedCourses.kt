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
import com.example.eduexamine.AdminActivityFragments.AddCourseFragment
import com.example.eduexamine.AdminActivityFragments.CoursesAdapter
import com.example.eduexamine.R
import com.google.firebase.firestore.FirebaseFirestore

class addedCourses : Fragment() {

    private lateinit var coursesRecyclerView: RecyclerView
    private lateinit var coursesAdapter: CoursesAdapter
    private val coursesList = mutableListOf<AddCourseFragment.Course>()
    private val db = FirebaseFirestore.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_added_courses, container, false)

        coursesRecyclerView = view.findViewById(R.id.coursesRecyclerView1)
        coursesRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        coursesAdapter = CoursesAdapter(coursesList)
        coursesRecyclerView.adapter = coursesAdapter

        fetchCourses()

        return view
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun fetchCourses() {
        val db = FirebaseFirestore.getInstance()
        val currentAdminEmail = "empedu01@admin.edu.in" // Update this as necessary

        // Access the admin's studentGroups collection
        db.collection("groups")
            .document(currentAdminEmail)
            .collection("studentGroups")
            .get()
            .addOnSuccessListener { groupDocuments ->
                for (groupDocument in groupDocuments) {
                    // Each document in studentGroups represents a student group (like "B.Tech")
                    db.collection("groups")
                        .document(currentAdminEmail)
                        .collection("studentGroups")
                        .document(groupDocument.id)
                        .collection("students")
                        .get()
                        .addOnSuccessListener { studentDocuments ->
                            for (studentDocument in studentDocuments) {
                                // Assume each student document has course details directly under it
                                val course = studentDocument.toObject(AddCourseFragment.Course::class.java)
                                coursesList.add(course)
                                coursesAdapter.notifyDataSetChanged()
                            }
                        }
                        .addOnFailureListener { exception ->
                            // Handle failure to get students
                            Log.e("FetchCourses", "Error fetching students: ${exception.message}")
                        }
                }
            }
            .addOnFailureListener { exception ->
                // Handle failure to get student groups
                Log.e("FetchCourses", "Error fetching students: ${exception.message}")
            }
    }

}