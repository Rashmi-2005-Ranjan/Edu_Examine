package com.example.eduexamine.AdminActivityFragments
import android.annotation.SuppressLint
import com.example.eduexamine.R
import android.app.DatePickerDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.android.material.button.MaterialButton
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.switchmaterial.SwitchMaterial
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.firestore.FirebaseFirestore
import java.util.*

class AddCourseFragment : Fragment() {

    private lateinit var fab: FloatingActionButton
    private lateinit var courseFormLayout: LinearLayout
    private lateinit var courseDetailContainer: LinearLayout
    private lateinit var courseDetailText: TextView
    private lateinit var submitButton: MaterialButton
    private lateinit var cancelButton: MaterialButton
    private lateinit var courseNameInput: TextInputEditText
    private lateinit var courseCodeInput: TextInputEditText
    private lateinit var courseDescriptionInput: TextInputEditText
    private lateinit var courseDuration: TextInputEditText
    private lateinit var instructorName: TextInputEditText
    private lateinit var courseStartDate: TextInputEditText
    private lateinit var courseEndDate: TextInputEditText
    private lateinit var courseActiveSwitch: SwitchMaterial
    private lateinit var uploadCourseMaterialButton: MaterialButton

    private var courseMaterialUri: Uri? = null
    private lateinit var firestore: FirebaseFirestore

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_add_course, container, false)

        // Initialize UI elements
        fab = view.findViewById(R.id.fab)
        courseFormLayout = view.findViewById(R.id.courseFormLayout)
        courseDetailContainer = view.findViewById(R.id.courseDetailContainer)
        courseDetailText = view.findViewById(R.id.courseDetailText)

        submitButton = view.findViewById(R.id.submitCourse)
        cancelButton = view.findViewById(R.id.cancelCourse)
        courseNameInput = view.findViewById(R.id.courseName)
        courseCodeInput = view.findViewById(R.id.courseCode)
        courseDescriptionInput = view.findViewById(R.id.courseDescription)
        courseDuration = view.findViewById(R.id.courseDuration)
        instructorName = view.findViewById(R.id.instructorName)
        courseStartDate = view.findViewById(R.id.courseStartDate)
        courseEndDate = view.findViewById(R.id.courseEndDate)
        courseActiveSwitch = view.findViewById(R.id.courseActiveSwitch)
        uploadCourseMaterialButton = view.findViewById(R.id.uploadCourseMaterialButton)

        firestore = FirebaseFirestore.getInstance()

        // Setup logic for FAB button to show/hide the form
        setupFabButton()

        // Setup logic for submit and cancel buttons in the form
        setupFormButtons()

        // Handle date picker for start date
        courseStartDate.setOnClickListener {
            showDatePicker { date -> courseStartDate.setText(date) }
        }

        // Handle date picker for end date
        courseEndDate.setOnClickListener {
            showDatePicker { date -> courseEndDate.setText(date) }
        }

        // Handle file upload
        uploadCourseMaterialButton.setOnClickListener {
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "*/*"
            startActivityForResult(intent, 1)
        }

        return view
    }

    // Setup logic for FAB button to show/hide the form
    private fun setupFabButton() {
        fab.setOnClickListener {
            fab.hide()  // Hide the FAB when the form appears
            courseFormLayout.visibility = View.VISIBLE
            courseDetailContainer.visibility = View.GONE
        }
    }

    // Setup logic for submit and cancel buttons in the form
    private fun setupFormButtons() {
        submitButton.setOnClickListener {
            val course = collectCourseData()
            if (course != null) {
                // Show course details and hide form
                saveCourseToFirestore(course)
                val courseDetailView = createCourseDetailView(course)
                courseDetailContainer.addView(courseDetailView)



                courseDetailContainer.visibility = View.VISIBLE
                courseFormLayout.visibility = View.GONE  // Hide the form after adding the course
                fab.show()  // Show the FAB again
            }
        }

        cancelButton.setOnClickListener {
            courseFormLayout.visibility = View.GONE  // Hide the form when canceled
            fab.show()  // Show the FAB again
        }
    }

    // Show date picker logic
    private fun showDatePicker(onDatePicked: (String) -> Unit) {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(requireContext(),
            { _, selectedYear, selectedMonth, selectedDay ->
                onDatePicked("$selectedDay/${selectedMonth + 1}/$selectedYear")
            }, year, month, day)
        datePickerDialog.show()
    }

    // Handle file upload result
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1 && data != null) {
            courseMaterialUri = data.data
            uploadCourseMaterialButton.text = "File Selected"
        }
    }

    // Collect course data from input fields
    private fun collectCourseData(): Course? {
        val name = courseNameInput.text.toString()
        val code = courseCodeInput.text.toString()
        val description = courseDescriptionInput.text.toString()
        val duration = courseDuration.text.toString()
        val instructor = instructorName.text.toString()
        val startDate = courseStartDate.text.toString()
        val endDate = courseEndDate.text.toString()
        val isActive = courseActiveSwitch.isChecked

        if (name.isEmpty() || code.isEmpty() || duration.isEmpty() || startDate.isEmpty() || endDate.isEmpty()) {
            Toast.makeText(context, "Please fill all fields", Toast.LENGTH_SHORT).show()
            return null
        }

        return Course(name, code, description, duration, instructor, startDate, endDate, isActive, courseMaterialUri)
    }


    private fun saveCourseToFirestore(course: Course) {
        val courseId = firestore.collection("courses").document().id
        firestore.collection("courses").document(courseId).set(course)
            .addOnSuccessListener {
                val courseDetailView = createCourseDetailView(course)
                courseDetailContainer.addView(courseDetailView)
                courseDetailContainer.visibility = View.VISIBLE
                courseFormLayout.visibility = View.GONE
                fab.show()
            }
            .addOnFailureListener {
                Toast.makeText(context, "Failed to save course", Toast.LENGTH_SHORT).show()
            }
    }


    // Create a view for displaying course details
    @SuppressLint("MissingInflatedId", "InflateParams")
    private fun createCourseDetailView(course: Course): View {
        val courseView = LayoutInflater.from(context).inflate(R.layout.course_item_layout, null)

        val courseNameText = courseView.findViewById<TextView>(R.id.courseNameTextView)
        val courseCodeText = courseView.findViewById<TextView>(R.id.courseCodeTextView)
        val courseDescriptionText = courseView.findViewById<TextView>(R.id.courseDescriptionTextView)
        val courseDurationText = courseView.findViewById<TextView>(R.id.courseDurationTextView)
        val courseInstructorText = courseView.findViewById<TextView>(R.id.courseTeacherTextView)
        val courseStartDateText = courseView.findViewById<TextView>(R.id.StartDateTextView)
        val courseEndDateText = courseView.findViewById<TextView>(R.id.EndDateTextView)

        courseNameText.text = course.name
        courseCodeText.text = course.code
        courseDescriptionText.text = course.description
        courseDurationText.text = course.duration
        courseInstructorText.text = course.instructor
        courseStartDateText.text = course.startDate
        courseEndDateText.text = course.endDate


        // Style the container (border-radius, etc.)
        val containerLayout = courseView.findViewById<LinearLayout>(R.id.courseContainer)
        containerLayout.setBackgroundResource(R.drawable.course_item_background)

        return courseView
    }


    // Course data model
    data class Course(
        val name: String,
        val code: String,
        val description: String,
        val duration: String,
        val instructor: String,
        val startDate: String,
        val endDate: String,
        val isActive: Boolean,
        val materialUri: Uri?
    )
}
