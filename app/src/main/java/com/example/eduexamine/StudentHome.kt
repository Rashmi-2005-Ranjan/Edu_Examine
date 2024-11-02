package com.example.eduexamine

import ExamFragment
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.example.eduexamine.StudentActivityFragments.AcheivementFragment
import com.example.eduexamine.StudentActivityFragments.CourseFragment
import com.example.eduexamine.StudentActivityFragments.HomeFragment
import com.example.eduexamine.StudentActivityFragments.MarksheetFragment
import com.example.eduexamine.StudentActivityFragments.NewApplyFragment
import com.example.eduexamine.StudentActivityFragments.ProfileFragment
import com.example.eduexamine.StudentActivityFragments.ResultFragment
import com.example.eduexamine.StudentActivityFragments.ShowExamFragment
import com.example.eduexamine.StudentActivityFragments.TrackingFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth

class StudentHome : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener,
    BottomNavigationView.OnNavigationItemSelectedListener {

    private lateinit var drawerLayout: DrawerLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_student_home)

        // Set up the Drawer Layout and Toolbar
        drawerLayout = findViewById(R.id.main)
        val toolbar: androidx.appcompat.widget.Toolbar = findViewById(R.id.toolbars)
        setSupportActionBar(toolbar)

        // Set up ActionBarDrawerToggle for opening and closing the drawer
        val toggle = ActionBarDrawerToggle(
            this, drawerLayout, toolbar,
            R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        // Set up the Navigation View and handle item clicks
        val navigationView: NavigationView = findViewById(R.id.nav_drawer)
        navigationView.setNavigationItemSelectedListener(this)

        // Set up the Bottom Navigation View and handle item clicks
        val bottomNavigationView: BottomNavigationView = findViewById(R.id.bottom_navigation)
        bottomNavigationView.setOnNavigationItemSelectedListener(this)

        // Set initial fragment when the activity starts
        if (savedInstanceState == null) {
            replaceFragment(HomeFragment()) // Replace with your initial fragment
            navigationView.setCheckedItem(R.id.homeb) // Ensure this matches the menu ID
            bottomNavigationView.menu.findItem(R.id.homeb).isChecked =
                true // Set bottom navigation initial selection
        }

        // Apply window insets for immersive mode
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    // Handle back press to close navigation drawer if open
    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    // Handle navigation drawer item clicks and replace fragments accordingly
    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_track -> replaceFragment(TrackingFragment())
            R.id.nav_profile -> replaceFragment(ProfileFragment())
            R.id.nav_cours -> replaceFragment(CourseFragment())
            R.id.nav_examp -> replaceFragment(ShowExamFragment())
            R.id.nav_gexamp -> replaceFragment(ExamFragment())
            R.id.nav_result -> replaceFragment(ResultFragment())
            R.id.nav_logout -> {
                val dialog=AlertDialog.Builder(this)
                dialog.setTitle("Logout")
                dialog.setMessage("Are You Sure You Want To Logout?")
                dialog.setIcon(R.drawable.logoutn)
                dialog.setPositiveButton("YES") { dialog, which ->
                    // Perform sign out
                    FirebaseAuth.getInstance().signOut()
                    Toast.makeText(this, "Signing You Out", Toast.LENGTH_SHORT).show()
                    // Redirect to login activity or main page
                    val intent = Intent(this, WelcomeScreen::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(intent)
                    finish() // Finish current activity
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

            // Handle bottom navigation view item clicks
            R.id.homeb -> replaceFragment(HomeFragment()) // Example item ID
            R.id.Acheivement -> replaceFragment(AcheivementFragment()) // Example item ID
            R.id.getsheet -> replaceFragment(MarksheetFragment()) // Example item ID
            R.id.Extra -> replaceFragment(NewApplyFragment())
        }
        // Close drawer if navigation drawer item selected
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        }
        return true
    }




    // Function to replace fragments in the frame layout
    private fun replaceFragment(fragment: androidx.fragment.app.Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(
            R.id.fragment_container,
            fragment
        ) // Ensure fragment container ID matches the XML
        transaction.commit()
    }
}
