package com.example.eduexamine

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.firestore.FirebaseFirestore

class NAT : AppCompatActivity() {

    private lateinit var questionEditText: TextInputEditText
    private lateinit var addQuestionButton: Button
    private lateinit var addImageButton: Button
    private lateinit var imageView: ImageView

    // Initialize Firestore
    private val db = FirebaseFirestore.getInstance()

    // Constants for image selection
    private val IMAGE_PICK_CODE = 1000
    private var imageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nat)

        // Initialize UI elements
        questionEditText = findViewById(R.id.msqQuestion)
        addQuestionButton = findViewById(R.id.button_add_question)
        addImageButton = findViewById(R.id.button_add_image)
        imageView = findViewById(R.id.imageView)

        // Set up the button click listener to save the question
        addQuestionButton.setOnClickListener {
            saveQuestion()
        }

        // Set up the button click listener to add an image
        addImageButton.setOnClickListener {
            selectImage()
        }
    }

    private fun saveQuestion() {
        // Gather input data
        val question = questionEditText.text.toString().trim()

        // Validate inputs
        if (question.isEmpty()) {
            Toast.makeText(this, "Please enter a question", Toast.LENGTH_SHORT).show()
            return
        }

        // Prepare data for Firestore
        val natData = hashMapOf(
            "question" to question,
            "type" to "NAT"
        )

        // Save data to Firestore
        db.collection("questions").add(natData)
            .addOnSuccessListener {
                Toast.makeText(this, "Question saved successfully!", Toast.LENGTH_SHORT).show()
                clearFields()
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Error saving question: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun clearFields() {
        questionEditText.text?.clear()
        imageView.setImageURI(null) // Clear the image display if needed
        imageUri = null // Clear the image URI
    }

    private fun selectImage() {
        // Intent to open gallery
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, IMAGE_PICK_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == IMAGE_PICK_CODE && resultCode == RESULT_OK && data != null) {
            imageUri = data.data // Get the image URI
            imageView.setImageURI(imageUri) // Set the image in ImageView
        }
    }
}