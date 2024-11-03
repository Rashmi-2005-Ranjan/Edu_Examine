package com.example.eduexamine.StudentActivityFragments

import ExamAdapter
import ExamFragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.eduexamine.Exam
import com.example.eduexamine.R
import com.google.firebase.firestore.FirebaseFirestore

class ShowExamFragment : Fragment() {
    private lateinit var recyclerViewExams: RecyclerView
    private lateinit var examAdapter: ExamAdapter
    private val db = FirebaseFirestore.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_show_exam, container, false)
        recyclerViewExams = view.findViewById(R.id.recyclerViewExams)
        recyclerViewExams.layoutManager = LinearLayoutManager(requireContext())

        loadExams()
        return view
    }

    private fun loadExams() {
        db.collection("Exams").whereEqualTo("is_available", true).get().addOnSuccessListener { documents ->
            val exams = documents.map { document ->
                Exam(
                    document.id,
                    document.getString("subject_name") ?: "",
                    document.getString("exam_date") ?: "",
                    document.getString("exam_duration") ?: ""
                )
            }
            examAdapter = ExamAdapter(exams) { examId ->
                // Navigate to ExamFragment with examId
                val examFragment = ExamFragment()
                val bundle = Bundle()
                bundle.putString("EXAM_ID", examId.toString())
                examFragment.arguments = bundle

                // Replace the current fragment with ExamFragment
                requireActivity().supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, examFragment)
                    .addToBackStack(null)
                    .commit()
            }
            recyclerViewExams.adapter = examAdapter
        }
    }
}
