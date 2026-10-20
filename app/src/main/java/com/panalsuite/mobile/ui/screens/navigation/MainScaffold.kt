package com.panalsuite.mobile.ui.navigation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.panalsuite.mobile.R
import com.panalsuite.mobile.data.local.entity.UserEntity
import com.panalsuite.mobile.ui.screens.common.PlaceholderScreen
import com.panalsuite.mobile.ui.screens.profile.ProfileScreen
import com.panalsuite.mobile.ui.theme.Navy

private const val DEMO_TICKET_ID = 1L

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScaffold(
    user: UserEntity,
    onLogout: () -> Unit
) {
    val navController = rememberNavController()
    val tabs = remember(user.role) { AppDestination.tabsFor(user.role) }
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route
    val isDetail = currentRoute == Routes.TICKET_DETAIL
    val showBottomBar = tabs.any { it.route == currentRoute }

    val title = if (isDetail) {
        stringResource(R.string.nav_ticket_detail)
    } else {
        tabs.firstOrNull { it.route == currentRoute }
            ?.let { stringResource(it.labelRes) }
            .orEmpty()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(title) },
                navigationIcon = {
                    if (isDetail) {
                        IconButton(onClick = { navController.popBackStack() }) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = stringResource(R.string.nav_back)
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Navy,
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
                )
            )
        },
        bottomBar = {
            AnimatedVisibility(
                visible = showBottomBar,
                enter = slideInVertically(tween(250)) { it },
                exit = slideOutVertically(tween(250)) { it }
            ) {
                NavigationBar(containerColor = MaterialTheme.colorScheme.surface) {
                    tabs.forEach { tab ->
                        NavigationBarItem(
                            selected = currentRoute == tab.route,
                            onClick = {
                                navController.navigate(tab.route) {
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            },
                            icon = { Icon(imageVector = tab.icon, contentDescription = null) },
                            label = { Text(stringResource(tab.labelRes)) },
                            colors = NavigationBarItemDefaults.colors(
                                indicatorColor = MaterialTheme.colorScheme.tertiary,
                                selectedIconColor = MaterialTheme.colorScheme.onTertiary,
                                selectedTextColor = MaterialTheme.colorScheme.onSurface,
                                unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                                unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = tabs.first().route,
            modifier = Modifier.padding(innerPadding),
            enterTransition = { fadeIn(tween(200)) + slideInHorizontally(tween(250)) { it / 10 } },
            exitTransition = { fadeOut(tween(150)) },
            popEnterTransition = { fadeIn(tween(200)) },
            popExitTransition = { fadeOut(tween(150)) + slideOutHorizontally(tween(250)) { it / 10 } }
        ) {
            tabs.forEach { tab ->
                composable(tab.route) {
                    if (tab == AppDestination.PROFILE) {
                        ProfileScreen(user = user, onLogout = onLogout)
                    } else {
                        PlaceholderScreen(
                            title = stringResource(tab.labelRes),
                            message = stringResource(R.string.placeholder_message),
                            actionLabel = stringResource(R.string.placeholder_open_demo),
                            onAction = { navController.navigate(Routes.ticketDetail(DEMO_TICKET_ID)) }
                        )
                    }
                }
            }
            composable(
                route = Routes.TICKET_DETAIL,
                arguments = listOf(navArgument(Routes.TICKET_ID_ARG) { type = NavType.LongType })
            ) { entry ->
                val ticketId = entry.arguments?.getLong(Routes.TICKET_ID_ARG) ?: 0L
                PlaceholderScreen(
                    title = stringResource(R.string.ticket_detail_title, ticketId),
                    message = stringResource(R.string.placeholder_message)
                )
            }
        }
    }
}