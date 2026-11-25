package com.example.baism_cw_3

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            var id by remember { mutableStateOf("") }
            var lista by remember { mutableStateOf(listOf<Character>()) }
            var wynik by remember { mutableStateOf("") }
            val scope = rememberCoroutineScope()

            Column {

                Button(onClick = {
                    scope.launch {
                        try {
                            lista = RetrofitClient.api.getCharacters().results
                            wynik = ""
                        } catch (e: Exception) {
                            wynik = "Blad"
                        }
                    }
                }) {
                    Text("Lista postaci")
                }

                OutlinedTextField(
                    value = id,
                    onValueChange = { id = it },
                    label = { Text("ID") }
                )

                Button(onClick = {
                    scope.launch {
                        try {
                            val postac = RetrofitClient.api.getCharacterById(id.toInt())
                            wynik = "${postac.name}, ${postac.status}, ${postac.species}"
                            lista = emptyList()
                        } catch (e: Exception) {
                            wynik = e.message ?: "Blad"
                        }
                    }
                }) {
                    Text("Szukaj")
                }

                Text(wynik)

                LazyColumn {
                    items(lista) { postac ->
                        Text("${postac.id}. ${postac.name}")
                    }
                }
            }
        }
    }
}