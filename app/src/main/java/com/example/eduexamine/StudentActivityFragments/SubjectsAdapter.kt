import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.eduexamine.R
import com.example.eduexamine.Subject

class SubjectsAdapter(
    private val context: Context,
    private val subjects: List<Subject>,
    private val onResultClick: (String) -> Unit
) : RecyclerView.Adapter<SubjectsAdapter.SubjectViewHolder>() {

    class SubjectViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val subjectName: TextView = view.findViewById(R.id.tvSubjectName)
        val viewResultButton: Button = view.findViewById(R.id.btnViewResult)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SubjectViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_subject, parent, false)
        return SubjectViewHolder(view)
    }

    override fun onBindViewHolder(holder: SubjectViewHolder, position: Int) {
        holder.subjectName.text = subjects[position].name

        holder.viewResultButton.setOnClickListener {
            onResultClick(subjects[position].name)
        }
    }

    override fun getItemCount(): Int = subjects.size
}
