package com.example.pam_florify.view.controllNavigasi

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.navigation.navArgument
import com.example.pam_florify.view.*
import com.example.pam_florify.view.route.*

@Composable
fun PetaNavigasi() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = DestinasiLogin.route) {

        // LOGIN
        composable(DestinasiLogin.route) {
            HalamanLogin(
                onLoginSuccess = { user ->
                    navController.navigate(DestinasiHome.createRoute(user.userId, user.username))
                },
                onRegisterClick = {
                    navController.navigate(DestinasiRegister.route)
                }
            )
        }

        // REGISTER
        composable(DestinasiRegister.route) {
            HalamanRegister(
                onRegisterSuccess = {
                    navController.navigate(DestinasiLogin.route) {
                        popUpTo(DestinasiLogin.route) { inclusive = true }
                    }
                },
                onLoginClick = {
                    navController.popBackStack()
                }
            )
        }

        // HOME
        composable(
            route = DestinasiHome.routeWithArgs,
            arguments = listOf(
                navArgument("userId") { type = NavType.IntType },
                navArgument("username") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val userId = backStackEntry.arguments?.getInt("userId") ?: 0
            val username = backStackEntry.arguments?.getString("username") ?: ""
            HalamanHome(
                userId = userId,
                username = username,
                onStart = { vm -> vm.getTanaman(userId) },
                navigateToItemEntry = { navController.navigate(DestinasiEntry.createRoute(userId)) },
                onDetailClick = { id -> navController.navigate(DestinasiDetail.createRoute(id)) },
                navigateToEditItem = { id -> navController.navigate(DestinasiEdit.createRoute(id)) },
                onLogoutClick = {
                    navController.navigate(DestinasiLogin.route) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }

        // DETAIL TANAMAN
        composable(
            route = DestinasiDetail.routeWithArgs,
            arguments = listOf(navArgument("tanamanId") { type = NavType.IntType })
        ) {
            HalamanDetail(
                navigateBack = { navController.popBackStack() },
                navigateToEditItem = { id -> navController.navigate(DestinasiEdit.createRoute(id)) }
            )
        }

        // EDIT TANAMAN
        composable(
            route = DestinasiEdit.routeWithArgs,
            arguments = listOf(navArgument("tanamanId") { type = NavType.IntType })
        ) {
            HalamanEdit(
                onNavigateUp = { navController.navigateUp() }
            )
        }

        composable(
            route = DestinasiEntry.routeWithArgs,
            arguments = listOf(navArgument("userId") { type = NavType.IntType })
        ) { backStackEntry ->
            val userId = backStackEntry.arguments?.getInt("userId") ?: 0
            HalamanEntry(
                userId = userId,
                navigateBack = { navController.popBackStack() }
            )
        }
    }
}