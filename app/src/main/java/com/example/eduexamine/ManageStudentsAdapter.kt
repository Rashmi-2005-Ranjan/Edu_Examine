package com.example.eduexamine

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ManageStudentsAdapter(
    private val dataList: ArrayList<ManageStudents>,
    private val onUpdateClick: (ManageStudents) -> Unit,
    private val onDeleteClick: (ManageStudents) -> Unit
) : RecyclerView.Adapter<ManageStudentsAdapter.ViewHolderClass>() {

    class ViewHolderClass(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvGroupname: TextView = itemView.findViewById(R.id.title_txrv3)
        val btnUpdate: Button = itemView.findViewById(R.id.updatebtn)
        val btnDelete: Button = itemView.findViewById(R.id.deletebtn)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderClass {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_studentgroup, parent, false)
        return ViewHolderClass(itemView)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onBindViewHolder(holder: ViewHolderClass, position: Int) {
        val currentItem = dataList[position]
        holder.tvGroupname.text = currentItem.groupname

        // Set click listeners for update and delete buttons
        holder.btnUpdate.setOnClickListener {
            onUpdateClick(currentItem)
        }
        holder.btnDelete.setOnClickListener {
            onDeleteClick(currentItem)
        }
    }
}