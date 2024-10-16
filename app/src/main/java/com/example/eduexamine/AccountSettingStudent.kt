package com.example.eduexamine

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.eduexamine.databinding.ActivityAccountSettingStudentBinding

class AccountSettingStudent : AppCompatActivity() {
    private val binding:ActivityAccountSettingStudentBinding by lazy {
        ActivityAccountSettingStudentBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        binding.button8.setOnClickListener{
            val intent=Intent(this,UpdatePassStudent::class.java)
            startActivity(intent)
            finish()
            Toast.makeText(this,"Update Your Password",Toast.LENGTH_SHORT).show()
        }
        binding.button9.setOnClickListener{
            val intent=Intent(this,DeleteAccountStudent::class.java)
            startActivity(intent)
            finish()
            Toast.makeText(this,"Delete Your Account",Toast.LENGTH_SHORT).show()
        }
    }
}