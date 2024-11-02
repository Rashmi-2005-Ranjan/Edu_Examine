import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.eduexamine.R
import com.google.firebase.firestore.FirebaseFirestore

class ShowExamFragment : Fragment() {

    private lateinit var recyclerViewExams: RecyclerView
    private lateinit var examAdapter: ExamAdapter
    private val db = FirebaseFirestore.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_show_exam, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerViewExams = view.findViewById(R.id.recyclerViewExams)
        recyclerViewExams.layoutManager = LinearLayoutManager(requireContext())

        loadExams()
    }

    private fun loadExams() {
        db.collection("Exams").whereEqualTo("is_available", true).get()
            .addOnSuccessListener { documents ->
                val exams = documents.map { document ->
                    Exam(
                        id = document.id,
                        subjectName = document.getString("subject_name") ?: "",
                        examDate = document.getString("exam_date") ?: "",
                        examDuration = document.getString("exam_duration") ?: ""
                    )
                }

                examAdapter = ExamAdapter(exams) { examId ->
                    navigateToExamFragment(examId)
                }
                recyclerViewExams.adapter = examAdapter
            }
            .addOnFailureListener { exception ->
                Toast.makeText(requireContext(), "Failed to load exams: ${exception.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun navigateToExamFragment(examId: String) {
        val examFragment = ExamFragment().apply {
            arguments = Bundle().apply {
                putString("EXAM_ID", examId)
            }
        }

        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, examFragment)
            .addToBackStack(null)
            .commit()
    }
}



data class Exam(
    val id: String,
    val subjectName: String,
    val examDate: String,
    val examDuration: String
)

