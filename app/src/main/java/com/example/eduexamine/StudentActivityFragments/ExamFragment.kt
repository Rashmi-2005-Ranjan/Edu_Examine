package com.example.eduexamine.StudentActivityFragments

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
import com.example.eduexamine.StudentActivityFragments.ResultFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore

class ExamFragment : Fragment() {

    private lateinit var subjectNameTextView: TextView
    private lateinit var questionTextView: TextView
    private lateinit var optionsRadioGroup: RadioGroup
    private lateinit var natAnswerEditText: EditText
    private lateinit var submitAnswerButton: Button
    private lateinit var prevButton: Button
    private lateinit var nextButton: Button
    private lateinit var submitExam: Button
    private lateinit var timerTextView: TextView
    private lateinit var questionNumberTextView: TextView
    private lateinit var questionTypeTextView: TextView
    private val db = FirebaseFirestore.getInstance()
    private val userAnswers = mutableMapOf<String, String>()
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
        submitExam = view.findViewById(R.id.submit_Exam)

        // Get the examId passed from the previous fragment
        val examId = arguments?.getString("examId")

        if (examId == null) {
            Toast.makeText(requireContext(), "Exam ID is missing", Toast.LENGTH_SHORT).show()
            return view
        }

        loadExamDetails(examId)
        loadQuestions(examId)

