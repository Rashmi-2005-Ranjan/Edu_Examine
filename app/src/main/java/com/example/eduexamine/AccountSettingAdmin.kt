package com.example.eduexamine

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.eduexamine.databinding.ActivityAccountSettingAdminBinding

class AccountSettingAdmin : AppCompatActivity() {
    private val binding: ActivityAccountSettingAdminBinding by lazy {
        ActivityAccountSettingAdminBinding.inflate(layoutInflater)
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
            dialog.setTitle("Update Password")
            dialog.setMessage(R.string.des)
            dialog.setPositiveButton("YES") { text, listener ->
                val intent = Intent(this, UpdatePassAdmin::class.java)
                startActivity(intent)
                finish()
                Toast.makeText(this, "Update Your Password", Toast.LENGTH_SHORT).show()
            }
            dialog.setNegativeButton("NO") { text, listener ->
                Toast.makeText(this, "No", Toast.LENGTH_SHORT).show()
            }
            dialog.setNeutralButton("CANCEL") { dialog, which ->
                Toast.makeText(this, "You Clicked Cancel", Toast.LENGTH_SHORT).show()
            }
            val alertDialog = dialog.create()
            alertDialog.setCancelable(false)
            alertDialog.show()

        }
        binding.button9.setOnClickListener {
            val dialog = AlertDialog.Builder(this)
            dialog.setTitle("Delete Account")
            dialog.setMessage(R.string.del)
            dialog.setPositiveButton("YES") { text, listener ->
                val intent = Intent(this, DeleteAccountAdmin::class.java)
                startActivity(intent)
                finish()
                Toast.makeText(this, "Delete Your Account", Toast.LENGTH_SHORT).show()
            }
            dialog.setNegativeButton("NO") { text, listener ->
                Toast.makeText(this, "No", Toast.LENGTH_SHORT).show()
            }
            dialog.setNeutralButton("CANCEL") { dialog, which ->
                Toast.makeText(this, "You Clicked Cancel", Toast.LENGTH_SHORT).show()
            }
            val alertDialog = dialog.create()
            alertDialog.setCancelable(false)
            alertDialog.show()
        }
    }
}