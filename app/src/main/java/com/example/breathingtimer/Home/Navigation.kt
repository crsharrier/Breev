package com.example.breathingtimer.Home

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.breathingtimer.Pages.HistoryPage.HistoryPage
import com.example.breathingtimer.Pages.SettingsPage.SettingsPage
import com.example.breathingtimer.Pages.TimerSession.TimerSession

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