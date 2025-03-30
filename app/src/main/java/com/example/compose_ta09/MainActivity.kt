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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.WindowCompat
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavHostController
import androidx.navigation.compose.*
import androidx.navigation.navArgument
import com.example.app.ui.screens.HomeScreen
import com.example.compose_ta09.ui.theme.COMPOSE_TA09Theme
import com.example.compose_ta09.services.SharedPreferencesManager

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        WindowCompat.setDecorFitsSystemWindows(window, false)
        enableEdgeToEdge()
        setContent {
            COMPOSE_TA09Theme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MyApp()
                }
            }
        }
    }
}

@Composable
fun MyApp() {
    val context = LocalContext.current // Get the context here
    val walletAddress = SharedPreferencesManager.getUserWalletAddress(context) // Get wallet address from SharedPreferences
    val isLoggedIn = remember { mutableStateOf(SharedPreferencesManager.isUserLoggedIn(context)) }
    val navController = rememberNavController()

    // Navigate to the right screen based on login state
    if (isLoggedIn.value) {
        navController.navigate("main") // User is logged in
    } else {
        navController.navigate("connectMeta") // User is not logged in, connect wallet
    }

    NavHost(navController = navController, startDestination = "connectMeta") {
        composable("connectMeta") { ConnectMetaScreen(navController) }
        composable("main") { MainScreen(navController) }
        composable("register/{walletAddress}") { backStackEntry ->
            val walletAddress = backStackEntry.arguments?.getString("walletAddress")
            RegisterScreen(navController, walletAddress) // Pass walletAddress to RegisterScreen
        }
        composable("login/{walletAddress}") { backStackEntry ->
            val walletAddress = backStackEntry.arguments?.getString("walletAddress")
            LoginScreen(navController, walletAddress) // Pass walletAddress to LoginScreen
        }
        composable("tambah") { TambahScreenContent(navController) }
    }
}

@Composable
fun MainScreen(navController: NavHostController) {
    val bottomNavController = rememberNavController()

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .windowInsetsPadding(WindowInsets.systemBars),
        bottomBar = { BottomNavigationBar(bottomNavController, navController) }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            NavHost(navController = bottomNavController, startDestination = "home") {
                composable("home") { HomeScreen(bottomNavController) }
                composable("tambah") { TambahScreenContent(bottomNavController) } // Adjusted to call TambahScreenContent
                composable("profile") { ProfileScreen(navController) }
                composable(
                    "detail/{plantName}",
                    arguments = listOf(navArgument("plantName") { defaultValue = "" })
                ) { backStackEntry ->
                    val plantName = backStackEntry.arguments?.getString("plantName")
                    if (plantName != null) {
                        DetailScreen(plantName = plantName, onBack = { bottomNavController.popBackStack() })
                    } else {
                        Text("Error: Plant name not found")
                    }
                }
            }
        }
    }
}

@Composable
fun BottomNavigationBar(navController: NavHostController, mainNavController: NavHostController) {
    val items = listOf(
        BottomNavItem("home", "Home", Icons.Filled.Home),
        BottomNavItem("tambah", "Tambah", Icons.Filled.Add),
        BottomNavItem("profile", "Profile", Icons.Filled.Person)
    )

    val darkGreenColor = colorResource(id = R.color.dark_green)

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    // Hide BottomNav if we're in a detail page
    if (currentDestination?.hierarchy?.any { it.route == "detail/{plantName}" } == true) {
        return
    }

    NavigationBar(
        modifier = Modifier.fillMaxWidth(),
        containerColor = Color.White
    ) {
        items.forEach { item ->
            NavigationBarItem(
                modifier = Modifier.weight(1f),
                icon = { Icon(item.icon, contentDescription = item.label, tint = darkGreenColor) },
                label = { Text(item.label, color = darkGreenColor) },
                selected = currentDestination?.route == item.route,
                onClick = {
                    if (currentDestination?.route != item.route) {
                        navController.navigate(item.route) {
                            launchSingleTop = true
                            restoreState = true
                            popUpTo(navController.graph.startDestinationId) {
                                saveState = true
                            }
                        }
                    }
                }
            )
        }
    }
}

@Composable
fun ProfileScreen(navController: NavHostController) {
    val context = LocalContext.current
    val isLoggedIn = remember { mutableStateOf(SharedPreferencesManager.isUserLoggedIn(context)) }

    LaunchedEffect(Unit) {
        if (!isLoggedIn.value) {
            navController.navigate("connectMeta") {
                popUpTo("main") { inclusive = true }
            }
        }
    }

    if (!isLoggedIn.value) {
        // Show loading indicator if the user is not logged in yet
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    } else {
        // Show profile if user is logged in
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("Welcome to Profile Page")
            // Add Logout button here if needed
        }
    }
}

data class BottomNavItem(val route: String, val label: String, val icon: androidx.compose.ui.graphics.vector.ImageVector)
