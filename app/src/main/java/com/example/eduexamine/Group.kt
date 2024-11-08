package com.example.eduexamine

data class Group(
    val groupName: String = "",
    val students: List<Student> = emptyList() // List of students in this group
)
