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
import androidx.core.view.WindowCompat
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavHostController
import androidx.navigation.compose.*
import com.example.compose_ta09.ui.theme.COMPOSE_TA09Theme
import com.example.compose_ta09.ui.viewModels.EventSinkMetaMask
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
    val context = LocalContext.current // Mendapatkan context
    val jwtToken = SharedPreferencesManager.getUserWalletAddress(context) // Mengambil wallet address dari SharedPreferences
    val isLoggedIn = remember { mutableStateOf(SharedPreferencesManager.isUserLoggedIn(context)) }
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = if (isLoggedIn.value) "main" else "connectMeta") {
        composable("connectMeta") {
            ConnectMetaScreen(
                navController = navController,
                eventSink = { event ->
                    // Tangani event terkait koneksi MetaMask di ConnectMetaScreen
                    when (event) {
                        is EventSinkMetaMask.Connect -> {
                            // Handle connect logic
                        }
                        is EventSinkMetaMask.Disconnect -> {
                            // Handle disconnect logic
                        }
                        is EventSinkMetaMask.WalletConnected -> {
                            // Handle wallet connected logic
                        }
                        is EventSinkMetaMask.ConnectionFailed -> {
                            // Handle connection failed logic
                        }
                    }
                }
            )
        }
        composable("main") {
            MainScreen(
                navController = navController,
                jwtToken = jwtToken,
                eventSink = { event ->
                    // Tangani event terkait koneksi MetaMask di MainScreen
                    when (event) {
                        is EventSinkMetaMask.Connect -> {
                            // Handle connect logic
                        }
                        is EventSinkMetaMask.Disconnect -> {
                            // Handle disconnect logic
                        }
                        is EventSinkMetaMask.WalletConnected -> {
                            // Handle wallet connected logic
                        }
                        is EventSinkMetaMask.ConnectionFailed -> {
                            // Handle connection failed logic
                        }
                    }
                }
            )
        }
        composable("profile") {
            ProfileScreen(
                navController = navController,
                jwtToken = jwtToken,
                eventSink = { event ->
                    // Tangani event terkait koneksi MetaMask di ProfileScreen
                    when (event) {
                        is EventSinkMetaMask.Connect -> {
                            // Handle connect logic
                        }
                        is EventSinkMetaMask.Disconnect -> {
                            // Handle disconnect logic
                        }
                        is EventSinkMetaMask.WalletConnected -> {
                            // Handle wallet connected logic
                        }
                        is EventSinkMetaMask.ConnectionFailed -> {
                            // Handle connection failed logic
                        }
                    }
                }
            )
        }
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
fun MainScreen(navController: NavHostController, jwtToken: String?, eventSink: (EventSinkMetaMask) -> Unit) {
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
                composable("home") { HomeScreen(bottomNavController, jwtToken) } // Pass jwtToken
                composable("tambah") { TambahScreenContent(bottomNavController) }
                composable("profile") {
                    ProfileScreen(
                        navController = navController,
                        jwtToken = jwtToken,
                        eventSink = eventSink  // Tambahkan parameter eventSink
                    )
                } // Pass jwtToken
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

data class BottomNavItem(val route: String, val label: String, val icon: androidx.compose.ui.graphics.vector.ImageVector)
