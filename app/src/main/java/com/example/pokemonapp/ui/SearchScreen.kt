package com.example.pokemonapp.ui

import android.util.Log
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.pokemonapp.ui.views.pokemonInfoScreen.PokemonInfo
import com.example.pokemonapp.ui.views.pokemonInfoScreen.PokemonInfoViewModel
import com.example.pokemonapp.ui.views.searchInfoScreen.PokemonList
import com.example.pokemonapp.ui.views.searchInfoScreen.SearchPokemonViewModel
import java.util.Locale
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ListItem
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.Lifecycle
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navigation
import com.example.pokemonapp.ui.theme.RetrofitTestTheme
import com.example.pokemonapp.ui.views.PokemonInfo
import com.example.pokemonapp.ui.views.PokemonList
import kotlinx.coroutines.launch
import com.example.pokemonapp.R
import com.example.pokemonapp.ui.views.Login
import com.example.pokemonapp.ui.views.PokemonDestination
import com.example.pokemonapp.ui.views.PokemonScreens
import com.example.pokemonapp.ui.views.Welcome
import com.example.pokemonapp.ui.views.login.LoginScreen
import com.example.pokemonapp.ui.views.login.LoginViewModel
import com.example.pokemonapp.ui.views.login.WelcomeScreen

@Composable
fun PokemonApp(
    navController: NavHostController = rememberNavController()
) {
    val tabs = remember { PokemonScreens }
    val currentBackStack by navController.currentBackStackEntryAsState()
    val currentDestination = currentBackStack?.destination
    val currentScreen = PokemonScreens.find { it.route == currentDestination?.route} ?: Welcome

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    RetrofitTestTheme {
        ModalNavigationDrawer(
            drawerState = drawerState,
            drawerContent = {
                DrawerMenu(
                    onItemClick = {
                        navController.navigateSingleTopTo(it)
                        scope.launch { drawerState.close() }
                    }
                )
            }
        ) {
            Scaffold(
                topBar = {
                    PokemonTopBar(
                        currentScreen = currentScreen,
                        drawerButtonOnClick = { scope.launch { drawerState.open() } },
                    )
                },
                bottomBar = {
                    PokemonBottomBar(
                        currentScreen = currentScreen,
                        navController = navController,
                        tabs
                    )
                }
            ) { innerPadding ->
                //PokemonNavigationGraph(
                //    modifier = Modifier.padding(innerPadding)
                //)

                NavHost(
                    navController = navController,
                    startDestination = Welcome.route,
                    modifier = Modifier
                        //.fillMaxSize()
                        //.verticalScroll(rememberScrollState())
                        .padding(innerPadding)
                ) {
                    composable(
                        route = Welcome.route
                    ) {
                        WelcomeScreen(
                            onSignInSignUp = {
                                navController.navigateSingleTopTo(Login.route)
                            },
                            onSignInAsGuest = {
                                navController.navigateSingleTopTo(Login.route)
                            }
                        )
                    }
                    composable(
                        route = Login.route
                    ) {
                        //val loginViewModel: LoginViewModel = viewModel(factory = LoginViewModel.Factory)
                        LoginScreen(
                            onLoginClick = { email, password ->
                                navController.navigateSingleTopTo(Login.route)
                            },
                            onSignInAsGuest = {
                                navController.navigateSingleTopTo(Login.route)
                            }
                            //loginViewModel = loginViewModel
                        )
                    }
                    composable(
                        route = PokemonList.route
                    ) {
                        val searchPokemonViewModel: SearchPokemonViewModel = viewModel(factory = SearchPokemonViewModel.Factory)
                        PokemonList(
                            viewModel = searchPokemonViewModel,
                            onPokemonSelected = { pokemon ->
                                navController.navigate("pokemonInfo/${pokemon.name}")
                            }
                        )
                    }
                    composable(
                        route = PokemonInfo.route,
                        arguments = PokemonInfo.arguments
                    ) { backStackEntry ->
                        val pokemonId = backStackEntry.arguments?.getString("pokemonName")
                        if (pokemonId != null) {
                            val pokemonInfoViewModel: PokemonInfoViewModel = viewModel(factory = PokemonInfoViewModel.Factory)
                            PokemonInfo(
                                viewModel = pokemonInfoViewModel,
                                pokemonName = pokemonId
                            )
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PokemonTopBar(
    currentScreen: PokemonDestination,
    drawerButtonOnClick: () -> Unit,
) {
    TopAppBar(
        title = { Text(text = currentScreen.route) },
        navigationIcon = {
            IconButton(onClick = drawerButtonOnClick) {
                Icon(
                    Icons.Filled.Menu,
                    contentDescription = "Open Drawer"
                )
            }
        }
    )
}

@Composable
fun PokemonBottomBar(
    currentScreen: PokemonDestination,
    navController: NavController,
    tabs: List<PokemonDestination> = PokemonScreens
) {
    BottomAppBar(
        actions = {
            tabs.forEach {
                IconButton(onClick = { /* do something */ }) {
                    Icon(Icons.Filled.Add, contentDescription = "Localized description")
                }
            }
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { /* do something */ },
                containerColor = BottomAppBarDefaults.bottomAppBarFabColor,
                elevation = FloatingActionButtonDefaults.bottomAppBarFabElevation()
            ) {
                Icon(Icons.Filled.Favorite, "Localized description")
            }
        }
    )
}

@Composable
fun DrawerMenu(
    onItemClick: (String) -> Unit
) {
    Column (
        modifier = Modifier
            .background(Color.Gray)
            .fillMaxHeight()
            .fillMaxWidth(0.7f)
    ) {
        DrawerMenuItem(
            icon = Icons.Filled.Add,
            onClick = { onItemClick("login") },
            text = "pokemonList"
        )
        DrawerMenuItem(
            icon = Icons.Filled.Add,
            onClick = { onItemClick("pokemonList") },
            text = "pokemonList"
        )
    }
}

@Composable
fun DrawerMenuItem(
    icon: ImageVector,
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .clickable(onClick = onClick)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null, // Add content description if needed
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = text,
            modifier = Modifier.clickable(onClick = onClick)
        )
    }
}




















@Composable
fun PokemonNavigationGraph(
    modifier: Modifier = Modifier,
    finishActivity: () -> Unit = {},
    navController: NavHostController = rememberNavController(),
    startDestination: String = PokemonList.route,
    showOnboardingInitially: Boolean = true
) {
    val onboardingComplete = remember(showOnboardingInitially) {
        mutableStateOf(!showOnboardingInitially)
    }

    val actions = remember(navController) { MainActions(navController) }

    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        composable(PokemonList.route) {
            val searchPokemonViewModel: SearchPokemonViewModel = viewModel(factory = SearchPokemonViewModel.Factory)
            PokemonList(
                viewModel = searchPokemonViewModel,
                onPokemonSelected = { pokemon ->
                    navController.navigate("pokemonInfo/${pokemon.name}")
                }
            )
        }
        composable(
            route = PokemonInfo.route,
            arguments = listOf(navArgument("pokemonName") { type = NavType.StringType })
        ) { backStackEntry ->
            val pokemonId = backStackEntry.arguments?.getString("pokemonName")
            if (pokemonId != null) {
                val pokemonInfoViewModel: PokemonInfoViewModel = viewModel(factory = PokemonInfoViewModel.Factory)
                PokemonInfo(viewModel = pokemonInfoViewModel, pokemonName = pokemonId)
            }
        }
    }
}

class MainActions(navController: NavHostController) {
    val onboardingComplete: () -> Unit = {
        navController.popBackStack()
    }

    // Used from COURSES_ROUTE
    val openCourse = { newCourseId: Long, from: NavBackStackEntry ->
        // In order to discard duplicated navigation events, we check the Lifecycle
        if (from.lifecycleIsResumed()) {
            //navController.navigate(PokemonDestinations.POKEMONINFO_ROUTE)
        }
    }

    // Used from COURSE_DETAIL_ROUTE
    val relatedCourse = { newCourseId: Long, from: NavBackStackEntry ->
        // In order to discard duplicated navigation events, we check the Lifecycle
        if (from.lifecycleIsResumed()) {
            //navController.navigate(PokemonDestinations.POKEMONINFO_ROUTE)
        }
    }

    // Used from COURSE_DETAIL_ROUTE
    val upPress: (from: NavBackStackEntry) -> Unit = { from ->
        // In order to discard duplicated navigation events, we check the Lifecycle
        if (from.lifecycleIsResumed()) {
            navController.navigateUp()
        }
    }
}

private fun NavBackStackEntry.lifecycleIsResumed() =
    this.lifecycle.currentState == Lifecycle.State.RESUMED





@Composable
private fun PokemonTab(
    text: String,
    icon: ImageVector,
    onSelected: () -> Unit,
    selected: Boolean
) {
    val color = MaterialTheme.colorScheme.onSurface
    val durationMillis = if (selected) TabFadeInAnimationDuration else TabFadeOutAnimationDuration
    val animSpec = remember {
        tween<Color>(
            durationMillis = durationMillis,
            easing = LinearEasing,
            delayMillis = TabFadeInAnimationDelay
        )
    }
    val tabTintColor by animateColorAsState(
        targetValue = if (selected) color else color.copy(alpha = 0.60f),
        animationSpec = animSpec,
        label = ""
    )
    Row(
        modifier = Modifier
            .padding(16.dp)
            .animateContentSize()
            .height(56.dp)
            .selectable(
                selected = selected,
                onClick = onSelected,
                role = Role.Tab,
                interactionSource = remember { MutableInteractionSource() },
                indication = rememberRipple(
                    bounded = false,
                    radius = Dp.Unspecified,
                    color = Color.Unspecified
                )
            )
            .clearAndSetSemantics { contentDescription = text }
    ) {
        Icon(imageVector = icon, contentDescription = text, tint = tabTintColor)
        if (selected) {
            Spacer(Modifier.width(12.dp))
            Text(text.uppercase(Locale.getDefault()), color = tabTintColor)
        }
    }
}

fun NavHostController.navigateSingleTopTo(route: String) =
    this.navigate(route) { launchSingleTop = true }

private fun NavHostController.navigateToSingleAccount(accountType: String) {
    //this.navigateSingleTopTo("${Screen4.route}/$accountType")
}

private const val TabFadeInAnimationDuration = 150
private const val TabFadeInAnimationDelay = 100
private const val TabFadeOutAnimationDuration = 100