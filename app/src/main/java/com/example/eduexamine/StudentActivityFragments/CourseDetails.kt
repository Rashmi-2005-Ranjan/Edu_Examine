package com.example.eduexamine.StudentActivityFragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.eduexamine.R
import com.google.firebase.firestore.FirebaseFirestore

class CourseDetails : Fragment() {

    private val db = FirebaseFirestore.getInstance()
    private var courseIds: List<String>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Retrieve course IDs passed from the previous fragment
        courseIds = arguments?.getStringArrayList("courseIds")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_course_details, container, false)
        val courseDetailsTextView = view.findViewById<TextView>(R.id.courseDetailsTextView)

        if (courseIds.isNullOrEmpty()) {
            courseDetailsTextView.text = "No courses selected."
        } else {
            fetchCourseDetails(courseDetailsTextView)
        }

        return view
    }

    @SuppressLint("SetTextI18n")
    private fun fetchCourseDetails(courseDetailsTextView: TextView) {
        courseIds?.forEach { courseId ->
            db.collection("courses")
                .whereEqualTo("code", courseId)  // Querying by the 'code' field
                .get()
                .addOnSuccessListener { querySnapshot ->
                    if (!querySnapshot.isEmpty) {
                        querySnapshot.documents.forEach { document ->
                            val code = document.getString("code") ?: "N/A"
                            val active = document.getBoolean("active") ?: false
                            val description = document.getString("description") ?: "N/A"
                            val duration = document.getString("duration") ?: "N/A"
                            val endDate = document.getString("endDate") ?: "N/A"
                            val instructor = document.getString("instructor") ?: "N/A"
                            val materialUri = document.getString("materialUri") ?: "N/A"
                            val name = document.getString("name") ?: "N/A"
                            val startDate = document.getString("startDate") ?: "N/A"

                            // Append course details to TextView
                            courseDetailsTextView.append(
                                """
                            Course Name: $name
                            Active: $active
                            Course Code: $code
                            Description: $description
                            Duration: $duration
                            Start Date: $startDate
                            End Date: $endDate
                            Instructor: $instructor
                            Material URI: $materialUri
                            
                            """.trimIndent()
                            )
                        }
                    } else {
                        Log.e("CourseDetails", "No course found with code: $courseId")
                        courseDetailsTextView.append("\nError: Course with code $courseId not found.\n")
                    }
                }
                .addOnFailureListener { exception ->
                    Log.e("CourseDetails", "Error fetching course details: ${exception.message}")
                    courseDetailsTextView.append("\nError fetching course details: ${exception.message}\n")
                }
        }
    }

}
