package com.example.eduexamine

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.ScrollView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.firestore.FirebaseFirestore

class NAT : AppCompatActivity() {

    private lateinit var questionEditText: TextInputEditText
    private lateinit var addQuestionButton: Button
    private lateinit var addImageButton: Button
    private lateinit var imageView: ImageView
    private var examId: String? = null
    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nat)

        // Retrieve examId from intent
        examId = intent.getStringExtra("examId")

        if (examId.isNullOrEmpty()) {
            Toast.makeText(this, "Exam ID is missing", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        // Initialize UI elements
        questionEditText = findViewById(R.id.msqQuestion)
        addQuestionButton = findViewById(R.id.button_add_question)
        addImageButton = findViewById(R.id.button_add_image)
        imageView = findViewById(R.id.imageView)

        // Set up the button click listener to save the question
        addQuestionButton.setOnClickListener {
            saveQuestion()
        }

        // Set up the button click listener for adding images
        addImageButton.setOnClickListener {
            selectImage()
        }
    }

    private fun saveQuestion() {
        val question = questionEditText.text.toString().trim()

        if (question.isEmpty()) {
            Toast.makeText(this, "Please provide a question", Toast.LENGTH_SHORT).show()
            return
        }

        // Prepare data for Firestore
        val natData = hashMapOf(
            "question" to question,
            "type" to "NAT",
            "mark" to 2 // Adjust this as needed based on your scoring logic
        )

        // Save data to Firestore in the "exam" collection's "questions" sub-collection
        db.collection("exams").document(examId!!).collection("questions").add(natData)
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
        imageView.setImageURI(null) // Clear the image view
    }

    private fun selectImage() {
        // Intent to open image gallery
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, IMAGE_PICK_CODE)
    }

    // Handle the result of image selection
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == IMAGE_PICK_CODE && resultCode == RESULT_OK && data != null) {
            val imageUri: Uri? = data.data
            imageView.setImageURI(imageUri)
            // Optionally, handle the selected image (upload to Firestore, etc.)
        }
    }

    companion object {
        const val IMAGE_PICK_CODE = 1000
    }
}
