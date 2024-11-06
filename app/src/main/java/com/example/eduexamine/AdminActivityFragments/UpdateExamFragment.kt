package com.example.eduexamine.AdminActivityFragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.eduexamine.R
import com.google.firebase.firestore.FirebaseFirestore

class UpdateExamFragment : Fragment() {

    private lateinit var examId: String
    private lateinit var db: FirebaseFirestore
    private lateinit var examTitleEditText: EditText
    private lateinit var examCodeEditText: EditText
    private lateinit var selectedStartTimeTextView: TextView
    private lateinit var selectedEndTimeTextView: TextView
    private lateinit var selectDateTextView: TextView
    private lateinit var saveButton: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_update_exam, container, false)

        // Initialize UI elements
        examTitleEditText = view.findViewById(R.id.examtitle)
        examCodeEditText = view.findViewById(R.id.examcode)
        selectedStartTimeTextView = view.findViewById(R.id.selectedStartTimeTextView)
        selectedEndTimeTextView = view.findViewById(R.id.selectedEndTimeTextView)
        selectDateTextView = view.findViewById(R.id.selectdate)
        saveButton = view.findViewById(R.id.scheduleExamButton)

        // Initialize Firestore
        db = FirebaseFirestore.getInstance()

        // Retrieve examId from arguments
        examId = arguments?.getString("examId") ?: ""

        // Load exam details
        loadExamDetails()

        // Set click listener for save button
        saveButton.setOnClickListener {
            updateExamDetails()
        }

        return view
    }

    private fun loadExamDetails() {
        Log.d("UpdateExamFragment", "Exam ID: $examId")  // Add this line to log the exam ID
        if (examId.isNotEmpty()) {
            db.collection("exams").document(examId).get()
                .addOnSuccessListener { document ->
                    if (document.exists()) {
                        val examTitle = document.getString("examTitle") ?: "N/A"
                        val examCode = document.getString("examCode") ?: "N/A"
                        val startTime = document.getString("startTime") ?: "N/A"
                        val endTime = document.getString("endTime") ?: "N/A"
                        val date = document.getString("date") ?: "N/A"

                        examTitleEditText.setText(examTitle)
                        examCodeEditText.setText(examCode)
                        selectedStartTimeTextView.text = "Start Time: $startTime"
                        selectedEndTimeTextView.text = "End Time: $endTime"
                        selectDateTextView.text = date

                        Log.d("UpdateExamFragment", "Exam details loaded: $examTitle, $examCode, $startTime, $endTime, $date")
                    } else {
                        Toast.makeText(requireContext(), "Exam not found.", Toast.LENGTH_SHORT).show()
                    }
                }
                .addOnFailureListener { exception ->
                    Toast.makeText(requireContext(), "Error loading exam details: ${exception.message}", Toast.LENGTH_LONG).show()
                }
        } else {
            Toast.makeText(requireContext(), "Invalid exam ID.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun updateExamDetails() {
        val updatedTitle = examTitleEditText.text.toString()
        val updatedCode = examCodeEditText.text.toString()
        val updatedStartTime = selectedStartTimeTextView.text.toString().replace("Start Time: ", "")
        val updatedEndTime = selectedEndTimeTextView.text.toString().replace("End Time: ", "")
        val updatedDate = selectDateTextView.text.toString()

        // Validation to ensure fields are not empty
        if (updatedTitle.isBlank() || updatedCode.isBlank() || updatedDate.isBlank()) {
            Toast.makeText(requireContext(), "Please fill in all fields.", Toast.LENGTH_SHORT).show()
            return
        }

        // Create a map with the updated values
        val updatedData = mapOf(
            "examTitle" to updatedTitle,
            "examCode" to updatedCode,
            "startTime" to updatedStartTime,
            "endTime" to updatedEndTime,
            "date" to updatedDate
        )

        // Update Firestore document
        db.collection("exams").document(examId).update(updatedData)
            .addOnSuccessListener {
                Toast.makeText(requireContext(), "Exam updated successfully", Toast.LENGTH_SHORT).show()
                Log.d("UpdateExamFragment", "Exam updated successfully with ID: $examId")
            }
            .addOnFailureListener { e ->
                Toast.makeText(requireContext(), "Error updating exam: ${e.message}", Toast.LENGTH_LONG).show()
                Log.e("UpdateExamFragment", "Error updating exam", e)
            }
    }

    companion object {
        fun newInstance(examId: String): UpdateExamFragment {
            val fragment = UpdateExamFragment()
            val args = Bundle()
            args.putString("examId", examId)
            fragment.arguments = args
            return fragment
        }
    }
}
