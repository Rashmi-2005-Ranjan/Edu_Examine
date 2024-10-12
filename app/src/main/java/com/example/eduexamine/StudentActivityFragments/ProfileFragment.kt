package com.example.eduexamine.StudentActivityFragments

import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.eduexamine.AcademicInfoStudent
import com.example.eduexamine.AccountSettingStudent
import com.example.eduexamine.BasicInfoStudent
import com.example.eduexamine.EducationalDetails
import com.example.eduexamine.R
import com.example.eduexamine.SocialStudents
import com.example.eduexamine.StudentHome
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import de.hdodenhof.circleimageview.CircleImageView
import java.util.*

class ProfileFragment : Fragment() {

    private lateinit var profileImageView: CircleImageView
    private lateinit var backgroundImageView: ImageView
    private lateinit var firestore: FirebaseFirestore
    private lateinit var storage: FirebaseStorage



    private var selectedImageUri: Uri? = null
    private var currentRequestCode: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Initialize Firestore and Storage
        firestore = FirebaseFirestore.getInstance()
        storage = FirebaseStorage.getInstance()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_profile2, container, false)
        val buttonEducationalDetails: Button = view.findViewById(R.id.button)
        val buttonBasicInformation: Button = view.findViewById(R.id.button2)
        val buttonAccountSetting: Button = view.findViewById(R.id.button3)
        val buttonAcademicCareerInfo: Button = view.findViewById(R.id.button4)
        val buttonSocialContactLinks: Button = view.findViewById(R.id.button5)
        val backToMainButton: Button = view.findViewById(R.id.backToMainButton)

        buttonEducationalDetails.setOnClickListener{
            val intent=Intent(requireContext(),EducationalDetails::class.java)
            startActivity(intent)
        }
        buttonBasicInformation.setOnClickListener{
            val intent=Intent(requireContext(),BasicInfoStudent::class.java)
            startActivity(intent)
        }
        buttonAccountSetting.setOnClickListener{
            val intent=Intent(requireContext(),AccountSettingStudent::class.java)
            startActivity(intent)
        }
        buttonAcademicCareerInfo.setOnClickListener{
            val intent=Intent(requireContext(),AcademicInfoStudent::class.java)
            startActivity(intent)
        }
        buttonSocialContactLinks.setOnClickListener{
            val intent=Intent(requireContext(),SocialStudents::class.java)
            startActivity(intent)
        }
        backToMainButton.setOnClickListener{
            val intent=Intent(requireContext(),StudentHome::class.java)
            startActivity(intent)
        }
        profileImageView = view.findViewById(R.id.imageView2)
        backgroundImageView = view.findViewById(R.id.imageView)

        // Set click listeners for changing profile and background images
        profileImageView.setOnClickListener { showImagePickerDialog(PROFILE_IMAGE_REQUEST_CODE) }
        backgroundImageView.setOnClickListener { showImagePickerDialog(BACKGROUND_IMAGE_REQUEST_CODE) }

        // Load previously saved images for the current user
        loadUserImages()

        return view
    }

    // Display options for choosing an image from the gallery
    private fun showImagePickerDialog(requestCode: Int) {
        currentRequestCode = requestCode
        val options = arrayOf("Choose from Gallery", "Cancel")
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Upload or Change Photo")
        builder.setItems(options) { dialog, which ->
            when (options[which]) {
                "Choose from Gallery" -> {
                    val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                    startActivityForResult(intent, requestCode)
                }
                "Cancel" -> dialog.dismiss()
            }
        }
        builder.show()
    }

    // Handle result after selecting an image from the gallery
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == android.app.Activity.RESULT_OK && data != null) {
            selectedImageUri = data.data
            selectedImageUri?.let { uri ->
                // Display selected image in the ImageView
                if (requestCode == PROFILE_IMAGE_REQUEST_CODE) {
                    profileImageView.setImageURI(uri)
                } else if (requestCode == BACKGROUND_IMAGE_REQUEST_CODE) {
                    backgroundImageView.setImageURI(uri)
                }

                // Upload the image to Firebase
                uploadImageToFirebase(uri)
            }
        }
    }

    // Upload the selected image to Firebase Storage
    private fun uploadImageToFirebase(fileUri: Uri) {
        val storageRef = storage.reference
        val fileName = UUID.randomUUID().toString() // Generate a unique file name
        val imageRef = if (currentRequestCode == PROFILE_IMAGE_REQUEST_CODE) {
            storageRef.child("profile_images/$fileName")
        } else {
            storageRef.child("background_images/$fileName")
        }

        imageRef.putFile(fileUri)
            .addOnSuccessListener {
                // Get the download URL and save it to Firestore
                imageRef.downloadUrl.addOnSuccessListener { uri ->
                    saveImageUriToFirestore(uri.toString())
                }
            }
            .addOnFailureListener { exception ->
                Log.e("Firebase", "Failed to upload image", exception)
                Toast.makeText(requireContext(), "Image upload failed", Toast.LENGTH_SHORT).show()
            }
    }

    // Save the image download URL to Firestore
    private fun saveImageUriToFirestore(downloadUrl: String) {
        val userId = FirebaseAuth.getInstance().currentUser?.uid

        if (userId != null) {
            val updateData = if (currentRequestCode == PROFILE_IMAGE_REQUEST_CODE) {
                mapOf("profileImageUrl" to downloadUrl)
            } else {
                mapOf("backgroundImageUrl" to downloadUrl)
            }

            firestore.collection("users")
                .document(userId)
                .update(updateData)
                .addOnSuccessListener {
                    Toast.makeText(requireContext(), "Image URL saved in database", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener { e ->
                    Log.e("Firestore", "Error saving image URL", e)
                    Toast.makeText(requireContext(), "Failed to save image URL", Toast.LENGTH_SHORT).show()
                }
        } else {
            Toast.makeText(requireContext(), "User not authenticated", Toast.LENGTH_SHORT).show()
        }
    }

    // Load profile and background images for the current user from Firestore
    private fun loadUserImages() {
        val userId = FirebaseAuth.getInstance().currentUser?.uid
        userId?.let {
            firestore.collection("users").document(it)
                .get()
                .addOnSuccessListener { document ->
                    val profileImageUrl = document.getString("profileImageUrl")
                    val backgroundImageUrl = document.getString("backgroundImageUrl")

                    // Load the profile image using Glide
                    profileImageUrl?.let { url ->
                        Glide.with(this)
                            .load(url)
                            .placeholder(R.drawable.profile3) // Default image
                            .into(profileImageView)
                    }

                    // Load the background image using Glide
                    backgroundImageUrl?.let { url ->
                        Glide.with(this)
                            .load(url)
                            .placeholder(R.drawable.top_background) // Default image
                            .into(backgroundImageView)
                    }
                }
                .addOnFailureListener { e ->
                    Log.e("Firestore", "Error loading images", e)
                    Toast.makeText(requireContext(), "Error loading images", Toast.LENGTH_SHORT).show()
                }
        } ?: run {
            Toast.makeText(requireContext(), "User not authenticated", Toast.LENGTH_SHORT).show()
        }
    }

    companion object {
        private const val PROFILE_IMAGE_REQUEST_CODE = 1
        private const val BACKGROUND_IMAGE_REQUEST_CODE = 2
    }
}
