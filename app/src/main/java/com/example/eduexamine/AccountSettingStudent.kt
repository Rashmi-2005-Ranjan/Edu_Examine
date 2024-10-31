package com.example.eduexamine

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.eduexamine.databinding.ActivityAccountSettingStudentBinding

class AccountSettingStudent : AppCompatActivity() {
    private val binding: ActivityAccountSettingStudentBinding by lazy {
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
        binding.button8.setOnClickListener {
            val dialog = AlertDialog.Builder(this)
            dialog.setTitle("Update Your Password")
            dialog.setMessage(R.string.des)
            dialog.setIcon(R.drawable.update)
            dialog.setPositiveButton("YES") { dialog, which ->
                val intent = Intent(this, UpdatePassStudent::class.java)
                startActivity(intent)
                finish()
                Toast.makeText(this, "Update Your Password Here", Toast.LENGTH_SHORT).show()
            }
            dialog.setNegativeButton("NO") { dialog, which ->
                Toast.makeText(this, "You Clicked No", Toast.LENGTH_SHORT).show()
            }
            dialog.setNeutralButton("CANCEL") { dialog, which ->
                Toast.makeText(this, "You Clicked Cancel", Toast.LENGTH_SHORT).show()
            }
            dialog.show()
        }
        binding.button9.setOnClickListener {
            val dialog = AlertDialog.Builder(this)
            dialog.setTitle("Delete Your Account")
            dialog.setMessage(R.string.del)
            dialog.setIcon(R.drawable.delete)
            dialog.setPositiveButton("YES") { dialog, which ->
                val intent = Intent(this, DeleteAccountStudent::class.java)
                startActivity(intent)
                finish()
                Toast.makeText(this, "Delete Your Account", Toast.LENGTH_SHORT).show()
            }
            dialog.setNegativeButton("NO") { dialog, which ->
                Toast.makeText(this, "You Clicked No", Toast.LENGTH_SHORT).show()
            }
            dialog.setNeutralButton("CANCEL") { dialog, which ->
                Toast.makeText(this, "You Clicked Cancel", Toast.LENGTH_SHORT).show()
            }
            val alertDialog=dialog.create()
            alertDialog.setCancelable(false)
            alertDialog.show()
        }
    }
}