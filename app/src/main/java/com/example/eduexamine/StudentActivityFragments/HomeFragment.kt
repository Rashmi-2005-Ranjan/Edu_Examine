package com.example.eduexamine.StudentActivityFragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
        storageRef = FirebaseStorage.getInstance() // Initialize Firebase Storage reference
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        // Initialize the views
        welcomeTextView = view.findViewById(R.id.welcome_text)
        profileImageView = view.findViewById(R.id.student_profile_image) // ImageView for profile image

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Load user basic information
        loadUserInfo()
    }

    private fun loadUserInfo() {
        val email = auth.currentUser?.email ?: return
        val documentId = email.replace("_", ".") // Use consistent format for Firebase path

        firestore.collection("studentBasicInfo").document(documentId)
            .get()
            .addOnSuccessListener { document ->
                if (document != null && document.exists()) {
                    val fullName = document.getString("fullName") ?: "User Name"
                    welcomeTextView.text = "Welcome, $fullName"
                    loadProfileImage() // Load profile image after user info
                } else {
                    Toast.makeText(requireContext(), "No data found for this user", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener { e ->
                Toast.makeText(requireContext(), "Error retrieving information: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun loadProfileImage() {
        val email = auth.currentUser?.email ?: return
        val documentId = email.replace("_", ".") // Use consistent format for Firebase path

        val profileRef = storageRef.reference.child("images/$documentId/profile.jpg")
        profileRef.downloadUrl.addOnSuccessListener { uri ->
            if (isAdded) { // Ensure fragment is attached before loading image
                Glide.with(this) // Use viewLifecycleOwner instead of `this`
                    .load(uri)
                    .into(profileImageView)
            }
        }.addOnFailureListener {
            Toast.makeText(requireContext(), "Profile image not found.", Toast.LENGTH_SHORT).show()
        }
    }
}
