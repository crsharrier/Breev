package com.example.breev.home

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.breev.pages.HistoryPage.HistoryPage
import com.example.breev.pages.SettingsPage.SettingsPage
import com.example.breev.pages.TimerSession.TimerSession

@Composable
fun BreevNavHost(
    appState: BreevAppState
) {
    val navController = appState.navController
    NavHost(
        navController = navController,
        startDestination = "Timer"
    ) {
        composable("Timer") {
            TimerSession(appState)
        }
        composable("History") {
            HistoryPage(appState)
        }
        composable("Settings") {
            SettingsPage(appState)
        }
    }
}