package com.example.goosebuddy

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.goosebuddy.AppDatabase.Companion.createInstance
import com.example.goosebuddy.receivers.HabitsReceiver
import com.example.goosebuddy.ui.screens.*
import com.example.goosebuddy.ui.shared.components.bottomnavigation.BottomNavigation.BottomNavigation
import com.example.goosebuddy.ui.shared.components.bottomnavigation.BottomNavigation.BottomNavigationItem
import com.example.goosebuddy.ui.shared.components.topbar.TopBar
import com.example.goosebuddy.ui.theme.GooseBuddyTheme
import com.example.goosebuddy.ui.theme.Grey
import io.github.boguszpawlowski.composecalendar.rememberSelectableCalendarState
import java.time.LocalDateTime
import java.util.*


class MainActivity : ComponentActivity() {
    private val channelId = "channelId"
    private val channelName = R.string.channel_name.toString()
    private val notifyId = 0
    var receiver: HabitsReceiver? = null
    var intentFilter: IntentFilter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Creates a notification channel
        createNotificationChannel()
        val notificationManager: NotificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val alarmManager: AlarmManager =
            getSystemService(Context.ALARM_SERVICE) as AlarmManager

        receiver = HabitsReceiver()
        intentFilter = IntentFilter("com.example.goosebuddy.broadcastreceiver.SOME_ACTION")
        setContent {
            RootNavigationGraph(ctx = baseContext, channelId, notifyId, notificationManager, alarmManager)
        }
    }

    override fun onResume() {
        super.onResume()
        registerReceiver(receiver, intentFilter)
    }

    private fun  createNotificationChannel(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Create the NotificationChannel.
            val descriptionText = R.string.channel_description.toString()
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val mChannel = NotificationChannel(channelId, channelName, importance)
            mChannel.description = descriptionText
            // Register the channel with the system. You can't change the importance
            // or other notification behaviors after this.
            val notificationManager = applicationContext.getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(mChannel)
        }
    }

}

@Composable
fun MainFoundation(navController: NavHostController, scaffoldState: ScaffoldState, content: @Composable() () -> Unit) {
    GooseBuddyTheme {
        Scaffold(
            scaffoldState = scaffoldState,
            drawerContent = {
                Text("hello this is a drawer")
            },
            topBar = { TopBar(scaffoldState, navController) },
            bottomBar = { BottomNavigation(navController = navController) }
        ) { padding ->
            Surface(
                modifier = Modifier
                    .padding(padding)
                    .fillMaxSize()
                    .fillMaxHeight()
                    .background(Grey)
            ) {
                content()
            }
        }
    }
}

private fun setHabitsAlarm(alarmManager: AlarmManager, ctx: Context) {
    val currentTime = LocalDateTime.now()
    val updateTime: Calendar = Calendar.getInstance()
    updateTime.set(Calendar.HOUR_OF_DAY, currentTime.hour)
    updateTime.set(Calendar.MINUTE, currentTime.minute)
    updateTime.set(Calendar.SECOND, currentTime.second + 1)
    val intent = Intent(ctx, HabitsReceiver::class.java)
    val pendingIntent = PendingIntent.getBroadcast(
        ctx,
        0, intent, PendingIntent.FLAG_IMMUTABLE
    )
    alarmManager.setExact(
        AlarmManager.RTC_WAKEUP,
        updateTime.timeInMillis,
        pendingIntent
    )
}

@Composable
fun RootNavigationGraph(ctx: Context, channelId: String, notifyId: Int, notificationManager: NotificationManager, alarmManager: AlarmManager) {
    val navController = rememberNavController()
    val scaffoldState = rememberScaffoldState()
    val calendarState = rememberSelectableCalendarState()
    var db = createInstance(ctx)
    val calendarViewModel = CalendarViewModel(calendarState, navController, db)
    val testingLock = false
    var startDestination = "onboarding"
    if (testingLock) {
        startDestination = "lock"
    }
    setHabitsAlarm(alarmManager, ctx)
    NavHost(
        navController = navController,
        startDestination = startDestination,
        route = "main"
    ) {
        composable(BottomNavigationItem.Home.screen_route) {
            MainFoundation(navController = navController, scaffoldState = scaffoldState) {
                Home(db=db)
            }
        }
        composable(BottomNavigationItem.Habits.screen_route) {
            MainFoundation(navController = navController, scaffoldState = scaffoldState) {
                Habits(navController = navController, db=db, notificationManager, ctx=ctx)
            }
        }
        composable(BottomNavigationItem.DailyRoutines.screen_route) {
            MainFoundation(navController = navController, scaffoldState = scaffoldState) {
                Routines(navController = navController, db=db)
            }
        }
        composable(
            "routines/{routine_id}",
            arguments = listOf(navArgument("routine_id") { type = NavType.StringType })
        ) { backStackEntry ->
            val routineId = backStackEntry.arguments?.getString("routine_id")!!.toInt()
            MainFoundation(navController = navController, scaffoldState = scaffoldState) {
                Routine(
                    id = routineId,
                    navController = navController,
                    db = db
                )
            }
        }
        composable(
            "routines/{routine_id}/timer",
            arguments = listOf(navArgument("routine_id") { type = NavType.StringType })
        ) { backStackEntry ->
            val routineId = backStackEntry.arguments?.getString("routine_id")!!.toInt()
            MainFoundation(navController = navController, scaffoldState = scaffoldState) {
                RoutineTimer(
                    routineId,
                    db = db
                )
            }
        }
        composable(BottomNavigationItem.Calendar.screen_route) {
            MainFoundation(navController = navController, scaffoldState = scaffoldState) {
                Calendar(cvm = calendarViewModel)
            }
        }
        composable(calendarImportRoute) {
            val sivm = ScheduleImportViewModel()
            ScheduleImport(sivm = sivm, onSubmit = calendarViewModel::onSubmitCalendarImport)
        }
        composable(BottomNavigationItem.Profile.screen_route) {
            MainFoundation(navController = navController, scaffoldState = scaffoldState) {
                Profile(db=db)
            }
        }
        composable(
            "onboarding"
        ) {
            OnboardingFlow(navController = navController, db=db, cvm=calendarViewModel, 0)
        }
        composable("lock"){
            Lock(navController=navController, db=db)
        }
    }
}

@Composable
fun Greeting(name: String) {
    Text(text = "$name")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    GooseBuddyTheme {
        Greeting("Waterloo")
    }
}