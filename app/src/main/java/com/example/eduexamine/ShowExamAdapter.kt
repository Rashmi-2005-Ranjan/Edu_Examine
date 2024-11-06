package com.example.eduexamine.StudentActivityFragments

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.eduexamine.R
import com.example.eduexamine.ShowExam

class ShowExamAdapter(private val dataList:ArrayList<ShowExam>):RecyclerView.Adapter<ShowExamAdapter.ViewHolderClass>(){

        class ViewHolderClass(itemView: View):RecyclerView.ViewHolder(itemView){
            val title: TextView = itemView.findViewById(R.id.title_txrv3)
            val date: TextView = itemView.findViewById(R.id.desc_txtv4)
            val examId: TextView = itemView.findViewById(R.id.desc_txtv3)
            val btnStart: Button = itemView.findViewById(R.id.attemptButton)
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderClass {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_show_exam,parent,false)
        return ViewHolderClass(itemView)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onBindViewHolder(holder: ViewHolderClass, position: Int) {
        val currentItem = dataList[position]
        holder.title.text = currentItem.title
        holder.date.text = currentItem.date
        holder.examId.text = currentItem.examId
        holder.btnStart.setOnClickListener {

        }
    }


}