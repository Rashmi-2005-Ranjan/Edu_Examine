package com.example.eduexamine.StudentActivityFragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.appcompat.widget.SearchView
import com.example.eduexamine.R
import com.example.eduexamine.markAdapter
import com.example.eduexamine.marksheetDataClass
import java.util.Locale

class MarksheetFragment : Fragment(R.layout.fragment_marksheet) {

    private lateinit var recyclerView: RecyclerView
    private lateinit var daList: ArrayList<marksheetDataClass>
    private lateinit var imageList: Array<Int>
    private lateinit var titleList: Array<String>
    private lateinit var searchView: SearchView
    private lateinit var searList: ArrayList<marksheetDataClass> // Initialize this list properly

    @SuppressLint("RestrictedApi")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = view.findViewById(R.id.rectangles_recycler_view)
        searchView = view.findViewById(R.id.search)
        recyclerView.layoutManager = LinearLayoutManager(context)

        // Initialize the lists
        daList = ArrayList()
        imageList = arrayOf(R.drawable.pdf, R.drawable.pdf) // Add your image resources here
        titleList = arrayOf(
            "1st Semester Grade Sheet",
            "2nd Semester Grade Sheet",
            "3rd Semester Grade Sheet",
            "4th Semester Grade Sheet",
            "5th Semester Grade Sheet",
            "6th Semester Grade Sheet",
            "7th Semester Grade Sheet",
            "8th Semester Grade Sheet",
            "1st Internal",
            "2nd Internal",
            "3rd Internal",
            "4th Internal",
            "5th Internal",
            "6th Internal",
            "7th Internal",
            "8th Internal",
            "9th Internal",
            "10th Internal",
            "11th Internal",
            "12th Internal",
            "13th Internal",
            "14th Internal",
            "15th Internal",
            "16th Internal",
            "17th Internal",
            "18th Internal",
            "19th Internal",
            "20th Internal",
            "21st Internal",
            "22nd Internal",
            "23rd Internal",
            "24th Internal",
            "25th Internal",
            "26th Internal",
            "27th Internal",
            "28th Internal",
            "29th Internal",
            "30th Internal",
            "31st Internal",
            "32nd Internal",
        ) // Add your titles here

        // Initialize the searchable list
        searList = ArrayList()

        // Populate the data
        getData()
        val searchEditText = searchView.findViewById<androidx.appcompat.widget.SearchView.SearchAutoComplete>(
            androidx.appcompat.R.id.search_src_text
        )
        searchEditText.setTextColor(resources.getColor(R.color.black)) // Set the text color to dark
        searchEditText.setHintTextColor(resources.getColor(R.color.cyan)) // Optionally set the hint color to dark as well


        // Set up the search functionality
        searchView.clearFocus()
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                searchView.clearFocus()
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                searList.clear()
                val searchText = newText!!.toLowerCase(Locale.getDefault())
                if (searchText.isNotEmpty()) {
                    daList.forEach {
                        if (it.title.toLowerCase(Locale.getDefault()).contains(searchText)) {
                            searList.add(it)
                        }
                    }
                    recyclerView.adapter = markAdapter(searList) // Update adapter with filtered list
                    recyclerView.adapter!!.notifyDataSetChanged()
                } else {
                    searList.clear()
                    searList.addAll(daList) // Restore the full list if no search text
                    recyclerView.adapter = markAdapter(searList)
                    recyclerView.adapter!!.notifyDataSetChanged()
                }
                return true
            }
        })
    }

    private fun getData() {
        for (i in titleList.indices) {
            val data = marksheetDataClass(imageList[i % imageList.size], titleList[i])
            daList.add(data)
        }
        // Initially set the full list to the adapter
        searList.addAll(daList) // Populate search list with all items
        recyclerView.adapter = markAdapter(searList)
    }
}
