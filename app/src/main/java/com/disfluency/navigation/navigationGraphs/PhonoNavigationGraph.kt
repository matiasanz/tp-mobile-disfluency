package com.disfluency.navigation.navigationGraphs

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.disfluency.model.Phono
import com.disfluency.model.User
import com.disfluency.navigation.bottomNavigation.BottomNavigationItem
import com.disfluency.navigation.Route
import com.disfluency.screens.exercise.ExercisesScreen
import com.disfluency.screens.exercise.SingleExerciseScreen
import com.disfluency.screens.phono.PatientQuestionnairesScreen
import com.disfluency.screens.phono.PatientSessionsScreen
import com.disfluency.screens.patient.SinglePatientScreen
import com.disfluency.screens.phono.*

@Composable
fun PhonoNavigationGraph(navController: NavHostController, user: User, onLogout: () -> Unit) {
    NavHost(navController, startDestination = Route.HomePhono.route) {
        composable(BottomNavigationItem.HomePhono.screenRoute.route) {
            PhonoHomeScreen(onLogout)
        }
        composable(BottomNavigationItem.Pacientes.screenRoute.route) {
            PatientsScreen(navController, user.role as Phono)
        }
        composable(BottomNavigationItem.Ejercicios.screenRoute.route) {
            ExercisesScreen(navController)
        }
        composable(BottomNavigationItem.Cuestionarios.screenRoute.route) {
            PhonoQuestionnaireScreen()
        }
        composable(
            route = Route.Paciente.route,
            arguments = listOf(navArgument("id") {  })
        ) { backStackEntry -> //TODO: ver si hay forma de no tener que hacer el pasamanos de navController
            backStackEntry.arguments?.getString("id")?.let { SinglePatientScreen(id = it.toInt(), navController = navController) }
        }
        composable(
            route = Route.PatientExercises.route,
            arguments = listOf(navArgument("id") {  })
        ){ backStackEntry ->
            backStackEntry.arguments?.getString("id")?.let { PhonoExercisesScreen(id = it.toInt()) }
        }
        composable(
            route = Route.PatientQuestionnaires.route,
            arguments = listOf(navArgument("id") {  })
        ){ backStackEntry ->
            backStackEntry.arguments?.getString("id")?.let { PatientQuestionnairesScreen(id = it.toInt()) }
        }
        composable(
            route = Route.PatientSessions.route,
            arguments = listOf(navArgument("id") {  })
        ){ backStackEntry ->
            backStackEntry.arguments?.getString("id")?.let { PatientSessionsScreen(id = it.toInt()) }
        }
        composable(Route.NuevoPaciente.route) {
            FormNewPatient(navController, user.role as Phono)
        }

        composable(
            route = Route.Ejercicio.route,
            arguments = listOf(navArgument("id") {  })
        ){ backStackEntry ->
            backStackEntry.arguments?.getString("id")?.let { SingleExerciseScreen(id = it.toInt()) }
        }

        composable(Route.NuevoEjercicio.route) {
            FormNewExercise()
        }
    }
}