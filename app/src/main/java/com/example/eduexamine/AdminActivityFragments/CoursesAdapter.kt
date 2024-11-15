package com.example.eduexamine.AdminActivityFragments

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.eduexamine.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore

class CoursesAdapter(private val coursesList: List<AddCourseFragment.Course>) : RecyclerView.Adapter<CoursesAdapter.CourseViewHolder>() {

    inner class CourseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val courseName: TextView = itemView.findViewById(R.id.courseNameTextView)
        val courseCode: TextView = itemView.findViewById(R.id.courseCodeTextView)
        val courseDescription: TextView = itemView.findViewById(R.id.courseDescriptionTextView)
        val courseDuration: TextView = itemView.findViewById(R.id.courseDurationTextView)
        val courseInstructor: TextView = itemView.findViewById(R.id.courseTeacherTextView)
        val courseStartDate: TextView = itemView.findViewById(R.id.StartDateTextView)
        val courseEndDate: TextView = itemView.findViewById(R.id.EndDateTextView)
        val groupSpinner: Spinner = itemView.findViewById(R.id.groupSpinner)
        val sendToGroupButton: Button = itemView.findViewById(R.id.sendToGroupButton)

        fun bind(course: AddCourseFragment.Course) {
            courseName.text = course.name
            courseCode.text = course.code
            courseDescription.text = course.description
            courseDuration.text = course.duration
            courseInstructor.text = course.instructor
            courseStartDate.text = course.startDate
            courseEndDate.text = course.endDate

            val spinnerItems = listOf(
                "Select Group", "B.Tech", "B.E", "M.Tech", "M.E", "BCA", "MCA", "B.Sc",
                "M.Sc", "B.Com", "M.Com", "B.A", "M.A", "BBA", "MBA", "BDS", "MBBS",
                "B.Pharm", "M.Pharm", "B.Arch", "M.Arch", "BHM", "MHM", "BBA", "MBA",
                "BBA LLB", "LLB", "LLM", "B.Ed", "M.Ed", "B.P.Ed", "M.P.Ed", "B.Lib",
                "M.Lib", "BDS", "MDS", "BHMS", "DHMS", "BUMS", "BAMS", "BNYS", "BPT",
                "MPT", "BOT", "Class Group"
            )

            val adapter = ArrayAdapter(
                itemView.context,
                android.R.layout.simple_spinner_item, spinnerItems
            )
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            groupSpinner.adapter = adapter

            sendToGroupButton.setOnClickListener {
                sendCourseToGroup(course)
            }
        }

        private fun sendCourseToGroup(course: AddCourseFragment.Course) {
            val selectedGroup = groupSpinner.selectedItem.toString()
            if (selectedGroup == "Select Group") {
                Toast.makeText(itemView.context, "Please select a group", Toast.LENGTH_SHORT).show()
                return
            }

            val currentAdminEmail = FirebaseAuth.getInstance().currentUser?.email
            if (currentAdminEmail == null) {
                Toast.makeText(itemView.context, "User is not logged in", Toast.LENGTH_SHORT).show()
                return
            }

            FirebaseFirestore.getInstance().collection("groups")
                .document(currentAdminEmail)
                .collection("studentGroups")
                .document(selectedGroup)
                .update("courses", FieldValue.arrayUnion(course.code))
                .addOnSuccessListener {
                    Toast.makeText(itemView.context, "Course sent to $selectedGroup group", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener { e ->
                    Toast.makeText(itemView.context, "Error sending course: ${e.message}", Toast.LENGTH_SHORT).show()
                }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CourseViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.course_item_layout, parent, false)
        return CourseViewHolder(view)
    }

    override fun onBindViewHolder(holder: CourseViewHolder, position: Int) {
        val course = coursesList[position]
        holder.bind(course)
    }

    override fun getItemCount() = coursesList.size
}