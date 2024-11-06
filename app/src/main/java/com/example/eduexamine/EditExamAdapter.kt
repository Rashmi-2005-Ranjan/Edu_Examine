package com.example.eduexamine

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView

class EditExamAdapter(
    private val editExam: ArrayList<EditExam>,
    private val context: Context, // Add context parameter for Toast
    private val onUpdate: (EditExam) -> Unit,
    private val onDelete: (EditExam) -> Unit
) : RecyclerView.Adapter<EditExamAdapter.ViewHolderClass>() {

    class ViewHolderClass(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.findViewById(R.id.title_txrv3)
        val examId: TextView = itemView.findViewById(R.id.desc_txtv3)
        val date: TextView = itemView.findViewById(R.id.desc_txtv4)
        val update: Button = itemView.findViewById(R.id.updatebtn)
        val delete: Button = itemView.findViewById(R.id.deletebtn)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderClass {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_exam, parent, false)
        return ViewHolderClass(itemView)
    }

    override fun getItemCount(): Int {
        return editExam.size
    }

    override fun onBindViewHolder(holder: ViewHolderClass, position: Int) {
        val currentItem = editExam[position]
        holder.title.text = currentItem.title
        holder.examId.text = currentItem.examId
        holder.date.text = currentItem.date
        holder.update.setOnClickListener {
            onUpdate(currentItem)
        }
        holder.delete.setOnClickListener {
            onDelete(currentItem) }
    }
}
