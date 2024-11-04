package com.example.eduexamine.StudentActivityFragments // Replace with your package name

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.eduexamine.R

class NewApplyFragment: Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_exam, container, false) // Replace with your layout file
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize buttons and set click listeners
        val applyButton1: Button = view.findViewById(R.id.applyButton1)
        val applyButton2: Button = view.findViewById(R.id.applyButton2)
        val applyButton3: Button = view.findViewById(R.id.applyButton3)
        val applyButton4: Button = view.findViewById(R.id.applyButton4)
        val applyButton5: Button = view.findViewById(R.id.applyButton5)
        val applyButton6: Button = view.findViewById(R.id.applyButton6)
        val applyButton7: Button = view.findViewById(R.id.applyButton7)
        val applyButton8: Button = view.findViewById(R.id.applyButton8)
        val applyButton9: Button = view.findViewById(R.id.applyButton9)

        // Set click listeners for each button
        applyButton1.setOnClickListener { showToast(view, "Exam applied for ${view.findViewById<TextView>(R.id.subjectName1).text}") }
        applyButton2.setOnClickListener { showToast(view, "Exam applied for ${view.findViewById<TextView>(R.id.subjectName2).text}") }
        applyButton3.setOnClickListener { showToast(view, "Exam applied for ${view.findViewById<TextView>(R.id.subjectName3).text}") }
        applyButton4.setOnClickListener { showToast(view, "Exam applied for ${view.findViewById<TextView>(R.id.subjectName4).text}") }
        applyButton5.setOnClickListener { showToast(view, "Exam applied for ${view.findViewById<TextView>(R.id.subjectName5).text}") }
        applyButton6.setOnClickListener { showToast(view, "Exam applied for ${view.findViewById<TextView>(R.id.subjectName6).text}") }
        applyButton7.setOnClickListener { showToast(view, "Exam applied for ${view.findViewById<TextView>(R.id.subjectName7).text}") }
        applyButton8.setOnClickListener { showToast(view, "Exam applied for ${view.findViewById<TextView>(R.id.subjectName8).text}") }
        applyButton9.setOnClickListener { showToast(view, "Exam applied for ${view.findViewById<TextView>(R.id.subjectName9).text}") }
    }

    // Function to show a toast message
    private fun showToast(view: View, message: String) {
        Toast.makeText(view.context, message, Toast.LENGTH_SHORT).show()
    }
}
