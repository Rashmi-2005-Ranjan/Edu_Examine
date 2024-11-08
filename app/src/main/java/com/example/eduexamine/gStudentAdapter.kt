package com.example.eduexamine

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class gStudentAdapter(
    private val studentList: ArrayList<gStudent>, // Using the gStudent data class here
    private val onDeleteClick: (gStudent) -> Unit
) : RecyclerView.Adapter<gStudentAdapter.StudentViewHolder>() {

    // ViewHolder for binding data to each item in the list
    class StudentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvRegistrationNo: TextView = itemView.findViewById(R.id.title_txrv3)
        val tvEmail: TextView = itemView.findViewById(R.id.desc_txtv3)
        val btnDelete: Button = itemView.findViewById(R.id.deletebtn)
    }

    // Inflate the layout for individual student items
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StudentViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_update_gr_std, parent, false)
        return StudentViewHolder(itemView)
    }

    // Bind student data to each item
    override fun onBindViewHolder(holder: StudentViewHolder, position: Int) {
        val student = studentList[position]
        holder.tvRegistrationNo.text = student.registrationNo
        holder.tvEmail.text = student.email

        // Set up delete button click listener
        holder.btnDelete.setOnClickListener {
            onDeleteClick(student) // Call the function to delete the student
        }
    }

    // Return the size of the student list
    override fun getItemCount(): Int = studentList.size
}