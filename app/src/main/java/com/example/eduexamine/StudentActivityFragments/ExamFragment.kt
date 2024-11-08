package com.example.eduexamine.StudentActivityFragments

import android.content.Context
import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.eduexamine.R
import com.google.firebase.firestore.FirebaseFirestore

class ExamFragment : Fragment() {

    private lateinit var subjectNameTextView: TextView
    private lateinit var questionTextView: TextView
    private lateinit var optionsRadioGroup: RadioGroup
    private lateinit var natAnswerEditText: EditText
    private lateinit var submitAnswerButton: Button
    private lateinit var prevButton: Button
    private lateinit var nextButton: Button
    private lateinit var timerTextView: TextView
    private lateinit var questionNumberTextView: TextView
    private lateinit var questionTypeTextView: TextView
    private val db = FirebaseFirestore.getInstance()

    private var currentQuestionIndex = 0
    private val questionsList = mutableListOf<Question>()
    private var totalTimeInMillis: Long = 60000 // Example: 1 minute
    private var timer: CountDownTimer? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_exam, container, false)

        subjectNameTextView = view.findViewById(R.id.titleTextView)
        questionTextView = view.findViewById(R.id.questionTextView)
        optionsRadioGroup = view.findViewById(R.id.optionsRadioGroup)
        natAnswerEditText = view.findViewById(R.id.natAnswerEditText)
        submitAnswerButton = view.findViewById(R.id.submitAnswerButton)
        prevButton = view.findViewById(R.id.prevButton)
        nextButton = view.findViewById(R.id.nextButton)
        timerTextView = view.findViewById(R.id.timerTextView)
        questionNumberTextView = view.findViewById(R.id.questionNumberTextView)
        questionTypeTextView = view.findViewById(R.id.questionTypeTextView)

        // Get the examId passed from the previous fragment
        val examId = arguments?.getString("examId")

        if (examId == null) {
            Toast.makeText(requireContext(), "Exam ID is missing", Toast.LENGTH_SHORT).show()
            return view
        }

        loadExamDetails(examId)
        loadQuestions(examId)

        submitAnswerButton.setOnClickListener {
            handleAnswerSubmission()
        }

        prevButton.setOnClickListener {
            if (currentQuestionIndex > 0) {
                currentQuestionIndex--
                displayQuestion(currentQuestionIndex)
            }
        }

        nextButton.setOnClickListener {
            if (currentQuestionIndex < questionsList.size - 1) {
                currentQuestionIndex++
                displayQuestion(currentQuestionIndex)
            } else {
                submitAnswerButton.visibility = View.VISIBLE
                nextButton.isEnabled = false
            }
        }

        return view
    }

    private fun loadExamDetails(examId: String) {
        db.collection("exams").document(examId).get().addOnSuccessListener { document ->
            if (document != null) {
                val subjectName = document.getString("examTitle") ?: "Unknown Subject"
                subjectNameTextView.text = "Subject: $subjectName"
                startTimer()
            } else {
                Toast.makeText(requireContext(), "Exam details not found", Toast.LENGTH_SHORT).show()
            }
        }.addOnFailureListener {
            Toast.makeText(requireContext(), "Failed to load exam details", Toast.LENGTH_SHORT).show()
        }
    }

    private fun loadQuestions(examId: String) {
        db.collection("exams").document(examId).collection("questions").get().addOnSuccessListener { querySnapshot ->
            questionsList.clear() // Clear questions list to avoid loading previous exam questions
            currentQuestionIndex = 0 // Reset question index for the new exam

            for (document in querySnapshot.documents) {
                val question = document.getString("question") ?: ""
                val option1 = document.getString("option1") ?: ""
                val option2 = document.getString("option2") ?: ""
                val option3 = document.getString("option3") ?: ""
                val option4 = document.getString("option4") ?: ""
                val mark = document.getLong("mark") ?: 0
                val type = document.getString("type") ?: ""

                questionsList.add(Question(question, option1, option2, option3, option4, mark, type))
            }

            if (questionsList.isNotEmpty()) {
                displayQuestion(currentQuestionIndex)
            }
        }.addOnFailureListener {
            Toast.makeText(requireContext(), "Failed to load questions", Toast.LENGTH_SHORT).show()
        }
    }

    private fun displayQuestion(index: Int) {
        if (index < questionsList.size) {
            val questionItem = questionsList[index]
            questionTextView.text = questionItem.question

            questionNumberTextView.text = "Question ${index + 1}"
            questionTypeTextView.text = "Type: ${questionItem.type}"

            optionsRadioGroup.removeAllViews()
            natAnswerEditText.visibility = View.GONE
            optionsRadioGroup.visibility = View.GONE

            when (questionItem.type) {
                "MCQ", "MSQ" -> {
                    optionsRadioGroup.visibility = View.VISIBLE
                    val optionButtons = listOf(
                        questionItem.option1,
                        questionItem.option2,
                        questionItem.option3,
                        questionItem.option4
                    )
                    optionButtons.forEach { option ->
                        optionsRadioGroup.addView(RadioButton(requireContext()).apply {
                            text = option
                            setTextColor(resources.getColor(R.color.black))
                        })
                    }
                }
                "NAT" -> {
                    natAnswerEditText.visibility = View.VISIBLE
                    natAnswerEditText.hint = "Type your answer here"
                    natAnswerEditText.inputType = android.text.InputType.TYPE_CLASS_TEXT

                    // Set the input text color to black
                    natAnswerEditText.setTextColor(resources.getColor(R.color.black, null))

                    // Optionally, set the hint text color to gray or any other color
                    natAnswerEditText.setHintTextColor(resources.getColor(R.color.gray, null))
                }

            }

            prevButton.isEnabled = index > 0
            nextButton.isEnabled = index < questionsList.size - 1
            submitAnswerButton.visibility = View.VISIBLE
        } else {
            Toast.makeText(requireContext(), "No more questions available", Toast.LENGTH_SHORT).show()
        }
    }

    private fun handleAnswerSubmission() {
        if (currentQuestionIndex < questionsList.size) {
            val questionItem = questionsList[currentQuestionIndex]
            val selectedOptionId = optionsRadioGroup.checkedRadioButtonId

            if (questionItem.type == "MCQ" || questionItem.type == "MSQ") {
                if (selectedOptionId != -1) {
                    val selectedAnswer = view?.findViewById<RadioButton>(selectedOptionId)?.text.toString()
                    Toast.makeText(requireContext(), "You selected: $selectedAnswer", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(requireContext(), "Please select an answer.", Toast.LENGTH_SHORT).show()
                }
            } else if (questionItem.type == "NAT") {
                val natAnswer = natAnswerEditText.text.toString()
                if (natAnswer.isNotEmpty()) {
                    Toast.makeText(requireContext(), "You entered: $natAnswer", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(requireContext(), "Please enter an answer.", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun startTimer() {
        timer = object : CountDownTimer(totalTimeInMillis, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                totalTimeInMillis = millisUntilFinished
                val seconds = (millisUntilFinished / 1000).toInt()
                timerTextView.text = String.format("%02d:%02d", seconds / 60, seconds % 60)
            }

            override fun onFinish() {
                timerTextView.text = "00:00"
            }
        }.start()
    }

    override fun onDestroy() {
        super.onDestroy()
        timer?.cancel()
    }

    data class Question(
        val question: String,
        val option1: String,
        val option2: String,
        val option3: String,
        val option4: String,
        val mark: Long,
        val type: String
    )
}