package com.example.breathingtimer.Home

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.breathingtimer.Pages.HistoryPage.GraphBuilder
import com.example.breathingtimer.Pages.SettingsPage.SettingsWidget

class BreevAppState(
    val navController: NavHostController,
    //private val resources: Resources,
    //coroutineScope: CoroutineScope
) {
    val viewModel = TimerSessionViewModel()
    val breevBlob = BreevBlob(viewModel)
    val settingsWidget = SettingsWidget(this, navController)
    val graph = GraphBuilder(viewModel)

    // ----------------------------------------------------------
    // Navigation state source of truth
    // ----------------------------------------------------------

    val navBackStackEntry: NavBackStackEntry?
        @Composable get() = navController.currentBackStackEntryAsState().value
    val currentDestination: NavDestination?
        @Composable get() = navBackStackEntry?.destination

    // ----------------------------------------------------------
    // BottomBar state source of truth
    // ----------------------------------------------------------

    val appPages = listOf(AppPage.Timer, AppPage.History, AppPage.Settings)
    val currentPhase: SessionPhase
        @Composable get() = viewModel.currentPhase.collectAsState().value

    // Reading this attribute will cause recompositions when the bottom bar needs shown, or not.
    // Not all routes need to show the bottom bar.
    val shouldShowBottomBar: Boolean
        @Composable get() = currentPhase == SessionPhase.NOT_STARTED


}

@Composable
fun rememberBreevAppState(
    navController: NavHostController = rememberNavController(),
    //resources: Resources = resources(),
    //coroutineScope: CoroutineScope = rememberCoroutineScope()
) =
    remember(
        navController,
        //resources,
        //coroutineScope
    ) {
        BreevAppState(
            navController,
            //resources,
            //coroutineScope
        )
    }