package com.example.eduexamine.AdminActivityFragments

import android.animation.ObjectAnimator
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.eduexamine.R

class HomeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home2, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Find the welcome text view
        val welcomeText: TextView = view.findViewById(R.id.welcome_text)

        // Animate the welcome text
        welcomeText.post {
            val startY = -welcomeText.height.toFloat()
            val endY = welcomeText.y
            welcomeText.y = startY

            val animator = ObjectAnimator.ofFloat(welcomeText, "y", startY, endY)
            animator.duration = 1000 // 1 second
            animator.interpolator = AccelerateDecelerateInterpolator()
            animator.start()
        }

        // Set up click listeners for buttons
        view.findViewById<Button>(R.id.btn_upcoming_exams).setOnClickListener {
            // Handle Add courses button click
        }

        view.findViewById<Button>(R.id.btn_results).setOnClickListener {
            // Handle Manage Exams button click
        }

        view.findViewById<Button>(R.id.btn_marksheet).setOnClickListener {
            // Handle Account Settings button click
        }

        view.findViewById<Button>(R.id.btn_learning).setOnClickListener {
            // Handle Set Answer Sheet button click
        }
    }
}