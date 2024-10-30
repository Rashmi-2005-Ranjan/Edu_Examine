package com.example.eduexamine.AdminActivityFragments

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.eduexamine.AccountSettingAdmin
import com.example.eduexamine.BasicInfosAdmin
import com.example.eduexamine.PersonalInfosAdmin
import com.example.eduexamine.ProfessionalInfoAdmin
import com.example.eduexamine.R
import com.example.eduexamine.SocialAdmin
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import de.hdodenhof.circleimageview.CircleImageView

class ProfileFragment : Fragment() {

    private lateinit var profileImageView: CircleImageView
    private lateinit var backgroundImageView: ImageView
    private lateinit var firestore: FirebaseFirestore
    private lateinit var auth: FirebaseAuth
    private lateinit var storageRef: StorageReference
    private lateinit var nameTextView: TextView
    private lateinit var emailTextView: TextView
    private lateinit var backToHome: Button

    private var profileImageUri: Uri? = null
    private var backgroundImageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        firestore = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()
        storageRef = FirebaseStorage.getInstance().reference
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_profile2, container, false)

        profileImageView = view.findViewById(R.id.imageView2)
        backgroundImageView = view.findViewById(R.id.imageView)
        nameTextView = view.findViewById(R.id.textView)
        emailTextView = view.findViewById(R.id.textView2)

        setupButtons(view)
        loadBasicInfo()
        loadProfileAndBackgroundImages()

        profileImageView.setOnClickListener { selectImage(PROFILE_IMAGE_REQUEST_CODE) }
        backgroundImageView.setOnClickListener { selectImage(BACKGROUND_IMAGE_REQUEST_CODE) }

        return view
    }

    private fun selectImage(requestCode: Int) {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, requestCode)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && data != null) {
            val selectedImageUri = data.data
            when (requestCode) {
                PROFILE_IMAGE_REQUEST_CODE -> {
                    profileImageUri = selectedImageUri
                    profileImageView.setImageURI(profileImageUri)
                }
                BACKGROUND_IMAGE_REQUEST_CODE -> {
                    backgroundImageUri = selectedImageUri
                    backgroundImageView.setImageURI(backgroundImageUri)
                }
            }
        }
    }

    private fun loadBasicInfo() {
        val email = auth.currentUser?.email ?: return
        val documentId = email.replace("_", ".") // Use consistent format for Firebase path

        firestore.collection("adminBasicInfo").document(documentId) // Update to admin collection
            .get()
            .addOnSuccessListener { document ->
                if (document != null && document.exists()) {
                    val fullName = document.getString("fullName") ?: "Admin Name"
                    nameTextView.text = fullName
                    emailTextView.text = email
                } else {
                    Toast.makeText(requireContext(), "No data found for this admin", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener { e ->
                Toast.makeText(requireContext(), "Error retrieving information: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun setupButtons(view: View) {
        // Update buttons to match admin functionalities if needed
        val buttonEducationalDetails: Button = view.findViewById(R.id.button)
        val buttonBasicInformation: Button = view.findViewById(R.id.button2)
        val buttonAccountSetting: Button = view.findViewById(R.id.button3)
        val buttonAcademicCareerInfo: Button = view.findViewById(R.id.button4)
        val buttonSocialContactLinks: Button = view.findViewById(R.id.button5)
        val saveAllButton: Button = view.findViewById(R.id.backToMainButton)

        buttonEducationalDetails.setOnClickListener { val intent = Intent(requireContext(), BasicInfosAdmin::class.java)
            startActivity(intent) }
        buttonBasicInformation.setOnClickListener { val intent=Intent(requireContext(),ProfessionalInfoAdmin::class.java)
            startActivity(intent) }
        buttonAccountSetting.setOnClickListener { val intent=Intent(requireContext(),PersonalInfosAdmin::class.java)
            startActivity(intent) }
        buttonAcademicCareerInfo.setOnClickListener { val intent=Intent(requireContext(),AccountSettingAdmin::class.java)
            startActivity(intent) }
        buttonSocialContactLinks.setOnClickListener { val intent=Intent(requireContext(),SocialAdmin::class.java)
            startActivity(intent) }

        saveAllButton.setOnClickListener {
            Toast.makeText(requireContext(),"SAVING ALL THE DETAILS SUCCESSFULLY",Toast.LENGTH_SHORT).show()
            saveImagesToFirebase()
        }
    }

    private fun saveImagesToFirebase() {

        val email = auth.currentUser?.email ?: return
        val documentId = email.replace("_", ".")

        profileImageUri?.let { uri ->
            val profileRef = storageRef.child("images/$documentId/profile.jpg")
            profileRef.putFile(uri).addOnSuccessListener {
                // Use getContext() instead of requireContext()
                    Toast.makeText(requireContext(), "Profile image saved successfully", Toast.LENGTH_SHORT).show()

            }.addOnFailureListener {
                Toast.makeText(requireContext(),"Failed To Save Profile Image",Toast.LENGTH_SHORT).show()
            }
        }

        backgroundImageUri?.let { uri ->
            val backgroundRef = storageRef.child("images/$documentId/background.jpg")
            backgroundRef.putFile(uri).addOnSuccessListener {

                    Toast.makeText(requireContext(), "Background image saved successfully", Toast.LENGTH_SHORT).show()

            }.addOnFailureListener {

                    Toast.makeText(requireContext(), "Failed to save background image:", Toast.LENGTH_SHORT).show()

            }
        }
    }



    private fun loadProfileAndBackgroundImages() {
        val email = auth.currentUser?.email ?: return
        val documentId = email.replace("_", ".")

        val profileRef = storageRef.child("images/$documentId/profile.jpg") // Update storage path
        profileRef.downloadUrl.addOnSuccessListener { uri ->
            Glide.with(this).load(uri).into(profileImageView)
        }.addOnFailureListener {
            Toast.makeText(requireContext(), "Profile image not found.", Toast.LENGTH_SHORT).show()
        }

        val backgroundRef = storageRef.child("images/$documentId/background.jpg") // Update storage path
        backgroundRef.downloadUrl.addOnSuccessListener { uri ->
            Glide.with(this).load(uri).into(backgroundImageView)
        }.addOnFailureListener {
            Toast.makeText(requireContext(), "Background image not found.", Toast.LENGTH_SHORT).show()
        }
    }

    companion object {
        private const val PROFILE_IMAGE_REQUEST_CODE = 1
        private const val BACKGROUND_IMAGE_REQUEST_CODE = 2
    }
}