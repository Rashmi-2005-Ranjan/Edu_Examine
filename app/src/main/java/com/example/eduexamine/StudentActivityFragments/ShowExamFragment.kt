// ShowExamFragment.kt
package com.example.eduexamine.StudentActivityFragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.eduexamine.R
import com.example.eduexamine.ShowExam
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot

class ShowExamFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ShowExamAdapter
    private lateinit var examList: ArrayList<ShowExam>
    private lateinit var db: FirebaseFirestore
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_show_exam, container, false)

        // Initialize Firebase Auth and Firestore
        db = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()

        // Initialize RecyclerView
        recyclerView = view.findViewById(R.id.rectangles_recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        // Initialize examList and set up the adapter with a lambda for the click action
        examList = arrayListOf()
        adapter = ShowExamAdapter(examList) { exam ->
            // Handle "Start" button click to navigate to ExamFragment
            navigateToExamFragment(exam.examId)
        }
        recyclerView.adapter = adapter

        // Load exams for current user
        loadExamsForCurrentUser()

        return view
    }

    private fun loadExamsForCurrentUser() {
        val currentUserEmail = auth.currentUser?.email

        if (currentUserEmail != null) {
            db.collection("exams")
                .whereArrayContains("studentEmails", currentUserEmail)
                .get()
                .addOnSuccessListener { querySnapshot: QuerySnapshot ->
                    if (!querySnapshot.isEmpty) {
                        examList.clear() // Clear existing list to avoid duplicates
                        for (document in querySnapshot.documents) {
                            val title = document.getString("examTitle") ?: "No Title"
                            val examId = document.id // Use document ID as examId
                            val startDate = document.getString("date") ?: "No Date"

                            // Create ShowExam object and add to list
                            val exam = ShowExam(title, examId, startDate)
                            examList.add(exam)
                        }
                        adapter.notifyDataSetChanged() // Refresh RecyclerView
                    } else {
                        Log.d("ShowExamFragment", "No exams available for this user.")
                    }
                }
                .addOnFailureListener { e ->
                    Log.e("ShowExamFragment", "Error fetching exams: ", e)
                }
        } else {
            Log.e("ShowExamFragment", "User email is null.")
        }
    }

    private fun navigateToExamFragment(examId: String) {
        val fragment = ExamFragment()
        val bundle = Bundle()
        bundle.putString("examId", examId)
        fragment.arguments = bundle

        // Perform the fragment transaction
        parentFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .addToBackStack(null)
            .commit()
    }
}