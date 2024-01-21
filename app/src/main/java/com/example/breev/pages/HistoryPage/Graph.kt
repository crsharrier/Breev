package com.example.breev.pages.HistoryPage

import android.util.Log
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.example.breev.home.TimerSessionViewModel

class BreevGraph(
    viewModel: TimerSessionViewModel
){


    @Composable
    fun DrawBreevGraph(){
        Text("Drawing Graph Here")
        Log.d("BreevGraph", "Drawing graph")
    }
}

