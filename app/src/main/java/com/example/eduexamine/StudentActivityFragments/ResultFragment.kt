package com.example.eduexamine.StudentActivityFragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.eduexamine.R


class ResultFragment : Fragment() {

    private lateinit var scoreTextView: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        // Inflate the fragment's layout
        val view = inflater.inflate(R.layout.fragment_result, container, false)


        // Initialize the TextView
        scoreTextView = view.findViewById(R.id.scoreTextView)

        // Retrieve the passed arguments (marks)
        val scoredMarks = arguments?.getInt("scoredMarks", 0) ?: 0
        val totalMarks = arguments?.getInt("totalMarks", 0) ?: 0

        // Display the score
        scoreTextView.text = "Your Score: $scoredMarks / $totalMarks"

        return view
    }
}
