package com.example.eduexamine

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.eduexamine.AdminActivityFragments.AddCourseFragment
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
        coursesRecyclerView = view.findViewById(R.id.coursesRecyclerView1)
        coursesRecyclerView.layoutManager = LinearLayoutManager(context)
        coursesAdapter = CoursesAdapter(coursesList)
        coursesRecyclerView.adapter = coursesAdapter
        fetchCourses()

        coursesRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                // Implement scroll effect logic here
                if (dy > 0) {
                    // Scrolling down
                    // Add your effect here
                } else {
                    // Scrolling up
                    // Add your effect here
                }
            }
        })

        return view
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun fetchCourses() {
        firestore.collection("courses")
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    val course = document.toObject(AddCourseFragment.Course::class.java)
                    if (course != null) {
                        coursesList.add(course)
                    }
                }
                coursesAdapter.notifyDataSetChanged()
            }
            .addOnFailureListener { exception ->
                Log.e("FetchCourses", "Error fetching courses: ${exception.message}")
            }
    }
}