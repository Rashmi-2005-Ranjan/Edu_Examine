package com.example.eduexamine.AdminActivityFragments

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.eduexamine.EditExam
import com.example.eduexamine.EditExamAdapter
import com.example.eduexamine.R
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.auth.FirebaseAuth

class StudentDetailsFragment : Fragment() {

    private lateinit var examRecyclerView: RecyclerView
    private lateinit var examAdapter: EditExamAdapter
    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    private lateinit var examList: ArrayList<EditExam>

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_student_details, container, false)

        // Initialize RecyclerView
        examRecyclerView = view.findViewById(R.id.rectangles_recycler_view)
        examRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        // Initialize the exam list
        examList = arrayListOf()

        // Fetch exams from Firestore
        fetchExams()

        return view
    }

    private fun fetchExams() {
        val currentAdminId = auth.currentUser?.email // Assuming you are using email as the admin ID

        if (currentAdminId != null) {
            db.collection("exams")
                .whereEqualTo("adminEmail", currentAdminId)
                .get()
                .addOnSuccessListener { documents ->
                    if (documents.isEmpty) {
                        // Notify user if no exams are found
                        Toast.makeText(requireContext(), "No exams found for this admin.", Toast.LENGTH_SHORT).show()
                    } else {
                        for (document in documents) {
                            val examTitle = document.getString("examTitle") ?: ""
                            val examId = document.id // or document.getString("examId") if you have it as a field
                            val examDate = document.getString("date") ?: ""

                            // Create an EditExam object and add it to the list
                            examList.add(EditExam(examTitle, examId, examDate))
                        }

                        // Initialize adapter with the fetched exams and context
                        examAdapter = EditExamAdapter(
                            examList,
                            requireContext(), // Pass the context here
                            onUpdate = { exam -> handleUpdateExam(exam) },
                            onDelete = { exam -> handleDeleteExam(exam) }
                        )

                        // Set adapter to RecyclerView
                        examRecyclerView.adapter = examAdapter

                        // Notify user about successful retrieval
                        Toast.makeText(requireContext(), "Exams retrieved successfully.", Toast.LENGTH_SHORT).show()
                    }
                }
                .addOnFailureListener { exception ->
                    // Handle the error
                    exception.printStackTrace()
                    // Notify user about the error
                    Toast.makeText(requireContext(), "Error fetching exams: ${exception.message}", Toast.LENGTH_LONG).show()
                }
        } else {
            // Notify user if admin ID is null
            Toast.makeText(requireContext(), "No admin logged in.", Toast.LENGTH_SHORT).show()
        }
    }




    // Placeholder function for handling updates
    private fun handleUpdateExam(exam: EditExam) {
        // Navigate to an update fragment or activity
        val updateExamFragment = UpdateExamFragment.newInstance(exam.examId) // Assuming you have an UpdateExamFragment that takes an EditExam object
        val transaction = requireActivity().supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragment_container, updateExamFragment) // Assuming your activity has a container for fragments
        transaction.addToBackStack(null)
        transaction.commit()
    }

    private fun handleDeleteExam(exam: EditExam) {
        // Show a confirmation dialog before deletion
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Delete Exam")
        builder.setMessage("Are you sure you want to delete this exam?")
        builder.setPositiveButton("Yes") { dialog, _ ->
            // Delete the exam from Firestore
            db.collection("exams")
                .document(exam.examId) // Use the exam ID to identify the document
                .delete()
                .addOnSuccessListener {
                    // Remove the exam from the local list
                    examList.remove(exam)
                    examAdapter.notifyDataSetChanged() // Notify the adapter about data set change
                    Toast.makeText(requireContext(), "Exam deleted successfully.", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener { exception ->
                    // Handle the error
                    Toast.makeText(requireContext(), "Error deleting exam: ${exception.message}", Toast.LENGTH_LONG).show()
                }
            dialog.dismiss()
        }
        builder.setNegativeButton("No") { dialog, _ -> dialog.dismiss() }
        builder.show()
        builder.setCancelable(false)
    }

}
