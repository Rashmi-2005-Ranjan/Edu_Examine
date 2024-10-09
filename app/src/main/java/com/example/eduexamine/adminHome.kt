package com.example.eduexamine

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.example.eduexamine.AdminActivityFragments.AcceptApplicationFragment
import com.example.eduexamine.AdminActivityFragments.AddCourseFragment
import com.example.eduexamine.AdminActivityFragments.HomeFragment
import com.example.eduexamine.AdminActivityFragments.ManageStudentFragment
import com.example.eduexamine.AdminActivityFragments.ProfileFragment
import com.example.eduexamine.AdminActivityFragments.PublishResultFragment
import com.example.eduexamine.AdminActivityFragments.ScheduleExamFragment
import com.example.eduexamine.AdminActivityFragments.SetAnswerFragment
import com.example.eduexamine.AdminActivityFragments.SetPracticeQuestionFragment
import com.example.eduexamine.AdminActivityFragments.StudentDetailsFragment
import com.example.eduexamine.StudentActivityFragments.AcheivementFragment
import com.example.eduexamine.StudentActivityFragments.MarksheetFragment
import com.example.eduexamine.StudentActivityFragments.NewApplyFragment

import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth

class adminHome : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener, BottomNavigationView.OnNavigationItemSelectedListener {

    private lateinit var drawerLayout: DrawerLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_home) // Use the admin_home XML layout file

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
        val navigationView: NavigationView = findViewById(R.id.nav_drawera)
        navigationView.setNavigationItemSelectedListener(this)

        // Set up the Bottom Navigation View and handle item clicks
        val bottomNavigationView: BottomNavigationView = findViewById(R.id.bottom_navigationa)
        bottomNavigationView.setOnNavigationItemSelectedListener(this)

        // Set initial fragment when the activity starts
        if (savedInstanceState == null) {
            replaceFragment(HomeFragment()) // Replace with your initial fragment
            navigationView.setCheckedItem(R.id.nav_drawera) // Ensure this matches the menu ID
            bottomNavigationView.menu.findItem(R.id.bottom_navigationa).isChecked = true // Set bottom navigation initial selection
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
            R.id.nav_profile -> replaceFragment(ProfileFragment())
            R.id.nav_manage -> replaceFragment(ManageStudentFragment())
            R.id.nav_ac -> replaceFragment(AddCourseFragment())
            R.id.nav_sexamp -> replaceFragment(ScheduleExamFragment())
            R.id.nav_seta -> replaceFragment(SetAnswerFragment())
            R.id.nav_sp ->replaceFragment(SetPracticeQuestionFragment())
            R.id.nav_logout -> {
                FirebaseAuth.getInstance().signOut()
                Toast.makeText(this, "Signing You Out", Toast.LENGTH_SHORT).show()
                // Redirect to login activity or main page
                val intent = Intent(this, WelcomeScreen::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
                finish() // Finish current activity
            }
            // Handle bottom navigation view item clicks
            R.id.homeb -> replaceFragment(HomeFragment()) // Example item ID
            R.id.acpt -> replaceFragment(AcceptApplicationFragment()) // Example item ID
            R.id.publish -> replaceFragment(PublishResultFragment()) // Example item ID
            R.id.getdetail -> replaceFragment(StudentDetailsFragment())
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
        transaction.replace(R.id.fragment_container, fragment) // Ensure fragment container ID matches the XML
        transaction.commit()
    }
}
