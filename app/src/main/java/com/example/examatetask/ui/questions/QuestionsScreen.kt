package com.example.examatetask.ui.questions

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.boundsInParent
import androidx.compose.ui.layout.boundsInRoot
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import com.example.examatetask.ArrowPosition
import com.example.examatetask.Spotlight
import com.example.examatetask.drawBottomTriangle
import kotlinx.coroutines.launch

@Composable
fun QuestionsRoute(
    modifier: Modifier,
    questionsCategories: List<QuestionsCategory>,
    oralCategories: List<QuestionOral>
) {
    QuestionsScreen(
        modifier = modifier,
        questionsCategories = questionsCategories,
        questionOrals = oralCategories
    )
}


data class QuestionsCategory(
    val questionsNum: Int,
    val answeredQuestions: Int,
    val categoryName: String
)

@Composable
fun ToolTip2(
    modifier: Modifier, text: String,
    arrowPosition: ArrowPosition,
    xPaddingOffset: Dp,
    yPaddingOffset: Dp
) {
    ConstraintLayout {
        val (card, arrow) = createRefs()
        Card(
            modifier = modifier
                .wrapContentWidth()
                .wrapContentHeight()
                .padding(top = yPaddingOffset)
                .constrainAs(card) {
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    top.linkTo(parent.top)
                },

            colors = CardDefaults.cardColors().copy(containerColor = Color(0xFF1F2937))
        ) {
            Text(
                modifier = modifier
                    .offset(y = -yPaddingOffset, x = -xPaddingOffset)
                    .width(180.dp)
                    .padding(8.dp),
                text = text,
            )
        }

        Canvas(
            modifier = Modifier
                .constrainAs(arrow) {
                    top.linkTo(card.bottom)
                    when (arrowPosition) {
                        ArrowPosition.Start -> start.linkTo(card.start)
                        ArrowPosition.Center -> {
                            start.linkTo(card.start)
                            end.linkTo(card.end)
                        }

                        ArrowPosition.End -> end.linkTo(card.end)
                    }
                }

                .offset(
                    x = when (arrowPosition) {
                        ArrowPosition.Start -> xPaddingOffset + 10f.dp
                        ArrowPosition.Center -> xPaddingOffset / 2
                        ArrowPosition.End -> 0.dp
                    }, y = yPaddingOffset
                )
        ) {
            drawBottomTriangle(arrowWidth = 80f, arrowHeight = 40f, position = arrowPosition)
        }

    }

}


@Composable
fun QuestionsCategoryGridItem(
    modifier: Modifier = Modifier,
    questionsCategory: QuestionsCategory,
) {
    ElevatedCard(modifier = modifier) {
        Column(
            Modifier
                .fillMaxSize()
                .padding(8.dp)
        ) {
            Text(
                text = "${questionsCategory.answeredQuestions} sur ${questionsCategory.questionsNum} Questions",
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.inversePrimary)
                    .padding(8.dp)
            )

            Spacer(Modifier.height(8.dp))
            Row(modifier = Modifier.fillMaxWidth()) {
                Icon(
                    imageVector = Icons.Default.DateRange,
                    contentDescription = questionsCategory.categoryName,
                    tint = MaterialTheme.colorScheme.primary
                )
                Spacer(Modifier.width(8.dp))
                Text(
                    text = questionsCategory.categoryName,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            Spacer(Modifier.height(8.dp))
            Text(text = "Progress ${(questionsCategory.answeredQuestions.toFloat() / questionsCategory.questionsNum * 100).toInt()}%")
            Spacer(Modifier.height(8.dp))
            LinearProgressIndicator(modifier = Modifier.fillMaxWidth(),
                color = MaterialTheme.colorScheme.primary,
                progress = { questionsCategory.answeredQuestions.toFloat() / questionsCategory.questionsNum })
        }
    }
}

@Composable
fun QuestionsCategoryGrid(
    modifier: Modifier,
    questionsCategories: List<QuestionsCategory>,
    onProceedClicked: () -> Unit

) {
    var rect by remember { mutableStateOf(Rect.Zero) }
    var showHints by remember { mutableStateOf(true) }

    Box(modifier = modifier) {
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier,
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(questionsCategories.size) {
                QuestionsCategoryGridItem(
                    questionsCategory = questionsCategories[it],
                    modifier = Modifier
                        .fillMaxSize()
                        .onGloballyPositioned {
                            rect = it.boundsInParent()
                        },
                )

            }
        }
        if (showHints) {
            Spotlight(
                targetRect = rect,
                targetRect2 = null,
                onProceed = onProceedClicked,
                onDismiss = { showHints = false }, showSpotlight = true
            )
            ToolTip2(
                modifier = Modifier.offset(y = 40.dp, x = 40.dp),
                text = "Grid Item",
                arrowPosition = ArrowPosition.Start,
                yPaddingOffset = 40.dp,
                xPaddingOffset = 40.dp
            )
        }
    }
}


