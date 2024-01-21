package com.example.breev.home

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.datastore.core.DataStore
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.breev.datastore.BreevSettings
import com.example.breev.pages.HistoryPage.BreevGraph
import com.example.breev.pages.SettingsPage.SettingsWidget
import kotlinx.coroutines.CoroutineScope

class BreevAppState(
    val navController: NavHostController,
    private val dataStore: DataStore<BreevSettings>,
    //private val resources: Resources,
    coroutineScope: CoroutineScope
) {
    val viewModel = TimerSessionViewModel(dataStore)
    val breevBlob = BreevBlob(viewModel)
    val settingsWidget = SettingsWidget(this)
    val graph = BreevGraph(viewModel)

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
    dataStore: DataStore<BreevSettings>,
    //resources: Resources = resources(),
    coroutineScope: CoroutineScope = rememberCoroutineScope()
) =
    remember(
        navController,
        //resources,
        coroutineScope
    ) {
        BreevAppState(
            navController,
            dataStore,
            //resources,
            coroutineScope
        )
    }