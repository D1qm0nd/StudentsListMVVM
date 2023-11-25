package com.example.studentslistmvvm

import java.time.LocalDate

data class Student(var Surname: String="", var Name: String="", var Middlename: String="", var Gender: String="", var BirthDay: LocalDate= LocalDate.now()) {}

