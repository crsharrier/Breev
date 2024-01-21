package com.example.breev.home

import android.view.LayoutInflater
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.view.ViewCompat
import androidx.datastore.core.DataStore
import com.example.breev.R
import com.example.breev.datastore.BreevSettings


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BreevApp(
    dataStore: DataStore<BreevSettings>
){
    val appState = rememberBreevAppState(dataStore = dataStore)
    val breevBlob = appState.breevBlob
    val settingsWidget = appState.settingsWidget


    val vSpacer by settingsWidget.vSpacer.collectAsState()

    Scaffold(
        modifier = Modifier.background(Color.Transparent),
        bottomBar = {
            if (appState.shouldShowBottomBar) {
                BreevBottomBar(
                    screens = appState.appPages,
                    navController = appState.navController,
                    currentDestination = appState.currentDestination
                )
            }
        }
    ){ contentPadding ->
        TorusBackground()

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Transparent)
                .padding(contentPadding),
            contentAlignment = Alignment.Center
        ) {

            breevBlob.DrawBlob(breevBlob.inner)
            breevBlob.DrawBlob(breevBlob.outer)
            BreevNavHost(appState)

            val vSpacerAnimation by animateDpAsState(
                targetValue = vSpacer,
                animationSpec = tween(500)
            )

            //Draw settings Widget
            Column (
                Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ){
                Spacer(Modifier.size(vSpacerAnimation))
                if (appState.currentDestination?.route != AppPage.History.route){
                    if (appState.currentPhase == SessionPhase.NOT_STARTED)
                        settingsWidget.DrawSettingsWidget(appState.viewModel)
                }
            }
        }
    }
}

@Composable
fun TorusBackground(){
    // Processing sketch background
    AndroidView(
        modifier = Modifier.fillMaxSize(),
        factory = { context ->
            LayoutInflater.from(context)
                .inflate(R.layout.processing_container, null)
                .apply {
                    // Nested Scroll Interop will be Enabled when
                    // nested scroll is enabled for the root view
                    ViewCompat.setNestedScrollingEnabled(this, true)
                }
        }
    )
}