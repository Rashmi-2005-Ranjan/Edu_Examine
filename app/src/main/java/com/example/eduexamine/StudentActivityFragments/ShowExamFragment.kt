package com.example.eduexamine.StudentActivityFragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.eduexamine.R

class
ShowExamFragment : Fragment() {

    private lateinit var listView: ListView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_show_exam, container, false)
//        listView = view.findViewById(R.id.lvTopics)

        val topics = listOf("Mathematics", "Science", "History")
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, topics)
        listView.adapter = adapter
//
//        listView.setOnItemClickListener { _, _, position, _ ->
//            val topicId = (position + 1).toString()  // Convert to String to match argument type
//            val action = ShowExamFragmentDirections.actionShowExamFragmentToExamFragment(topicId)
//            findNavController().navigate(action)
//        }

        return view
    }
}
