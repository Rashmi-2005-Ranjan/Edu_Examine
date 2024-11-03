package com.example.eduexamine

data class Group(
    val name: String,
    val students: MutableList<Student> = mutableListOf()
)