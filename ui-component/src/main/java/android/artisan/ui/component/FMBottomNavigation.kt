package android.artisan.ui.component

import android.artisan.ui.compositionlocal.LocalNavController
import android.artisan.ui.theme.extraColors
import android.artisan.ui.theme.extraShape
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.navOptions
import java.util.Locale

/**
 * FMBottomNavigation is a Composable function that renders a custom bottom navigation bar.
 * It dynamically shows/hides based on the current destination route and handles navigation
 * between different screens defined in the [navigationBarScreens] list.
 *
 * @param currentDestinationRoute The route of the currently displayed screen. Used to determine
 *                                whether the bottom navigation should be visible and to select
 *                                the appropriate navigation item.
 * @param navigationBarScreens A list of [NavigationBarScreen] objects, each representing a
 *                               screen that can be navigated to via the bottom navigation.
 * @param selectedNavigationItemIndex The index of the currently selected navigation item.
 * @param onNavigationItemSelection A callback function that is invoked when a navigation item
 *                                is selected. It receives the index of the selected item.
 * @param modifier Modifier for customizing the appearance and layout of the bottom navigation.
 *
 * The bottom navigation bar is only visible if the [currentDestinationRoute] matches at least
 * one of the routes defined in the [navigationBarScreens].
 *
 * Each item in the navigation bar displays an icon and a label. The icon is a filled circle if
 * the item is selected, or the regular icon resource defined in [NavigationBarScreen */
@Composable
internal fun FMBottomNavigation(
    currentDestinationRoute: String?,
    navigationBarScreens: List<NavigationBarScreen>,
    selectedNavigationItemIndex: Int,
    onNavigationItemSelection: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val navHost = LocalNavController.current
    AnimatedVisibility(
        visible = navigationBarScreens.any { currentDestinationRoute?.contains(it.getSerializedRoute()) == true },
        enter = slideInVertically(initialOffsetY = { it }),
        exit = slideOutVertically(targetOffsetY = { it })
    ) {
        BottomNavigation(
            modifier = modifier
                .padding(start = 16.dp, end = 16.dp, bottom = 16.dp)
                .clip(MaterialTheme.extraShape.capsuleShape)
        ) {
            navigationBarScreens.forEachIndexed { index, screen ->
                if (currentDestinationRoute?.contains(screen.getSerializedRoute()) == true &&
                    index != selectedNavigationItemIndex
                ) {
                    onNavigationItemSelection(index)
                }
                BottomNavigationItem(
                    selected = selectedNavigationItemIndex == index,
                    icon = {
                        if (selectedNavigationItemIndex != index) {
                            Icon(
                                modifier = Modifier.size(24.dp),
                                painter = painterResource(id = screen.iconResourceId),
                                contentDescription = null
                            )
                        } else {
                            Box(
                                modifier = Modifier
                                    .size(24.dp)
                                    .padding(8.dp)
                                    .clip(CircleShape)
                                    .background(MaterialTheme.extraColors.selectedNavigationItemColor)
                            )
                        }
                    },
                    label = {
                        Text(
                            text = stringResource(id = screen.labelResourceId).uppercase(Locale.getDefault()),
                            overflow = TextOverflow.Visible,
                            softWrap = false,
                            maxLines = 1
                        )
                    },
                    onClick = {
                        navigateToDestinations(
                            index = index,
                            navigationBarScreen = screen,
                            navController = navHost,
                            currentDestinationRoute = currentDestinationRoute,
                            onNavigationItemSelected = onNavigationItemSelection
                        )
                    },
                    alwaysShowLabel = false
                )
            }
        }
    }
}

internal fun navigateToDestinations(
    index: Int,
    navigationBarScreen: NavigationBarScreen,
    navController: NavController,
    currentDestinationRoute: String?,
    onNavigationItemSelected: (Int) -> Unit
) {
    if (currentDestinationRoute != navigationBarScreen.route) {
        onNavigationItemSelected(index)
        val topLevelNavOptions = navOptions {
            popUpTo(navController.graph.findStartDestination().id) {
                saveState = true
            }
            launchSingleTop = true
            restoreState = true
        }

        navController.navigate(
            route = navigationBarScreen.route,
            navOptions = topLevelNavOptions
        )
    }
}
