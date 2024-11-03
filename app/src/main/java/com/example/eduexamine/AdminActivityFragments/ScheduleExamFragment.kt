package com.example.eduexamine.AdminActivityFragments

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.eduexamine.MCQ
import com.example.eduexamine.MSQ
import com.example.eduexamine.NAT
import com.example.eduexamine.R
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.util.Calendar

class ScheduleExamFragment : Fragment() {

    private lateinit var examTitleLayout: TextInputLayout
    private lateinit var examCodeLayout: TextInputLayout
    private lateinit var eligibilityLayout: TextInputLayout
    private lateinit var examTitleEditText: TextInputEditText
    private lateinit var examCodeEditText: TextInputEditText
    private lateinit var eligibilityEditText: TextInputEditText
    private lateinit var selectedStartTimeTextView: TextView
    private lateinit var selectedEndTimeTextView: TextView
    private lateinit var selectDateTextView: TextView
    private lateinit var addQuestionTextView: TextView
    private lateinit var scheduleExamButton: Button

    // Initialize Firestore instance and FirebaseAuth
    private val db = FirebaseFirestore.getInstance()
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.fragment_schedule_exam, container, false)

        // Initialize UI elements
        examTitleLayout = view.findViewById(R.id.material1)
        examCodeLayout = view.findViewById(R.id.material2)
        eligibilityLayout = view.findViewById(R.id.material3)
        examTitleEditText = view.findViewById(R.id.examtitle)
        examCodeEditText = view.findViewById(R.id.examcode)
        eligibilityEditText = view.findViewById(R.id.eligibility)
        selectedStartTimeTextView = view.findViewById(R.id.selectedStartTimeTextView)
        selectedEndTimeTextView = view.findViewById(R.id.selectedEndTimeTextView)
        selectDateTextView = view.findViewById(R.id.selectdate)
        addQuestionTextView = view.findViewById(R.id.addquestion)
        scheduleExamButton = view.findViewById(R.id.scheduleExamButton)

        setupOnClickListeners()

        return view
    }

    private fun setupOnClickListeners() {
        selectedStartTimeTextView.setOnClickListener { showTimePickerDialog(true) }
        selectedEndTimeTextView.setOnClickListener { showTimePickerDialog(false) }
        selectDateTextView.setOnClickListener { showDatePickerDialog() }
        addQuestionTextView.setOnClickListener { addQuestion() }
        scheduleExamButton.setOnClickListener { scheduleExam() }
    }

    private fun showTimePickerDialog(isStartTime: Boolean) {
        val calendar = Calendar.getInstance()
        val hour = calendar[Calendar.HOUR_OF_DAY]
        val minute = calendar[Calendar.MINUTE]

        val timePickerDialog = TimePickerDialog(requireActivity(),
            { _, selectedHour, selectedMinute ->
                val time = String.format("%02d:%02d", selectedHour, selectedMinute)
                if (isStartTime) {
                    selectedStartTimeTextView.text = time
                } else {
                    selectedEndTimeTextView.text = time
                }
            }, hour, minute, true
        )

        timePickerDialog.show()
    }

    private fun showDatePickerDialog() {
        val calendar = Calendar.getInstance()
        val year = calendar[Calendar.YEAR]
        val month = calendar[Calendar.MONTH]
        val day = calendar[Calendar.DAY_OF_MONTH]

        val datePickerDialog = DatePickerDialog(
            requireActivity(),
            { _, selectedYear, selectedMonth, selectedDay ->
                val date = "$selectedDay/${selectedMonth + 1}/$selectedYear"
                selectDateTextView.text = date
            }, year, month, day
        )

        datePickerDialog.show()
    }

    private fun addQuestion() {
        val options = arrayOf("MCQ", "MSQ", "NAT")

        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Select Question Type")
        builder.setItems(options) { _, which ->
            when (which) {
                0 -> startActivity(Intent(activity, MCQ::class.java))
                1 -> startActivity(Intent(activity, MSQ::class.java))
                2 -> startActivity(Intent(activity, NAT::class.java))
            }
        }
        builder.show()
    }

    private fun scheduleExam() {
        val examTitle = examTitleEditText.text.toString().trim()
        val examCode = examCodeEditText.text.toString().trim()
        val eligibility = eligibilityEditText.text.toString().trim()
        val startTime = selectedStartTimeTextView.text.toString()
        val endTime = selectedEndTimeTextView.text.toString()
        val date = selectDateTextView.text.toString()

        // Validate inputs
        if (examTitle.isEmpty()) {
            examTitleLayout.error = "Exam Title is required"
            return
        } else {
            examTitleLayout.error = null
        }

        if (examCode.isEmpty()) {
            examCodeLayout.error = "Exam Code is required"
            return
        } else {
            examCodeLayout.error = null
        }

        if (eligibility.isEmpty()) {
            eligibilityLayout.error = "Eligibility is required"
            return
        } else {
            eligibilityLayout.error = null
        }

        if (startTime == "Select Start Time" || endTime == "Select End Time" || date == "Select Date") {
            Toast.makeText(
                requireContext(),
                "Please select a valid start time, end time, and date.",
                Toast.LENGTH_SHORT
            ).show()
            return
        }

        // Get the current user's email to use for the exam data
        val currentUserEmail = auth.currentUser?.email ?: return

        // Prepare data for Firestore
        val examData = hashMapOf(
            "examTitle" to examTitle,
            "examCode" to examCode,
            "eligibility" to eligibility,
            "startTime" to startTime,
            "endTime" to endTime,
            "date" to date,
            "adminEmail" to currentUserEmail // Optionally store the admin's email
        )

        // Save exam details as a new document in the "exams" collection
        db.collection("exams").add(examData)
            .addOnSuccessListener { documentReference ->
                Toast.makeText(requireContext(), "Exam scheduled successfully!", Toast.LENGTH_SHORT).show()
                fetchQuestionsForExam(documentReference.id, eligibility) // Pass the exam ID and eligibility group
                clearFields()
            }
            .addOnFailureListener { e ->
                Toast.makeText(
                    requireContext(),
                    "Error scheduling exam: ${e.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
    }

    private fun fetchQuestionsForExam(examId: String, eligibilityGroup: String) {
        val questionsList = mutableListOf<Map<String, Any>>()

        // Fetch MCQ questions
        db.collection("questions")
            .whereEqualTo("type", "MCQ")
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    questionsList.add(document.data)
                }
                // After fetching MCQ questions, fetch MSQ questions
                fetchMSQQuestions(questionsList, examId, eligibilityGroup)
            }
            .addOnFailureListener { e ->
                Toast.makeText(
                    requireContext(),
                    "Error fetching MCQ questions: ${e.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
    }

    private fun fetchMSQQuestions(questionsList: MutableList<Map<String, Any>>, examId: String, eligibilityGroup: String) {
        db.collection("questions")
            .whereEqualTo("type", "MSQ")
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    questionsList.add(document.data)
                }
                // After fetching MSQ questions, fetch NAT questions
                fetchNATQuestions(questionsList, examId, eligibilityGroup)
            }
            .addOnFailureListener { e ->
                Toast.makeText(
                    requireContext(),
                    "Error fetching MSQ questions: ${e.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
    }

    private fun fetchNATQuestions(questionsList: MutableList<Map<String, Any>>, examId: String, eligibilityGroup: String) {
        db.collection("questions")
            .whereEqualTo("type", "NAT")
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    questionsList.add(document.data)
                }
                // Save the complete questions list to the exam document
                saveQuestionsToExam(questionsList, examId, eligibilityGroup)
            }
            .addOnFailureListener { e ->
                Toast.makeText(
                    requireContext(),
                    "Error fetching NAT questions: ${e.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
    }

    private fun saveQuestionsToExam(questionsList: List<Map<String, Any>>, examId: String, eligibilityGroup: String) {
        // Fetch eligible students based on eligibilityGroup and use a callback to handle the result
        getEligibleStudents(eligibilityGroup) { eligibleStudents ->
            // Ensure eligibleStudents is a List<String>
            if (eligibleStudents.isEmpty() || questionsList.isEmpty()) {
                Toast.makeText(requireContext(), "No questions or eligible students found!", Toast.LENGTH_SHORT).show()
                return@getEligibleStudents
            }

            // Update the exam document with the questions list and eligible students
            db.collection("exams").document(examId)
                .update(
                    mapOf(
                        "questions" to questionsList,
                        "eligibleStudents" to eligibleStudents
                    )
                )
                .addOnSuccessListener {
                    Toast.makeText(requireContext(), "Questions added to the exam successfully!", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener { e ->
                    Toast.makeText(
                        requireContext(),
                        "Error adding questions to the exam: ${e.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
        }
    }

    private fun getEligibleStudents(eligibilityGroup: String, callback: (List<String>) -> Unit) {
        val eligibleStudents = mutableListOf<String>()

        // Query the studentGroups collection
        db.collection("groups")
            .document(FirebaseAuth.getInstance().currentUser?.email ?: "")
            .collection("studentGroups")
            .document(eligibilityGroup)
            .collection("students")
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    // Add each student's email to the list
                    val email = document.getString("email")
                    if (!email.isNullOrEmpty()) {
                        eligibleStudents.add(email)
                    }
                }
                Toast.makeText(requireContext(), "Eligible students fetched successfully!", Toast.LENGTH_SHORT).show()
                // Invoke the callback with the fetched eligible students
                callback(eligibleStudents)
            }
            .addOnFailureListener { e ->
                Toast.makeText(requireContext(), "Error fetching eligible students: ${e.message}", Toast.LENGTH_SHORT).show()
                // Invoke the callback with an empty list in case of failure
                callback(emptyList())
            }
    }



    private fun clearFields() {
        examTitleEditText.text?.clear()
        examCodeEditText.text?.clear()
        eligibilityEditText.text?.clear()
        selectedStartTimeTextView.text = "Select Start Time"
        selectedEndTimeTextView.text = "Select End Time"
        selectDateTextView.text = "Select Date"
    }
}


