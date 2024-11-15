package com.example.eduexamine.StudentActivityFragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.eduexamine.AdminActivityFragments.AddCourseFragment
import com.example.eduexamine.AdminActivityFragments.CoursesAdapter
import com.example.eduexamine.R
import com.google.firebase.auth.FirebaseAuth
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

        coursesRecyclerView = view.findViewById(R.id.coursesRecyclerView)
        coursesRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        coursesAdapter = CoursesAdapter(coursesList)
        coursesRecyclerView.adapter = coursesAdapter

        fetchCourses()

        return view
    }

    private fun fetchCourses() {
        val currentAdminEmail = FirebaseAuth.getInstance().currentUser?.email
        if (currentAdminEmail == null) {
            // Handle user not logged in
            return
        }

        db.collection("groups")
            .document(currentAdminEmail)
            .collection("studentGroups")
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    val courses = document.get("courses") as? List<*> ?: continue
                    for (courseCode in courses) {
                        db.collection("courses")
                            .document(courseCode.toString())
                            .get()
                            .addOnSuccessListener { courseDocument ->
                                val course = courseDocument.toObject(AddCourseFragment.Course::class.java)
                                if (course != null) {
                                    coursesList.add(course)
                                    coursesAdapter.notifyDataSetChanged()
                                }
                            }
                    }
                }
            }
    }
}