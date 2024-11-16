package com.example.eduexamine.StudentActivityFragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.eduexamine.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.util.Date

class ExamHistoryFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private val db = FirebaseFirestore.getInstance()
    private val examResults = mutableListOf<ExamResult>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_exam_history, container, false)
        recyclerView = view.findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = ExamHistoryAdapter(examResults)

        loadExamHistory()
        return view
    }

    private fun loadExamHistory() {
        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return

        db.collection("exam_results")
            .whereEqualTo("userId", userId)
            .get()
            .addOnSuccessListener { querySnapshot ->
                examResults.clear()
                for (document in querySnapshot.documents) {
                    val examId = document.getString("examId") ?: ""
                    val scoredMarks = document.getLong("scoredMarks")?.toInt() ?: 0
                    val totalMarks = document.getLong("totalMarks")?.toInt() ?: 0
                    val timestamp = document.getTimestamp("timestamp")?.toDate()

                    examResults.add(ExamResult(examId, scoredMarks, totalMarks, timestamp))
                }
                recyclerView.adapter?.notifyDataSetChanged()
            }
            .addOnFailureListener {
                Toast.makeText(requireContext(), "Failed to load exam history.", Toast.LENGTH_SHORT).show()
            }
    }

    data class ExamResult(
        val examId: String,
        val scoredMarks: Int,
        val totalMarks: Int,
        val timestamp: Date?
    )
}

