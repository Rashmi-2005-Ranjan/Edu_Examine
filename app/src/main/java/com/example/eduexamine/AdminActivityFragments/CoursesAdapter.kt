package com.example.eduexamine.AdminActivityFragments


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.eduexamine.R


class CoursesAdapter(private val coursesList: List<AddCourseFragment.Course>) : RecyclerView.Adapter<CoursesAdapter.CourseViewHolder>() {

    inner class CourseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val courseName: TextView = itemView.findViewById(R.id.courseNameTextView)
        val courseCode: TextView = itemView.findViewById(R.id.courseCodeTextView)
        val courseDescription: TextView = itemView.findViewById(R.id.courseDescriptionTextView)
        val courseDuration: TextView = itemView.findViewById(R.id.courseDurationTextView)
        val courseInstructor: TextView = itemView.findViewById(R.id.courseTeacherTextView)
        val courseStartDate: TextView = itemView.findViewById(R.id.StartDateTextView)
        val courseEndDate: TextView = itemView.findViewById(R.id.EndDateTextView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CourseViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.course_item_layout, parent, false)
        return CourseViewHolder(view)
    }

    override fun onBindViewHolder(holder: CourseViewHolder, position: Int) {
        val course = coursesList[position]
        holder.courseName.text = course.name
        holder.courseCode.text = course.code
        holder.courseDescription.text = course.description
        holder.courseDuration.text = course.duration
        holder.courseInstructor.text = course.instructor
        holder.courseStartDate.text = course.startDate
        holder.courseEndDate.text = course.endDate
    }

    override fun getItemCount() = coursesList.size
}