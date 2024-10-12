package com.example.eduexamine

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.eduexamine.databinding.ActivityAcademicInfoStudentBinding

class AcademicInfoStudent : AppCompatActivity() {

    private lateinit var binding: ActivityAcademicInfoStudentBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAcademicInfoStudentBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Handle save button click
        binding.saveButton.setOnClickListener {
            saveInfo()
        }
    }

    private fun saveInfo() {
        val specialization = binding.specialization.text.toString()
        val certifications = binding.Certifications.text.toString()
        val projectTitle = binding.projectTitle.text.toString()
        val projectLink = binding.projectLink.text.toString()
        val companyName = binding.CompanyName.text.toString()
        val duration = binding.Duration.text.toString()

        if (validateInputs(specialization, certifications, projectTitle, projectLink, companyName, duration)) {
            // Display success message
            Toast.makeText(this, "Information saved successfully!", Toast.LENGTH_SHORT).show()
        } else {
            // Display error message if any field is empty
            Toast.makeText(this, "Please fill all required fields", Toast.LENGTH_SHORT).show()
        }
    }

    private fun validateInputs(
        specialization: String, certifications: String,
        projectTitle: String, projectLink: String,
        companyName: String, duration: String
    ): Boolean {
        return specialization.isNotEmpty() && certifications.isNotEmpty() &&
                projectTitle.isNotEmpty() && projectLink.isNotEmpty() &&
                companyName.isNotEmpty() && duration.isNotEmpty()
    }
}
