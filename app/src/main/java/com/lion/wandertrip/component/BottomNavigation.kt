package com.example.bottomnav

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.rememberNavController
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.ui.Modifier
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.EditCalendar
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PersonOutline
import androidx.compose.material.icons.filled.Settings
import androidx.navigation.compose.currentBackStackEntryAsState

@Composable
fun BottomNavigation(navController: androidx.navigation.NavController, items: List<BottomNavItem>) {
    NavigationBar {
        val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route
        items.forEach { item ->
            NavigationBarItem(
                icon = { Icon(imageVector = item.icon, contentDescription = item.label, modifier = Modifier.size(24.dp)) },
                label = { Text(item.label) },
                selected = currentRoute == item.route,
                onClick = {
                    if (currentRoute != item.route) {
                        navController.navigate(item.route) {
                            popUpTo(navController.graph.startDestinationId) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun BottomNavigationPreview() {
    val navController = rememberNavController()
    val items = listOf(
        BottomNavItem("홈", Icons.Filled.Home, "home"),
        BottomNavItem("일정", Icons.Filled.CalendarMonth, "profile"),
        BottomNavItem("여행기", Icons.Filled.EditCalendar, "settings"),
        BottomNavItem("내정보", Icons.Filled.PersonOutline, "Test2Screen")
    )

    MaterialTheme { // 🔹 MyTheme 대신 MaterialTheme 사용
        BottomNavigation(navController = navController, items = items)
    }
}

data class BottomNavItem(
    val label: String,
    val icon: ImageVector,
    val route: String
)