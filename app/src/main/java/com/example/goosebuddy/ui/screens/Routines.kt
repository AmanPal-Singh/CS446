package com.example.goosebuddy.ui.screens

import android.widget.Space
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.semantics.Role.Companion.Checkbox
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.goosebuddy.ui.theme.Green
import com.example.goosebuddy.ui.theme.Grey
import com.example.goosebuddy.ui.theme.Red
import com.example.goosebuddy.ui.theme.White

/** Represent data using another structure - not sealed class */
sealed class RoutineItem(var title: String, var progress: Int) {

    fun completeRoutine() {
        this.progress = 100
    }
    object Skincare: RoutineItem("Skincare", 100)
    object Fitness: RoutineItem("Fitness", 75)
    object Yoga: RoutineItem("Yoga", 0)
    object Cleaning: RoutineItem("Cleaning", 50)
    object Study: RoutineItem("Study", 25)
}

class WeekdayData(
    val weekday: String,
    val completedCount: Int,
    val totalCount: Int
)

val mockWeekdayData = arrayOf(
    WeekdayData("S", 5, 10),
    WeekdayData("M", 5, 10),
    WeekdayData("T", 3, 10),
    WeekdayData("W", 5, 9),
    WeekdayData("Th", 5, 10),
    WeekdayData("F", 1, 10),
    WeekdayData("S", 5, 10),
)

val items = listOf(
    RoutineItem.Skincare,
    RoutineItem.Fitness,
    RoutineItem.Yoga,
    RoutineItem.Cleaning,
    RoutineItem.Study,
    RoutineItem.Study,
    RoutineItem.Study,
    RoutineItem.Study,
    RoutineItem.Study,
    RoutineItem.Study,
    RoutineItem.Study,
)

/** TODO: Make the weekly tracker stick to top? and make vertical scroll only on list of routines */
@Composable
fun Routines() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
            .background(Grey)
            .fillMaxHeight()
            .verticalScroll(rememberScrollState())
    ) {
        RoutineWeeklyTracker()
        items.forEach { item ->
            RoutineBlock(item = item)
        }
    }
}


/** Make padding part of theme */
@Composable
fun RoutineWeeklyTracker() {
    Card(
        shape = RoundedCornerShape(7.dp),
        modifier = Modifier
            .fillMaxWidth()
            .background(White)
            .padding(10.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 5.dp)
            ) {
                Text("Daily Routines", fontSize = 24.sp)
                Text("Weekly view")
            }
            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier.padding(10.dp)
                    .fillMaxWidth()
            ) {
                mockWeekdayData.forEach{ day ->
                    val totalHeight = 160
                    val completedHeight = totalHeight * day.completedCount/day.totalCount
                    val leftOverheight = totalHeight - completedHeight
                    Column {
                        Box(
                            modifier = Modifier
                                .background(Grey)
                                .height(leftOverheight.dp)
                                .width(10.dp)
                        )
                        Box(
                            modifier = Modifier
                                .background(Green)
                                .height(completedHeight.dp)
                                .width(10.dp)
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(day.weekday)
                    }
                }
            }
        }
    }

}

/** TODO: Put in componenets directory? */
@Composable
fun RoutineBlock(item: RoutineItem) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 10.dp)
        ) {
            Checkbox(
                checked = item.progress == 100,
                onCheckedChange = {
                    item.completeRoutine()
                }
            )
            Column {
                Text(item.title)
                Spacer(Modifier.height(10.dp))
                LinearProgressIndicator(progress = item.progress/100f)
                Spacer(Modifier.height(10.dp))
            }

            IconButton(onClick = { /*TODO*/ }) {
                Icon(
                    imageVector = Icons.Default.KeyboardArrowRight,
                    contentDescription = "More",
                    tint = Grey,
                    modifier = Modifier.size(50.dp)
                )
            }
        }
    }
}

@Preview
@Composable
fun RoutineBlockPreview() {
    RoutineBlock(items[0])
    RoutineBlock(items[1])
}