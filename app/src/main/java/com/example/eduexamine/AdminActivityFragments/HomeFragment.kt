package com.example.eduexamine.AdminActivityFragments

import android.animation.ObjectAnimator
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.eduexamine.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage

class HomeFragment : Fragment() {

    private lateinit var firestore: FirebaseFirestore
    private lateinit var auth: FirebaseAuth
    private lateinit var storageRef: FirebaseStorage
    private lateinit var welcomeTextView: TextView
    private lateinit var profileImageView: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        firestore = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()
        storageRef = FirebaseStorage.getInstance()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home2, container, false)

        // Initialize the views
        welcomeTextView = view.findViewById(R.id.welcome_text)
        profileImageView = view.findViewById(R.id.student_profile_image)

        // Load admin basic information
        loadAdminInfo()

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Animate the welcome text
        welcomeTextView.post {
            val startY = -welcomeTextView.height.toFloat()
            val endY = welcomeTextView.y
            welcomeTextView.y = startY

            val animator = ObjectAnimator.ofFloat(welcomeTextView, "y", startY, endY)
            animator.duration = 1000 // 1 second
            animator.interpolator = AccelerateDecelerateInterpolator()
            animator.start()
        }

        // Set up click listeners for buttons
        view.findViewById<Button>(R.id.btn_upcoming_exams).setOnClickListener {
            // Handle Upcoming Exams button click
        }

        view.findViewById<Button>(R.id.btn_results).setOnClickListener {
            // Handle Results button click
        }

        view.findViewById<Button>(R.id.btn_marksheet).setOnClickListener {
            // Handle Marksheet button click
        }

        view.findViewById<Button>(R.id.btn_learning).setOnClickListener {
            // Handle Learning Resources button click
        }
    }

    private fun loadAdminInfo() {
        val email = auth.currentUser?.email ?: return
        val documentId = email.replace("_", ".")

        firestore.collection("adminBasicInfo").document(documentId)
            .get()
            .addOnSuccessListener { document ->
                if (document != null && document.exists()) {
                    val fullName = document.getString("fullName") ?: "Admin"
                    welcomeTextView.text = "Hello, $fullName"
                    loadProfileImage() // Load profile image after admin info
                } else {
                    Toast.makeText(requireContext(), "No data found for this admin", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener { e ->
                Toast.makeText(requireContext(), "Error retrieving information: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun loadProfileImage() {
        val email = auth.currentUser?.email ?: return
        val documentId = email.replace("_", ".")

        val profileRef = storageRef.reference.child("images/$documentId/profile.jpg")
        profileRef.downloadUrl.addOnSuccessListener { uri ->
            Glide.with(this).load(uri).into(profileImageView) // Load image using Glide
        }.addOnFailureListener {
            Toast.makeText(requireContext(), "Profile image not found.", Toast.LENGTH_SHORT).show()
        }
    }
}
