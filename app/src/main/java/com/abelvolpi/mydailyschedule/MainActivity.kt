package com.abelvolpi.mydailyschedule

import android.R.attr.type
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.abelvolpi.mydailyschedule.ui.add.AddTaskScreen
import com.abelvolpi.mydailyschedule.ui.edit.EditTaskScreen
import com.abelvolpi.mydailyschedule.ui.main.MainScreen
import com.abelvolpi.mydailyschedule.ui.theme.MyDailyScheduleTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyDailyScheduleTheme(darkTheme = true) {
                MyDailyScheduleApp()
            }
        }
    }
}

@Composable
fun MyDailyScheduleApp() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "main"
    ) {
        composable("main") {
            MainScreen(
                onAddTask = { navController.navigate("add_task") },
                onEditTask = { id -> navController.navigate("edit_task/$id") }
            )
        }

        composable("add_task") {
            AddTaskScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable(
            route = "edit_task/{taskId}",
            arguments = listOf(navArgument("taskId") { type = NavType.IntType })
        ) { backStackEntry ->
            val taskId = backStackEntry.arguments?.getInt("taskId") ?: return@composable
            EditTaskScreen(
                taskId = taskId,
                onNavigateBack = { navController.popBackStack() }
            )
        }
    }
}
