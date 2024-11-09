package com.example.eduexamine.AdminActivityFragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.eduexamine.R
import com.example.eduexamine.SetAnswerAdapter
import com.example.eduexamine.SetAnswerDataClass
import com.example.eduexamine.SetQuestionAnswerActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class SetAnswerFragment : Fragment() {
    private lateinit var setAnsRecyclerView: RecyclerView
    private lateinit var setAnswerAdapter: SetAnswerAdapter
    private lateinit var setAnsList: ArrayList<SetAnswerDataClass>
    private val db = FirebaseFirestore.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment and assign it to a variable
        val view = inflater.inflate(R.layout.fragment_set_answer, container, false)

        // Initialize RecyclerView
        setAnsRecyclerView = view.findViewById(R.id.rectangles_recycler_view)
        setAnsRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        // Initialize the exam list
        setAnsList = arrayListOf()

        // Fetch exams scheduled by the current admin's email
        val currentAdminEmail = FirebaseAuth.getInstance().currentUser?.email
        currentAdminEmail?.let { email ->
            db.collection("exams")
                .whereEqualTo("adminEmail", email)
                .get()
                .addOnSuccessListener { documents ->
                    for (document in documents) {
                        val examName = document.getString("examTitle") ?: "N/A"
                        val examId = document.id
                        setAnsList.add(SetAnswerDataClass(examName, examId))
                    }
                    // Notify the adapter that data has changed
                    setAnswerAdapter.notifyDataSetChanged()
                }
                .addOnFailureListener {
                    // Handle any errors here
                }
        }

        // Initialize Adapter and set it to RecyclerView
        setAnswerAdapter = SetAnswerAdapter(setAnsList, requireContext()) { selectedItem ->
            handleButtonClick(selectedItem)
        }
        setAnsRecyclerView.adapter = setAnswerAdapter

        return view
    }

    private fun handleButtonClick(item: SetAnswerDataClass) {
        val intent = Intent(requireContext(), SetQuestionAnswerActivity::class.java)
        intent.putExtra("EXAM_ID", item.examId)
        startActivity(intent)
    }

}
