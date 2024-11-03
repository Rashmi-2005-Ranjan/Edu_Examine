import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.fragment.app.Fragment
import com.example.eduexamine.R
import com.google.firebase.firestore.FirebaseFirestore

class ExamFragment : Fragment() {
    private lateinit var subjectNameTextView: TextView
    private lateinit var questionTextView: TextView
    private lateinit var optionsRadioGroup: RadioGroup
    private lateinit var submitAnswerButton: Button
    private val db = FirebaseFirestore.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_exam, container, false)

        subjectNameTextView = view.findViewById(R.id.subjectNameTextView)
//        questionTextView = view.findViewById(R.id.questionTextView)
        optionsRadioGroup = view.findViewById(R.id.optionsRadioGroup)
        submitAnswerButton = view.findViewById(R.id.submitAnswerButton)

        val examId = arguments?.getString("EXAM_ID") ?: return view

        loadExamDetails(examId)

        submitAnswerButton.setOnClickListener {
            val selectedOptionId = optionsRadioGroup.checkedRadioButtonId
            if (selectedOptionId != -1) {
                val selectedAnswer = view.findViewById<RadioButton>(selectedOptionId).text.toString()
                Toast.makeText(requireContext(), "You selected: $selectedAnswer", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(requireContext(), "Please select an answer.", Toast.LENGTH_SHORT).show()
            }
        }

        return view
    }

    private fun loadExamDetails(examId: String) {
        db.collection("Exams").document(examId).get().addOnSuccessListener { document ->
            if (document != null) {
                val subjectName = document.getString("subject_name") ?: "Unknown Subject"
                subjectNameTextView.text = "Subject: $subjectName"
                questionTextView.text = "Question 1: What is 2+2?"
            }
        }.addOnFailureListener {
            Toast.makeText(requireContext(), "Failed to load exam details", Toast.LENGTH_SHORT).show()
        }
    }
}
