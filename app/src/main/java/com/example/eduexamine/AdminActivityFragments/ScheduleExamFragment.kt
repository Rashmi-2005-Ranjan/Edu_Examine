package com.example.eduexamine.AdminActivityFragments

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
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
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
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

    private val db: FirebaseFirestore = Firebase.firestore
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private var currentExamId: String? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.fragment_schedule_exam, container, false)

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
        if (currentExamId == null) {
            Toast.makeText(requireContext(), "Please schedule the exam first", Toast.LENGTH_SHORT).show()
            return
        }

        val options = arrayOf("MCQ", "MSQ", "NAT")

        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Select Question Type")
        builder.setItems(options) { _, which ->
            val intent = when (which) {
                0 -> Intent(activity, MCQ::class.java)
                1 -> Intent(activity, MSQ::class.java)
                2 -> Intent(activity, NAT::class.java)
                else -> return@setItems
            }

            intent.putExtra("examId", currentExamId)
            startActivity(intent)
        }
        builder.show()
    }

    private fun scheduleExam() {
        val examTitle = examTitleEditText.text.toString().trim()
        val examCode = examCodeEditText.text.toString().trim()
        val eligibilityName = eligibilityEditText.text.toString().trim()
        val startTime = selectedStartTimeTextView.text.toString()
        val endTime = selectedEndTimeTextView.text.toString()
        val date = selectDateTextView.text.toString()

        if (examTitle.isEmpty() || examCode.isEmpty() || eligibilityName.isEmpty() ||
            startTime == "Select Start Time" || endTime == "Select End Time" || date == "Select Date") {
            Toast.makeText(requireContext(), "Please fill in all the details correctly.", Toast.LENGTH_SHORT).show()
            return
        }

        val currentUserEmail = auth.currentUser?.email ?: return

        db.collection("groups")
            .document(currentUserEmail)
            .collection("studentGroups")
            .document(eligibilityName)
            .collection("students")
            .get()
            .addOnSuccessListener { querySnapshot ->
                if (!querySnapshot.isEmpty) {
                    val studentEmails = querySnapshot.documents.mapNotNull { it.getString("email") }

                    if (studentEmails.isNotEmpty()) {
                        val examData = hashMapOf(
                            "examTitle" to examTitle,
                            "examCode" to examCode,
                            "startTime" to startTime,
                            "endTime" to endTime,
                            "date" to date,
                            "adminEmail" to currentUserEmail,
                            "studentEmails" to studentEmails
                        )

                        db.collection("exams").add(examData)
                            .addOnSuccessListener { documentReference ->
                                currentExamId = documentReference.id
                                saveExamIdInPreferences(currentExamId)
                                Toast.makeText(requireContext(), "Exam scheduled successfully!", Toast.LENGTH_SHORT).show()
                                clearFields()
                            }
                            .addOnFailureListener { e ->
                                Toast.makeText(requireContext(), "Error scheduling exam: ${e.message}", Toast.LENGTH_SHORT).show()
                            }
                    } else {
                        Toast.makeText(requireContext(), "No students found in the selected group.", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(requireContext(), "No group found with the specified eligibility name.", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener { e ->
                Toast.makeText(requireContext(), "Error fetching groups: ${e.message}", Toast.LENGTH_SHORT).show()
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

    private fun saveExamIdInPreferences(examId: String?) {
        val sharedPreferences = requireActivity().getSharedPreferences("ExamPrefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("currentExamId", examId)
        editor.apply()
        Log.d("ScheduleExamFragment", "Saved exam ID to preferences: $examId")
    }

    fun getExamIdFromPreferences(context: Context): String? {
        val sharedPreferences = context.getSharedPreferences("ExamPrefs", Context.MODE_PRIVATE)
        return sharedPreferences.getString("currentExamId", null)
    }
}
