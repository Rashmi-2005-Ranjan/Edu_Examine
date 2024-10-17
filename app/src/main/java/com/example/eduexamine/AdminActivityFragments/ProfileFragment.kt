package com.example.eduexamine.AdminActivityFragments

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
import com.example.eduexamine.AccountSettingAdmin
import com.example.eduexamine.BasicInfosAdmin
import com.example.eduexamine.PersonalInfosAdmin
import com.example.eduexamine.ProfessionalInfoAdmin
import com.example.eduexamine.R
import com.example.eduexamine.SocialAdmin
import com.example.eduexamine.adminHome
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
        // Initialize Firestore and Firebase Storage
        firestore = FirebaseFirestore.getInstance()
        storage = FirebaseStorage.getInstance()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_profile2, container, false)

        // Initialize buttons for navigating to different admin activities
        val buttonBasicInformation: Button = view.findViewById(R.id.button)
        val buttonProfessionalInformation: Button = view.findViewById(R.id.button2)
        val buttonPersonalInformation: Button = view.findViewById(R.id.button3)
        val buttonAccountSetting: Button = view.findViewById(R.id.button4)
        val buttonSocialContactLinks: Button = view.findViewById(R.id.button5)
        val backToMainButton: Button = view.findViewById(R.id.backToMainButton)

        // Set button click listeners to navigate to various information screens
        buttonBasicInformation.setOnClickListener {
            val intent = Intent(requireContext(), BasicInfosAdmin::class.java)
            startActivity(intent)
        }
        buttonProfessionalInformation.setOnClickListener {
            val intent = Intent(requireContext(), ProfessionalInfoAdmin::class.java)
            startActivity(intent)
        }
        buttonPersonalInformation.setOnClickListener {
            val intent = Intent(requireContext(), PersonalInfosAdmin::class.java)
            startActivity(intent)
        }
        buttonAccountSetting.setOnClickListener {
            val intent = Intent(requireContext(), AccountSettingAdmin::class.java)
            startActivity(intent)
        }
        buttonSocialContactLinks.setOnClickListener {
            val intent = Intent(requireContext(), SocialAdmin::class.java)
            startActivity(intent)
        }
        backToMainButton.setOnClickListener {
            val intent = Intent(requireContext(), adminHome::class.java)
            startActivity(intent)
        }

        // Initialize profile and background image views
        profileImageView = view.findViewById(R.id.imageView2)
        backgroundImageView = view.findViewById(R.id.imageView)

        // Set click listeners to open image picker for profile and background images
        /*
        profileImageView.setOnClickListener { showImagePickerDialog(PROFILE_IMAGE_REQUEST_CODE) }
        backgroundImageView.setOnClickListener { showImagePickerDialog(BACKGROUND_IMAGE_REQUEST_CODE) }

        // Load previously saved images for the current admin user
        loadUserImages()
         */

        return view
    }

    // Show a dialog with options to pick an image from the gallery
    /*
    private fun showImagePickerDialog(requestCode: Int) {
        currentRequestCode = requestCode
        val options = arrayOf("Choose from Gallery", "Cancel")
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Upload or Change Photo")
        builder.setItems(options) { dialog, which ->
            when (options[which]) {
                "Choose from Gallery" -> {
                    // Open the gallery to select an image
                    val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                    startActivityForResult(intent, requestCode)
                }
                "Cancel" -> dialog.dismiss()
            }
        }
        builder.show()
    }
     */

    // Handle the result after an image is selected from the gallery
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == android.app.Activity.RESULT_OK && data != null) {
            selectedImageUri = data.data
            selectedImageUri?.let { uri ->
                // Display the selected image in the respective ImageView
                if (requestCode == PROFILE_IMAGE_REQUEST_CODE) {
                    profileImageView.setImageURI(uri)
                } else if (requestCode == BACKGROUND_IMAGE_REQUEST_CODE) {
                    backgroundImageView.setImageURI(uri)
                }

                // Upload the image to Firebase Storage
//                uploadImageToFirebase(uri)
            }
        }
    }

    // Upload the selected image to Firebase Storage
    /*
    private fun uploadImageToFirebase(fileUri: Uri) {
        val storageRef = storage.reference
        val fileName = UUID.randomUUID().toString() // Generate a unique file name
        val imageRef = if (currentRequestCode == PROFILE_IMAGE_REQUEST_CODE) {
            storageRef.child("profile_images/$fileName")
        } else {
            storageRef.child("background_images/$fileName")
        }

        // Upload the image file to Firebase
        imageRef.putFile(fileUri)
            .addOnSuccessListener {
                // Once uploaded, get the image's download URL and save it to Firestore
                imageRef.downloadUrl.addOnSuccessListener { uri ->
                    saveImageUriToFirestore(uri.toString()) // Save the URL in Firestore
                }
            }
            .addOnFailureListener { exception ->
                Log.e("Firebase", "Failed to upload image", exception)
                Toast.makeText(requireContext(), "Image upload failed", Toast.LENGTH_SHORT).show()
            }
    }
     */

    // Save the image's download URL to Firestore database
    /*
    private fun saveImageUriToFirestore(downloadUrl: String) {
        val userId = FirebaseAuth.getInstance().currentUser?.uid

        if (userId != null) {
            // Update the Firestore document with the appropriate image URL (profile or background)
            val updateData = if (currentRequestCode == PROFILE_IMAGE_REQUEST_CODE) {
                mapOf("profileImageUrl" to downloadUrl) // Profile image URL
            } else {
                mapOf("backgroundImageUrl" to downloadUrl) // Background image URL
            }

            // Save the image URL in the "users" collection for the current user
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
     */

    // Load the profile and background images for the current admin user from Firestore
    /*
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
                            .placeholder(R.drawable.profile3) // Default profile image placeholder
                            .into(profileImageView)
                    }

                    // Load the background image using Glide
                    backgroundImageUrl?.let { url ->
                        Glide.with(this)
                            .load(url)
                            .placeholder(R.drawable.backp) // Default background image placeholder
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
         */

    companion object {
        private const val PROFILE_IMAGE_REQUEST_CODE = 1 // Request code for profile image selection
        private const val BACKGROUND_IMAGE_REQUEST_CODE = 2 // Request code for background image selection
    }
}