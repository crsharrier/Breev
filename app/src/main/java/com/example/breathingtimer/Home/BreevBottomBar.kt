package com.example.breathingtimer.Home

import androidx.annotation.StringRes
import androidx.compose.animation.core.SpringSpec
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Build
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.breathingtimer.R

sealed class AppPage(
    val route: String,
    @StringRes val resourceId: Int,
    val icon: ImageVector
    ) {
    object Timer : AppPage("Timer", R.string.timer, Icons.Outlined.LocationOn)
    object History : AppPage("History", R.string.history, Icons.Outlined.Build)
    object Settings : AppPage("Settings", R.string.settings, Icons.Outlined.Settings)
}

@Composable
fun BreevBottomBar(
    screens: List<AppPage>,
    navController: NavController,
    currentDestination: NavDestination?,
    bottomBarHeight: Dp = 90.dp,
    color: Color = Color.Blue,
    contentColor: Color = Color.White
){
    val springSpec = SpringSpec<Float>(
        // Determined experimentally
        stiffness = 800f,
        dampingRatio = 0.8f
    )

    NavigationBar() {
        screens.forEach{ screen ->
            NavigationBarItem(
                icon = {
                    Icon(
                        imageVector = screen.icon,
                        contentDescription = null
                    )
                },
                label = { Text(stringResource(screen.resourceId)) },
                selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                onClick = {
                    navController.navigate(screen.route) {
                        // Pop up to the start destination of the graph to
                        // avoid building up a large stack of destinations
                        // on the back stack as users select items
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        // Avoid multiple copies of the same destination when
                        // reselecting the same item
                        launchSingleTop = true
                        // Restore state when reselecting a previously selected item
                        restoreState = true
                    }
                }
            )
        }
    }
}


@Composable
fun BottomBarItem(
    label: String,
    icon: ImageVector
){

}