        setupListeners()
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
        db.collection("exams")
            .document(examId)
            .collection("questions")
            .get()
            .addOnSuccessListener { querySnapshot ->
                questionsList.clear() // Clear questions list to avoid loading previous exam questions
                currentQuestionIndex = 0 // Reset question index for the new exam

                for (document in querySnapshot.documents) {
                    val questionId = document.id
                    val question = document.getString("question") ?: ""
                    val option1 = document.getString("option1") ?: ""
                    val option2 = document.getString("option2") ?: ""
                    val option3 = document.getString("option3") ?: ""
                    val option4 = document.getString("option4") ?: ""
                    val correctAnswer = document.getString("correctAnswer") ?: ""
                    val mark = document.getLong("mark") ?: 1
                    val type = document.getString("type") ?: ""

                    // Add question to the list
                    questionsList.add(
                        Question(
                            id = questionId,
                            question = question,
                            option1 = option1,
                            option2 = option2,
                            option3 = option3,
                            option4 = option4,
                            correctAnswer = correctAnswer,
                            mark = mark,
                            type = type
                        )
                    )
                }

                if (questionsList.isNotEmpty()) {
                    displayQuestion(currentQuestionIndex)
                } else {
                    Toast.makeText(requireContext(), "No questions found for this exam.", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener {
                Toast.makeText(requireContext(), "Failed to load questions.", Toast.LENGTH_SHORT).show()
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
                    optionButtons.forEach { option: String ->
                        optionsRadioGroup.addView(RadioButton(requireContext()).apply {
                            text = option
                            setTextColor(resources.getColor(R.color.black))
                        })
                    }
                    optionsRadioGroup.clearCheck()
                    val savedAnswer = userAnswers[questionItem.question]
                    savedAnswer?.let { savedAnswer ->
                        for (i in 0 until optionsRadioGroup.childCount) {
                            val radioButton = optionsRadioGroup.getChildAt(i) as RadioButton
                            if (radioButton.text == savedAnswer) {
                                radioButton.isChecked = true
                                break
                            }
                        }
                    }

                }

                "NAT" -> {
                    natAnswerEditText.visibility = View.VISIBLE
                    natAnswerEditText.hint = "Type your answer here"

                    natAnswerEditText.inputType = android.text.InputType.TYPE_CLASS_TEXT
                    natAnswerEditText.setText("")
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

    private fun setupListeners() {
        nextButton.setOnClickListener {
            if (currentQuestionIndex < questionsList.size - 1) {
                handleAnswerSubmission()
                currentQuestionIndex++
                displayQuestion(currentQuestionIndex)
            }
        }

        prevButton.setOnClickListener {
            if (currentQuestionIndex > 0) {
                currentQuestionIndex--
                displayQuestion(currentQuestionIndex)
            }
        }

        submitAnswerButton.setOnClickListener {
            // Collect the answer
            val question = questionsList[currentQuestionIndex]
            val userAnswer: String = when (question.type) {
                "MCQ", "MSQ" -> {
                    // Get selected option
                    val selectedRadioButton = view?.findViewById<RadioButton>(optionsRadioGroup.checkedRadioButtonId)
                    selectedRadioButton?.text.toString()
                }
                "NAT" -> {
                    // Get text from NAT answer input
                    natAnswerEditText.text.toString()
                }
                else -> ""
            }

            if (userAnswer.isNotBlank()) {
                // Save the user's answer locally
                userAnswers[questionsList[currentQuestionIndex].question] = userAnswer

                // Save the answer to Firestore
                val db = FirebaseFirestore.getInstance()
                val currentUserId = FirebaseAuth.getInstance().currentUser?.uid
                val answerData = mapOf(
                    "question" to questionsList[currentQuestionIndex].question,
                    "userAnswer" to userAnswer,
                    "timestamp" to FieldValue.serverTimestamp()
                )

                db.collection("exams")
                    .document(arguments?.getString("examId") ?: "unknown_exam")
                    .collection("responses")
                    .document(currentUserId ?: "unknown_user") // Handle anonymous users
                    .collection("userAnswers")
                    .document(questionsList[currentQuestionIndex].question)
                    .set(answerData)
                    .addOnSuccessListener {
                        Toast.makeText(context, "Answer submitted!", Toast.LENGTH_SHORT).show()
                    }
                    .addOnFailureListener { e ->
                        Toast.makeText(context, "Failed to submit answer: ${e.message}", Toast.LENGTH_LONG).show()
                    }
            } else {
                Toast.makeText(context, "Please select or enter an answer before submitting.", Toast.LENGTH_SHORT).show()
            }
        }


        submitExam.setOnClickListener {
            evaluateExam(arguments?.getString("examId") ?: "unknown_exam")
        }
        handleAnswerSubmission()
    }

    private fun handleAnswerSubmission() {
        if (questionsList.isEmpty()) {
            Toast.makeText(requireContext(), "No questions available.", Toast.LENGTH_SHORT).show()
            return
        }

        val currentQuestion = questionsList[currentQuestionIndex]
        val selectedAnswer: String = when (currentQuestion.type) {
            "MCQ", "MSQ" -> {
                val selectedRadioButtonId = optionsRadioGroup.checkedRadioButtonId
                if (selectedRadioButtonId != -1) {
                    val selectedRadioButton = optionsRadioGroup.findViewById<RadioButton>(selectedRadioButtonId)
                    selectedRadioButton.text.toString()
                } else {
                    ""
                }
            }
            "NAT" -> {
                natAnswerEditText.text.toString().trim()
            }
            else -> ""
        }

        if (selectedAnswer.isEmpty()) {
            Toast.makeText(requireContext(), "Please select or enter an answer", Toast.LENGTH_SHORT).show()
            return
        }

        // Save the user's answer locally
        userAnswers[currentQuestion.question] = selectedAnswer

        // Save the answer to Firestore
        val currentUserId = FirebaseAuth.getInstance().currentUser?.uid ?: "unknown_user"
        val answerData = hashMapOf(
            "questionId" to currentQuestion.id,
            "userAnswer" to selectedAnswer,
            "timestamp" to FieldValue.serverTimestamp()
        )

        db.collection("exams")
            .document(arguments?.getString("examId") ?: "unknown_exam")
            .collection("responses")
            .document(currentUserId)
            .collection("userAnswers")
            .document(currentQuestion.id)
            .set(answerData)
            .addOnSuccessListener {
                Toast.makeText(context, "Answer submitted successfully!", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { e ->
                Toast.makeText(context, "Failed to submit answer: ${e.message}", Toast.LENGTH_LONG).show()
            }
    }


    private fun evaluateExam(examId: String) {
        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return

        // Fetch exam details (examTitle)
        db.collection("exams").document(examId).get().addOnSuccessListener { examDoc ->
            val examTitle = examDoc.getString("examTitle") ?: "Unknown Exam" // Default to "Unknown Exam" if missing

            // Fetch user responses
            val userResponsesRef = db.collection("exams").document(examId)
                .collection("responses").document(userId).collection("userAnswers")

            userResponsesRef.get().addOnSuccessListener { responseSnapshot ->
                // Fetch exam questions
                db.collection("exams").document(examId).collection("questions").get()
                    .addOnSuccessListener { questionsSnapshot ->
                        var totalMarks = 0
                        var scoredMarks = 0

                        for (questionDoc in questionsSnapshot.documents) {
                            val questionId = questionDoc.id
                            val correctAnswer = questionDoc.getString("correctAnswer")
                            val mark = questionDoc.getLong("mark") ?: 1

                            val userAnswerDoc =
                                responseSnapshot.documents.find { it.getString("questionId") == questionId }
                            val userAnswer = userAnswerDoc?.getString("userAnswer")

                            totalMarks += mark.toInt()
                            if (userAnswer.equals(correctAnswer, ignoreCase = true)) {
                                scoredMarks += mark.toInt()
                            }
                        }

                        // Save result with exam title
                        val resultData = mapOf(
                            "examId" to examId,
                            "examTitle" to examTitle, // Include the exam title
                            "userId" to userId,
                            "scoredMarks" to scoredMarks,
                            "totalMarks" to totalMarks,
                            "timestamp" to FieldValue.serverTimestamp()
                        )

                        // Save to Firestore
                        db.collection("exam_results").add(resultData)
                            .addOnSuccessListener {
                                Toast.makeText(requireContext(), "Result saved!", Toast.LENGTH_SHORT).show()
                                navigateToResultsScreen(scoredMarks, totalMarks)
                            }
                            .addOnFailureListener {
                                Toast.makeText(requireContext(), "Failed to save result.", Toast.LENGTH_SHORT).show()
                            }

                        // Display the score
                        Toast.makeText(requireContext(), "Score: $scoredMarks / $totalMarks", Toast.LENGTH_LONG).show()
                    }
                    .addOnFailureListener {
                        Toast.makeText(
                            requireContext(),
                            "Failed to load questions for evaluation.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
            }.addOnFailureListener {
                Toast.makeText(requireContext(), "Failed to load user responses.", Toast.LENGTH_SHORT).show()
            }
        }.addOnFailureListener {
            Toast.makeText(requireContext(), "Failed to fetch exam details.", Toast.LENGTH_SHORT).show()
        }
    }



    private fun navigateToResultsScreen(scoredMarks: Int, totalMarks: Int) {
        val resultFragment = ResultFragment()
        resultFragment.arguments = Bundle().apply {
            putInt("scoredMarks", scoredMarks)
            putInt("totalMarks", totalMarks)
        }
        parentFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, resultFragment)
            .commit()
    }

    private fun startTimer() {
        timer = object : CountDownTimer(totalTimeInMillis, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val minutes = (millisUntilFinished / 1000) / 60
                val seconds = (millisUntilFinished / 1000) % 60
                timerTextView.text = String.format("%02d:%02d", minutes, seconds)
            }

            override fun onFinish() {
                Toast.makeText(requireContext(), "Time's up! Submitting your responses.", Toast.LENGTH_SHORT).show()
                handleAnswerSubmission()
                // Optionally, navigate to a results screen or finish the activity
            }
        }.start()
    }

    override fun onDestroy() {
        super.onDestroy()
        timer?.cancel()
    }

    data class Question(
        val id: String,
        val question: String,
        val option1: String,
        val option2: String,
        val option3: String,
        val option4: String,
        val correctAnswer: String, // Add correctAnswer field
        val mark: Long,
        val type: String
    )
}