@Composable
fun QuestionsScreen(
    modifier: Modifier,
    questionsCategories: List<QuestionsCategory>,
    questionOrals: List<QuestionOral>,
) {
    val pagerState = rememberPagerState(pageCount = { 2 })
    val scope = rememberCoroutineScope()
    Box(modifier = modifier) {
        Column(modifier = Modifier.fillMaxSize()) {
            TabRow(selectedTabIndex = pagerState.currentPage) {
                Tab(
                    selected = pagerState.currentPage == 0,
                    selectedContentColor = MaterialTheme.colorScheme.primary,
                    onClick = { scope.launch { pagerState.animateScrollToPage(0) } },
                    text = { Text("Writing") },
                    icon = {
                        Icon(
                            imageVector = Icons.Default.Create,
                            contentDescription = "Writing Questions"
                        )
                    }
                )
                Tab(
                    selected = pagerState.currentPage == 1,
                    selectedContentColor = MaterialTheme.colorScheme.primary,
                    onClick = { scope.launch { pagerState.animateScrollToPage(1) } },
                    text = { Text("Oral") },
                    icon = {
                        Icon(
                            imageVector = Icons.Default.Call,
                            contentDescription = "Oral Questions"
                        )
                    }
                )
            }

            HorizontalPager(modifier = Modifier.fillMaxSize(), state = pagerState) { page ->
                when (page) {
                    0 -> QuestionsCategoryGrid(
                        modifier = Modifier.fillMaxSize(),
                        questionsCategories = questionsCategories
                    ) {
                        scope.launch {
                            pagerState.animateScrollToPage(1)
                        }
                    }

                    1 -> QuestionsOralColumn(
                        modifier = Modifier.fillMaxSize(),
                        questionOrals = questionOrals
                    )

                }
            }
        }

    }
}

@Composable
fun QuestionCard(
    modifier: Modifier,
    questionOral: QuestionOral,
) {
    val questionBullets by remember {
        mutableStateOf(
            buildAnnotatedString {
                questionOral.questionBullets.forEach {
                    append("\u2022 $it\n")
                }
            }
        )
    }
    Card(modifier = modifier) {
        Column(modifier = Modifier.padding(8.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy((16.dp)),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = questionOral.category,
                    modifier = Modifier
                        .background(Color.LightGray)
                        .padding(2.dp)
                )
                Text(
                    text = "Task ${questionOral.taskNumber}",
                    modifier = Modifier
                        .background(Color.LightGray)
                        .padding(2.dp)
                )
                Spacer(modifier = Modifier.weight(1f))
                Icon(
                    modifier = Modifier.size(24.dp),
                    imageVector = Icons.Default.MoreVert,
                    contentDescription = "Options"
                )
            }

            Text(
                modifier = Modifier.padding(16.dp),
                text = questionBullets
            )

            Spacer(modifier = Modifier.weight(1f))
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.List,
                    contentDescription = "Number of answers"
                )
                Text(text = "${questionOral.answersNumber} answers")
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    modifier = Modifier
                        .alpha(0.8f), text = "Date: ${questionOral.date}"
                )
            }

        }
    }
}

@Composable
fun QuestionsOralColumn(
    modifier: Modifier = Modifier,
    questionOrals: List<QuestionOral>,
) {
    var showHints by remember { mutableStateOf(true) }
    var rect11 by remember { mutableStateOf(Rect.Zero) }

    Box(
        modifier = modifier,
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
                .scrollable(orientation = Orientation.Vertical,
                    state = rememberScrollState()),
        ) {
            Spacer(modifier = Modifier.height(100.dp))
            Button(
                modifier = Modifier
                    .onGloballyPositioned {
                        rect11 = it.boundsInParent()
                    },
                onClick = { },
                shape = RoundedCornerShape(8.dp)
            ) {
                Row {
                    Text(
                        text = "Filter",
                        color = MaterialTheme.colorScheme.inversePrimary,
                        modifier = Modifier.padding(end = 8.dp)
                    )
                    Icon(
                        imageVector = Icons.Default.Create,
                        contentDescription = "Filter",
                        modifier = Modifier.size(24.dp),
                        tint = MaterialTheme.colorScheme.inversePrimary
                    )
                }
            }

            questionOrals.forEach { questionOral ->
                QuestionCard(
                    modifier = Modifier.padding(8.dp),
                    questionOral
                )
            }
        }

        Spotlight(
            targetRect = rect11,
            targetRect2 = null,
            onProceed = { showHints = false },
            onDismiss = { showHints = false },
            showSpotlight = showHints
        )
        if(showHints) {
            ToolTip2(
                modifier = Modifier.offset(y = 25.dp, x = 0.dp),
                text = "Grid Item",
                arrowPosition = ArrowPosition.Start,
                yPaddingOffset = 25.dp,
                xPaddingOffset = 0.dp
            )
        }
    }
}

@Preview
@Composable
fun QuestionsScreenPreview() {
    QuestionsScreen(
        modifier = Modifier.fillMaxSize(),
        questionsCategories = listOf(
            QuestionsCategory(
                questionsNum = 10,
                answeredQuestions = 5,
                categoryName = "General Knowledge"
            ),
            QuestionsCategory(
                questionsNum = 8,
                answeredQuestions = 3,
                categoryName = "Science and Nature"
            ),
            QuestionsCategory(
                questionsNum = 15,
                answeredQuestions = 10,
                categoryName = "History"
            ),
        ),
        questionOrals = listOf(
            QuestionOral(
                category = "Communication Skills", taskNumber = 2, answersNumber = 5, date = "20 Feb 2024",
                questionBullets = listOf(
                    "Describe a challenging communication scenario you've handled.",
                    "How do you adapt your communication style for different audiences?"
                ),
            ),
            QuestionOral(
                category = "Problem Solving", taskNumber = 3, answersNumber = 6, date = "22 Feb 2024",
                questionBullets = listOf(
                    "Give an example of a complex problem you solved.",
                    "What steps did you take to analyze the problem?"
                ),
            ),
            QuestionOral(
                category = "Leadership", taskNumber = 1, answersNumber = 4, date = "25 Feb 2024",
                questionBullets = listOf(
                    "Describe a time you led a team to achieve a goal.",
                    "How did you handle conflicts within the team?"
                ),
            ),
        )
    )
}


data class QuestionOral(
    val category: String,
    val taskNumber: Int,
    val answersNumber: Int,
    val date: String,
    val questionBullets: List<String>,
)

