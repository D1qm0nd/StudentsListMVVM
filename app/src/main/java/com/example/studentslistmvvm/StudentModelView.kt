package com.example.studentslistmvvm

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import java.lang.Exception
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class StudentModelView(model: Model<Student>, val context: Context) :
    ModelView<Student>(model) {//: ModelView<Student>(model) {

    companion object {
        //выбранная сущьность для изменения
        private var currentEdited : Pair<Int,Student>? = null

        //найденные сущьности при поиске
        //TODO: реализовать
        private lateinit var searchedList : List<Pair<Int,Student>>

        /* * * * * * * * * * * * * * * *
        * маршрутизаторы для навигации *
        * * * * * * * * * * * * * * * */
        private val navShowList = "ShowList" to "show"
        private val navEdit = "Edit" to "edit"
        private val navAdd = "Add" to "add"
        private val navRemove = "" to "remove"
    }

    @ExperimentalMaterial3Api
    @Composable
    override fun Show() {
        Card {
            var entities = model.notifyFlow.collectAsState().value
            val navController = rememberNavController()
            NavHost(navController = navController, startDestination = navShowList.first) {
                composable(navShowList.first) {
                    Column {
                        Row {
                            IconButton(onClick = {
                                navController.navigate(navAdd.first)
                            }) {
                                Icon(Icons.Default.Add, navAdd.second)
                            }
                            TextField(value = "", onValueChange = {})
                        }
                        ShowList(entities, navController)
                    }
                }
                composable(navAdd.first) {
                    Add(navController)
                }
                composable(navEdit.first) {
                    if (currentEdited != null)
                        Edit(entity = currentEdited!!)
                    else navController.navigate(navShowList.first)
                }
            }
        }
    }

    @ExperimentalMaterial3Api
    @Composable
    private fun Add(navController: NavController) {
//        var a = model.notifyFlow.collectAsState().value
        var entity by remember { mutableStateOf(Student()) }
        Column {
            var surname by remember { mutableStateOf(entity.Surname) }
            OutlinedTextField(value = surname, onValueChange = {
                surname = it
                entity.Surname = surname
            })
            var name by remember { mutableStateOf(entity.Name) }
            OutlinedTextField(value = name, onValueChange = {
                name = it
                entity.Name = name
            })
            var middlename by remember { mutableStateOf(entity.Middlename) }
            OutlinedTextField(value = middlename, onValueChange = {
                middlename = it
                entity.Middlename = middlename
            })
            var gender by remember { mutableStateOf(entity.Gender) }
            OutlinedTextField(value = gender, onValueChange = {
                gender = it
                entity.Gender = gender
            })
            var date by remember { mutableStateOf(entity.BirthDay.toString()) }
            OutlinedTextField(value = date, onValueChange = {
                date = it
            })
            IconButton(onClick = {


                try {
                    entity.BirthDay = LocalDate.parse(date, DateTimeFormatter.ofPattern("yyyy-MM-dd"))
                    if (entity.Surname.isNullOrEmpty()
                        || entity.Name.isNullOrEmpty()
                        || entity.Middlename.isNullOrEmpty()
                        || entity.Gender.isNullOrEmpty()
                        || entity.BirthDay.isAfter(LocalDate.now()))
                        throw ValidationException("validation failed")
                    model.add(entity)
                } catch (ex : Exception) {
                    Toast.makeText(context, ex.message,Toast.LENGTH_LONG).show()
                }
            }, modifier = Modifier.align(Alignment.End)) {
                Icon(Icons.Default.Add, "add")
            }
        }
    }

    @ExperimentalMaterial3Api
    @Composable
    fun Edit(entity : Pair<Int,Student>)
    {
        var editableEntity by remember { mutableStateOf(entity) }
        Card(modifier = Modifier.fillMaxWidth()) {
            var editEnable by remember { mutableStateOf(false) }
            Row {
                Text(
                    text = entity.first.toString(),
                    modifier = Modifier.width(40.dp)
                )
                Column {
                    val textFieldModifier = Modifier
                    var entity by rememberSaveable { mutableStateOf(entity) }
                    var surname by remember { mutableStateOf(editableEntity.second.Surname) }
                    OutlinedTextField(value = surname, onValueChange = {
                        surname = it
                        entity.second.Surname = surname
                    }, modifier = textFieldModifier)
                    var name by remember { mutableStateOf(editableEntity.second.Name) }
                    OutlinedTextField(value = name, onValueChange = {
                        name = it
                        entity.second.Name = name
                    }, modifier = textFieldModifier)
                    var gender by remember { mutableStateOf(editableEntity.second.Gender) }
                    OutlinedTextField(value = gender, onValueChange = {
                        gender = it
                        entity.second.Gender = gender
                    }, modifier = textFieldModifier)
                }
            }
        }
    }

    @Composable
    @ExperimentalMaterial3Api
    private fun ShowList(entities: List<Pair<Int, Student>>, navController: NavController) {
        LazyColumn {
            items(entities, key = { it.first }) {
                Card(modifier = Modifier.fillMaxWidth()) {
                    var editEnable by remember { mutableStateOf(false) }
                    Row {
                        Text(
                            text = it.first.toString(),
                            modifier = Modifier.width(40.dp)
                        )
                        Column {
                            val textFieldModifier = Modifier
                            var entity by rememberSaveable { mutableStateOf(it) }
                            Text(text = entity.second.Surname, modifier = textFieldModifier)
                            Text(text = entity.second.Name, modifier = textFieldModifier)
                            Text(text = entity.second.Middlename, modifier = textFieldModifier)
                            Text(text = entity.second.Gender, modifier = textFieldModifier)
                            Text(text = entity.second.BirthDay.toString(), modifier = textFieldModifier)
                        }
                        Column {
                            var buttonModifier = Modifier.align(Alignment.End)
                            IconButton(onClick = {currentEdited = it
                                navController.navigate(navEdit.first) }, modifier = buttonModifier) {
                                Icon(Icons.Default.Edit, navEdit.second)
                            }
                            IconButton(onClick = { model.remove(it) }, modifier = buttonModifier) {
                                Icon(Icons.Default.Delete, navRemove.second)
                            }
                        }
                    }
                }
            }
        }
    }
}