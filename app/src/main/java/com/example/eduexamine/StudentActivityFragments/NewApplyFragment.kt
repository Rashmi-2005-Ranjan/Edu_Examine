package com.example.eduexamine.StudentActivityFragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.eduexamine.R

class NewApplyFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_new_apply, container, false) // Updated layout file
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val applyButton1: Button = view.findViewById(R.id.applyButton1)
        val applyButton2: Button = view.findViewById(R.id.applyButton2)
        val applyButton3: Button = view.findViewById(R.id.applyButton3)

        applyButton1.setOnClickListener {
            showToast(requireContext(), "Exam applied for ${view.findViewById<TextView>(R.id.subjectName1).text}")
        }

        applyButton2.setOnClickListener {
            showToast(requireContext(), "Exam applied for ${view.findViewById<TextView>(R.id.subjectName2).text}")
        }

        applyButton3.setOnClickListener {
            showToast(requireContext(), "Exam applied for ${view.findViewById<TextView>(R.id.subjectName3).text}")
        }
    }

    private fun showToast(context: Context, message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }
}

