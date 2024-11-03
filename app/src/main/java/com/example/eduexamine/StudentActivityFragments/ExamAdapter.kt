import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.eduexamine.Exam
import com.example.eduexamine.R

class ExamAdapter(
    private val exams: List<Exam>,
    private val onAttemptClick: (String) -> Unit
) : RecyclerView.Adapter<ExamAdapter.ExamViewHolder>() {

    class ExamViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val examName: TextView = view.findViewById(R.id.tvExamName)
        val attemptButton: Button = view.findViewById(R.id.btnAttempt)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExamViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.exam_item, parent, false)
        return ExamViewHolder(view)
    }

    override fun onBindViewHolder(holder: ExamViewHolder, position: Int) {
        holder.examName.text = exams[position].name
        holder.attemptButton.setOnClickListener {
            onAttemptClick(exams[position].name)
        }
    }

    override fun getItemCount(): Int = exams.size
}
