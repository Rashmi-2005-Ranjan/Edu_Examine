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

        fetchCourseDetails(courseDetailsTextView)

        return view
    }

    @SuppressLint("SetTextI18n")
    private fun fetchCourseDetails(courseDetailsTextView: TextView) {
        courseIds?.forEach { courseId ->
            db.collection("courses")
                .document(courseId)
                .get()
                .addOnSuccessListener { document ->
                    if (document.exists()) {
                        val name = document.getString("name") ?: "N/A"
                        val code = document.getString("code") ?: "N/A"
                        val instructor = document.getString("instructor") ?: "N/A"
                        val duration = document.getString("duration") ?: "N/A"

                        courseDetailsTextView.append(
                            "\nCourse Name: $name\nCourse Code: $code\nInstructor: $instructor\nDuration: $duration\n\n"
                        )
                    } else {
                        Log.e("CourseDetails", "Course ID $courseId does not exist.")
                        courseDetailsTextView.append("\nError: Course ID $courseId not found\n")
                    }
                }
                .addOnFailureListener { exception ->
                    Log.e("CourseDetails", "Error fetching course ID $courseId: ${exception.message}")
                    courseDetailsTextView.append("\nError fetching course details: ${exception.message}\n")
                }
        }
    }

}
