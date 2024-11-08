package com.example.eduexamine

data class GroupData(
    var groupName: String = "",
    var students: List<StudentData> = listOf() // A list to hold students for each group
)

data class StudentData(
    var studentName: String = "",
    var studentId: String = ""
)
