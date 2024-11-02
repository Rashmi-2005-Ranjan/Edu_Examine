import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.eduexamine.Exam
import com.example.eduexamine.R


class ExamAdapter(
    private val examList: List<Exam>,
    private val onAttemptClick: (String) -> Unit
) : RecyclerView.Adapter<ExamAdapter.ExamViewHolder>() {

    inner class ExamViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val subjectName = itemView.findViewById<TextView>(R.id.subjectName)
        val attemptButton = itemView.findViewById<Button>(R.id.attemptButton)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExamViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.exam_item, parent, false)
        return ExamViewHolder(view)
    }

    override fun onBindViewHolder(holder: ExamViewHolder, position: Int) {
        val exam = examList[position]
        holder.subjectName.text = exam.subjectName
        holder.attemptButton.setOnClickListener {
            onAttemptClick(exam.id)
        }
    }

    override fun getItemCount() = examList.size
}
