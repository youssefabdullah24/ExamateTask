package com.example.examatetask.ui.home

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun HomeRoute(
    modifier: Modifier = Modifier,
    items: List<StudyUnit>
) {
    HomeScreen(
        modifier = modifier,
        items = items
    )
}

@Composable
fun HomeScreen(
    modifier: Modifier,
    items: List<StudyUnit>
) {
    StudyPlanColumn(
        modifier = modifier,
        items = items

    )
}

@Preview
@Composable
fun HomeScreenPreview() {
    HomeScreen(
        modifier = Modifier.fillMaxSize(),
        items = listOf(
            StudyUnit(id = 1, title = "Introduction to Mathematics", isLocked = false),
            StudyUnit(id = 2, title = "Advanced Algebra", isLocked = true),
            StudyUnit(id = 3, title = "Geometry Fundamentals", isLocked = true)
        ),
    )
}


@Composable
fun StudyPlanColumn(
    modifier: Modifier = Modifier
        .fillMaxSize()
        .padding(16.dp),
    items: List<StudyUnit>
) {

    LazyColumn(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        items(items.size) { index ->
            StudyUnitItem(
                modifier = Modifier.fillMaxSize(),
                studyUnit = items[index]
            )
        }
    }
}

@Composable
fun StudyUnitItem(
    modifier: Modifier,
    studyUnit: StudyUnit
) {
    val colorPrimary = if (studyUnit.isLocked) Color.Gray else MaterialTheme.colorScheme.primary
    val colorSecondary = if (studyUnit.isLocked) Color.LightGray else MaterialTheme.colorScheme.inversePrimary
    Card(modifier = modifier) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Card(
                modifier = Modifier.size(80.dp),
                shape = CircleShape,
                border = BorderStroke(6.dp, colorPrimary)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                        .clip(CircleShape)
                        .background(colorSecondary)
                ) {
                    if (studyUnit.isLocked) {
                        Icon(
                            imageVector = Icons.Default.Lock,
                            contentDescription = null,
                            tint = colorPrimary,
                            modifier = Modifier
                                .align(Alignment.Center)
                                .size(32.dp),
                        )
                    } else {
                        Text(
                            text = studyUnit.id.toString(),
                            modifier = Modifier
                                .align(Alignment.Center),
                            textAlign = TextAlign.Center,
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 32.sp,
                            color = colorPrimary
                        )
                    }
                }

            }
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                modifier = Modifier.basicMarquee(iterations = Int.MAX_VALUE),
                text = studyUnit.title,
                maxLines = 2,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                color = colorPrimary
            )
        }
    }
}


data class StudyUnit(val id: Int, val title: String, val isLocked: Boolean)

