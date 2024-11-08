import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.eduexamine.R
import com.example.eduexamine.Student

class StudentAdapter(
    private val students: List<Student>,
    private val onUpdate: (Student) -> Unit,
    private val onDelete: (Student) -> Unit
) : RecyclerView.Adapter<StudentAdapter.StudentViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StudentViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_student, parent, false)
        return StudentViewHolder(view)
    }

    override fun onBindViewHolder(holder: StudentViewHolder, position: Int) {
        val student = students[position]
        holder.bind(student)
    }

    override fun getItemCount(): Int {
        return students.size
    }

    inner class StudentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val registrationNumberTextView: TextView = itemView.findViewById(R.id.registrationNumberTextView)
        private val updateButton: Button = itemView.findViewById(R.id.updateButton)
        private val deleteButton: Button = itemView.findViewById(R.id.deleteButton)

        fun bind(student: Student) {
            registrationNumberTextView.text = student.registrationNumber
            updateButton.setOnClickListener { onUpdate(student) }
            deleteButton.setOnClickListener { onDelete(student) }
        }
    }
}
