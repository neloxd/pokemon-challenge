package com.jesusvilla.pokemon.challenge.ui.nav

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.jesusvilla.pokemon.challenge.ui.detail.DetailScreen
import com.jesusvilla.pokemon.challenge.ui.favorites.FavoritesScreen
import com.jesusvilla.pokemon.challenge.ui.home.HomeScreen

object Routes {
    const val HOME = "home"
    const val DETAIL = "detail"
    const val FAVORITES = "favorites"
}

@Composable
fun AppNavGraph() {
    val nav = rememberNavController()

    NavHost(navController = nav, startDestination = Routes.HOME) {

        composable(Routes.HOME) {
            HomeScreen(
                onOpenDetail = { id -> nav.navigate("${Routes.DETAIL}/$id") }
            )
        }

        composable(Routes.FAVORITES) {
            FavoritesScreen(
                onBack = { nav.popBackStack() },
                onOpenDetail = { id -> nav.navigate("${Routes.DETAIL}/$id") }
            )
        }

        composable(
            route = "${Routes.DETAIL}/{id}",
            arguments = listOf(navArgument("id") { type = NavType.IntType })
        ) { backStack ->
            val id = backStack.arguments?.getInt("id") ?: 1
            DetailScreen(
                id = id,
                onBack = { nav.popBackStack() },
                onNavigateTo = { nextId -> nav.navigate("${Routes.DETAIL}/$nextId") }
            )
        }
    }
}
