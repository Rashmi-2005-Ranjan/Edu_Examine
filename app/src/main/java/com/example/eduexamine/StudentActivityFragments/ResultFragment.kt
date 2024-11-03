import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.content.Intent
import com.example.eduexamine.R
import com.example.eduexamine.Subject

class ResultFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: SubjectsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_result, container, false)

        // Initialize RecyclerView
        recyclerView = view.findViewById(R.id.recyclerViewSubjects)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        // Sample data for subjects
        val subjects = listOf(
            Subject("Mathematics"),
            Subject("Physics"),
            Subject("Chemistry"),
            Subject("Biology"),
            Subject("English Literature"),
            Subject("History"),
            Subject("Computer Science")
        )

        adapter = SubjectsAdapter(requireContext(), subjects) { subjectName ->
            viewResult(subjectName)
        }
        recyclerView.adapter = adapter

        return view
    }

    private fun viewResult(subjectName: String) {
        // Implement navigation to the result display page
       // val intent = Intent(requireContext(), ResultActivity::class.java)
        //intent.putExtra("SUBJECT_NAME", subjectName)
       // startActivity(intent)
    }

    private fun generateExcelReport() {
    }
}
