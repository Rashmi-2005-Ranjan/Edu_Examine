package com.example.eduexamine

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.eduexamine.AdminActivityFragments.AddCourseFragment.Course
import com.example.eduexamine.AdminActivityFragments.CoursesAdapter
import com.google.firebase.firestore.FirebaseFirestore

class show_added_courses : Fragment() {

    private lateinit var firestore: FirebaseFirestore
    private lateinit var coursesRecyclerView: RecyclerView
    private lateinit var coursesAdapter: CoursesAdapter
    private val coursesList = mutableListOf<Course>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        firestore = FirebaseFirestore.getInstance()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_show_added_courses, container, false)
        coursesRecyclerView = view.findViewById(R.id.coursesRecyclerView)
        coursesRecyclerView.layoutManager = LinearLayoutManager(context)
        coursesAdapter = CoursesAdapter(coursesList)
        coursesRecyclerView.adapter = coursesAdapter
        fetchCourses()
        return view
    }

    private fun fetchCourses() {
        firestore.collection("courses")
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    val course = document.toObject(Course::class.java)
                    coursesList.add(course)
                }
                coursesAdapter.notifyDataSetChanged()
            }
            .addOnFailureListener {
                // Handle the error
            }
    }
}