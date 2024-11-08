package com.example.eduexamine.AdminActivityFragments

import StudentAdapter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.eduexamine.Group
import com.example.eduexamine.R
import com.example.eduexamine.Student

class GroupAdapter(
    private val groups: List<Group>,
    private val onUpdate: (Student) -> Unit,
    private val onDelete: (Student) -> Unit
) : RecyclerView.Adapter<GroupAdapter.GroupViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GroupViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_group, parent, false)
        return GroupViewHolder(view)
    }

    override fun onBindViewHolder(holder: GroupViewHolder, position: Int) {
        val group = groups[position]
        holder.bind(group)
    }

    override fun getItemCount(): Int {
        return groups.size
    }

    inner class GroupViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val groupNameTextView: TextView = itemView.findViewById(R.id.groupNameTextView)
       // private val studentsRecyclerView: RecyclerView = itemView.findViewById(R.id.studentsRecyclerView)

        fun bind(group: Group) {
            groupNameTextView.text = group.groupName
            val studentAdapter = StudentAdapter(group.students, onUpdate, onDelete)
//            studentsRecyclerView.layoutManager = LinearLayoutManager(itemView.context)
//            studentsRecyclerView.adapter = studentAdapter
        }
    }
}
