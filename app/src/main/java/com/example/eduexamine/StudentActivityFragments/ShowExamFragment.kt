import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.eduexamine.Exam
import com.example.eduexamine.R

class ShowExamFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var examAdapter: ExamAdapter
    private val exams = listOf(
        Exam("Math Exam"),
        Exam("Science Exam"),
        Exam("History Exam")
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_show_exam, container, false)
        recyclerView = view.findViewById(R.id.recyclerViewExams)
        recyclerView.layoutManager = LinearLayoutManager(context)
        examAdapter = ExamAdapter(exams) { examName ->
            val intent = Intent(context, ExamFragment::class.java)
            intent.putExtra("EXAM_NAME", examName)
            startActivity(intent)
        }
        recyclerView.adapter = examAdapter
        return view
    }
}
