package com.example.eduexamine

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.view.menu.MenuView.ItemView
import androidx.recyclerview.widget.RecyclerView

class SetAnswerAdapter(
    private val setAns:ArrayList<SetAnswerDataClass>,
    private val context: Context,
    private val setAnsBtn:(SetAnswerDataClass)->Unit
) :RecyclerView.Adapter<SetAnswerAdapter.ViewHolderClass>(){
    class ViewHolderClass(itemView:View):RecyclerView.ViewHolder(itemView) {
        val title:TextView=itemView.findViewById(R.id.title_txrv3)
        val examId:TextView=itemView.findViewById(R.id.desc_txtv3)
        val setAnsbtn:Button=itemView.findViewById(R.id.attemptButton)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderClass {
        val itemView=LayoutInflater.from(parent.context).inflate(R.layout.item_set_ans,parent,false)
        return ViewHolderClass(itemView)
    }

    override fun getItemCount(): Int {
        return setAns.size
    }

    override fun onBindViewHolder(holder: ViewHolderClass, position: Int) {
        val currentItem=setAns[position]
        holder.title.text=currentItem.title
        holder.examId.text=currentItem.examId
        holder.setAnsbtn.setOnClickListener{
            setAnsBtn(currentItem)
        }
    }


}