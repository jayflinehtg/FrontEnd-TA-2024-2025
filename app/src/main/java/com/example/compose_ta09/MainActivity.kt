package com.example.compose_ta09

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.*
import com.example.app.ui.screens.HomeScreen
import com.example.compose_ta09.ui.theme.COMPOSE_TA09Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            COMPOSE_TA09Theme {
                MyApp()
            }
        }
    }
}

@Composable
fun MyApp() {
    val navController = rememberNavController()
    var isLoggedIn by remember { mutableStateOf(false) }
    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = currentBackStackEntry?.destination?.route

    val showBottomBar = currentRoute in listOf("home", "addData", "profile")

    Scaffold { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            NavigationGraph(navController, isLoggedIn) { isLoggedIn = true }
        }
    }
}

@Composable
fun BottomNavigationBar(navController: NavHostController, isLoggedIn: Boolean) {
    val items = listOf(
        Screen.Home, Screen.AddData, Screen.Profile
    )
    var selectedRoute by remember { mutableStateOf("home") }

    NavigationBar(containerColor = Color.White) {
        items.forEach { screen ->
            val isSelected = selectedRoute == screen.route
            NavigationBarItem(
                icon = {
                    Icon(
                        screen.icon,
                        contentDescription = screen.route,
                        tint = if (isSelected) Color(0xFF006400) else Color.Gray
                    )
                },
                label = { Text(screen.title) },
                selected = isSelected,
                onClick = {
                    selectedRoute = screen.route
                    if (screen is Screen.Profile && !isLoggedIn) {
                        navController.navigate("login") {
                            popUpTo("home") { inclusive = false }
                            launchSingleTop = true
                        }
                    } else {
                        navController.navigate(screen.route) {
                            popUpTo("home") { inclusive = false }
                            launchSingleTop = true
                        }
                    }
                }
            )
        }
    }
}

@Composable
fun NavigationGraph(
    navController: NavHostController,
    isLoggedIn: Boolean,
    onLoginSuccess: () -> Unit
) {
    NavHost(navController, startDestination = "connectMeta") {
        composable("connectMeta") { ConnectMetaScreen(navController) }
        composable("home") { HomeScreen(navController) }
        composable("addData") { TambahScreen(navController) }
        composable("profile") { ProfileScreen() }
        composable("login") { LoginScreen(navController) }
        composable("register") { RegisterScreen(navController) }
        composable("detail/{plant}") { backStackEntry ->
            val plant = backStackEntry.arguments?.getString("plant") ?: ""
            DetailScreen(plant, onBack = {navController.popBackStack()})
        }
    }
}

@Composable
fun ProfileScreen() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text("Profile Screen")
    }
}

sealed class Screen(val route: String, val title: String, val icon: ImageVector) {
    object Home : Screen("home", "Beranda", Icons.Filled.Home)
    object AddData : Screen("addData", "Tambah Data", Icons.Filled.Add)
    object Profile : Screen("profile", "Saya", Icons.Filled.Person)
}

@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
    COMPOSE_TA09Theme {
        MyApp()
    }
}


@Composable
fun ProfileScreen(navController: NavHostController) {
    LaunchedEffect(Unit) {
        navController.navigate("connectMeta") {
            popUpTo("main") { inclusive = true }
        }
    }

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        CircularProgressIndicator()
    }
}

data class BottomNavItem(val route: String, val label: String, val icon: androidx.compose.ui.graphics.vector.ImageVector)
