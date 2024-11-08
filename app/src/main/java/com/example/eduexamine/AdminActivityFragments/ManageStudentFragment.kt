package com.example.eduexamine.AdminActivityFragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.eduexamine.ManageStudents
import com.example.eduexamine.ManageStudentsAdapter
import com.example.eduexamine.R
import com.example.eduexamine.UpdateGroupActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class ManageStudentFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ManageStudentsAdapter
    private val db = FirebaseFirestore.getInstance()
    private val groupList = ArrayList<ManageStudents>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_manage_student, container, false)

        // Initialize RecyclerView
        recyclerView = view.findViewById(R.id.rectangles_recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        // Initialize Adapter with update and delete click listeners
        adapter = ManageStudentsAdapter(
            dataList = groupList,
            onUpdateClick = { group ->
                val intent = Intent(requireContext(), UpdateGroupActivity::class.java)
                intent.putExtra("groupName", group.groupname)
                startActivity(intent)
            },
            onDeleteClick = { group ->
                onDeleteGroup(group)
            }
        )
        recyclerView.adapter = adapter

        // Fetch groups from Firebase
        fetchGroups()

        return view
    }

    private fun fetchGroups() {
        val currentAdminEmail = FirebaseAuth.getInstance().currentUser?.email
        if (currentAdminEmail == null) {
            Toast.makeText(requireContext(), "User is not logged in", Toast.LENGTH_SHORT).show()
            return
        }

        db.collection("groups")
            .document(currentAdminEmail)
            .collection("studentGroups")
            .get()
            .addOnSuccessListener { result ->
                // Clear the existing list before adding the updated groups
                groupList.clear()
                for (document in result) {
                    val groupName = document.getString("groupName") ?: continue
                    groupList.add(ManageStudents(groupName))
                }

                // Notify the adapter to update the RecyclerView
                adapter.notifyDataSetChanged()

                // Log to confirm the data is being updated
                Log.d("FetchGroups", "Groups refreshed: ${groupList.size} groups found.")
            }
            .addOnFailureListener { e ->
                Toast.makeText(requireContext(), "Error fetching groups: ${e.message}", Toast.LENGTH_SHORT).show()
                Log.e("FetchGroups", "Error fetching groups: ${e.message}", e)
            }
    }


    private fun onDeleteGroup(group: ManageStudents) {
        val currentAdminEmail = FirebaseAuth.getInstance().currentUser?.email
        if (currentAdminEmail == null) {
            Toast.makeText(requireContext(), "User is not logged in", Toast.LENGTH_SHORT).show()
            return
        }

        // Find and delete document with matching group name
        db.collection("groups")
            .document(currentAdminEmail)
            .collection("studentGroups")
            .whereEqualTo("groupName", group.groupname)
            .get()
            .addOnSuccessListener { querySnapshot ->
                if (!querySnapshot.isEmpty) {
                    for (document in querySnapshot.documents) {
                        // Delete the group from Firestore
                        db.collection("groups")
                            .document(currentAdminEmail)
                            .collection("studentGroups")
                            .document(document.id)
                            .delete()
                            .addOnSuccessListener {
                                // Remove the group from the list immediately (UI)
                                groupList.remove(group)
                                adapter.notifyDataSetChanged() // Update the UI
                                Toast.makeText(requireContext(), "Group deleted", Toast.LENGTH_SHORT).show()
                            }
                            .addOnFailureListener { e ->
                                Toast.makeText(requireContext(), "Error deleting group: ${e.message}", Toast.LENGTH_SHORT).show()
                            }
                    }
                } else {
                    Toast.makeText(requireContext(), "Group not found", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener { e ->
                Toast.makeText(requireContext(), "Error finding group: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }


}