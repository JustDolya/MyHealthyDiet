package com.example.myhealthydiet

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.example.myhealthydiet.ui.navigation.AppNavGraph
import com.example.myhealthydiet.ui.theme.MyHealthyDietTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyHealthyDietTheme {
                val isLoggedIn by viewModel.isLoggedIn.collectAsState()
                AppNavGraph(isLoggedIn = isLoggedIn)
            }
        }
    }
}