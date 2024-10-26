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

        }

        // Initialize profile and background image views
        profileImageView = view.findViewById(R.id.imageView2)
        backgroundImageView = view.findViewById(R.id.imageView)

        return view
    }

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
    companion object {
        private const val PROFILE_IMAGE_REQUEST_CODE = 1 // Request code for profile image selection
        private const val BACKGROUND_IMAGE_REQUEST_CODE = 2 // Request code for background image selection
    }
}