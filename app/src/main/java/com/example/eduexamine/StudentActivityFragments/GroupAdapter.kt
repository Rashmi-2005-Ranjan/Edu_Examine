package com.example.eduexamine.StudentActivityFragments

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.eduexamine.R

data class Group(
    val groupName: String,
    val courses: List<String>,
    val students: List<Student>,
    var isSelected: Boolean = false // Tracks whether this group is selected
)
data class Student(val email: String, val registrationNumber: String)
data class Course(val courseName: String)

class GroupAdapter(private val groupList: List<Group>) : RecyclerView.Adapter<GroupAdapter.GroupViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GroupViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_student_course, parent, false)
        return GroupViewHolder(view)
    }

    override fun onBindViewHolder(holder: GroupViewHolder, position: Int) {
        holder.bind(groupList[position])
    }

    override fun getItemCount(): Int = groupList.size

    class GroupViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val groupName: TextView = itemView.findViewById(R.id.groupName)
        private val courses: TextView = itemView.findViewById(R.id.courses)
        private val students: TextView = itemView.findViewById(R.id.students)
        private val selectCheckBox: CheckBox = itemView.findViewById(R.id.selectCheckBox)

        @SuppressLint("SetTextI18n")
        fun bind(group: Group) {
            groupName.text = group.groupName
            courses.text = "Courses: ${group.courses.joinToString(", ")}"
            students.text = "Students: ${group.students.joinToString(", ") { it.email }}"

            // Set checkbox state and listener
            selectCheckBox.isChecked = group.isSelected
            selectCheckBox.setOnCheckedChangeListener { _, isChecked ->
                group.isSelected = isChecked
            }
        }
    }
}