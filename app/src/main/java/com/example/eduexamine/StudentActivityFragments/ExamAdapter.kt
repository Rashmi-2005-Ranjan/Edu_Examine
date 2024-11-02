import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.eduexamine.R
import com.example.eduexamine.StudentActivityFragments.Exam

class ExamAdapter(private val exams: List<Exam>, private val onExamClick: (String) -> Unit) :
    RecyclerView.Adapter<ExamAdapter.ExamViewHolder>() {

    class ExamViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val examName: TextView = itemView.findViewById(R.id.tvExamName)
        val attemptButton: Button = itemView.findViewById(R.id.btnAttempt)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExamViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.exam_item, parent, false)
        return ExamViewHolder(view)
    }

    override fun onBindViewHolder(holder: ExamViewHolder, position: Int) {
        val exam = exams[position]
        holder.examName.text = exam.subjectName // Use the property from the Exam data class
        holder.attemptButton.setOnClickListener {
            onExamClick(exam.id) // Pass the exam ID when the button is clicked
        }
    }

    override fun getItemCount() = exams.size
}
