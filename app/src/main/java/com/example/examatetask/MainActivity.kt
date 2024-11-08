package com.example.examatetask

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberTooltipState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.graphics.ClipOp
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.clipPath
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.boundsInRoot
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.examatetask.ui.connector.ConnectionsRoute
import com.example.examatetask.ui.connector.Partner
import com.example.examatetask.ui.home.HomeRoute
import com.example.examatetask.ui.home.StudyUnit
import com.example.examatetask.ui.profile.ProfilesRoute
import com.example.examatetask.ui.questions.QuestionOral
import com.example.examatetask.ui.questions.QuestionsCategory
import com.example.examatetask.ui.questions.QuestionsRoute
import com.example.examatetask.ui.theme.ExamateTaskTheme
import com.example.examatetask.ui.tools.ToolsRoute
import com.example.examatetask.ui.welcome.WelcomeOverlay

@OptIn(ExperimentalMaterial3Api::class)
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            ExamateTaskTheme {
                val state = rememberTooltipState()
                var targetRect by remember { mutableStateOf<Rect?>(null) }
                var targetRect2 by remember { mutableStateOf<Rect?>(null) }
                var spotLightVisibility by remember { mutableStateOf(true) }
                var welcomeOverlayVisibility by remember { mutableStateOf(true) }
                val navController = rememberNavController()
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentRoute = navBackStackEntry?.destination?.route
                val selectedRoute = routes.find { it.name == currentRoute }

                val padding = when (selectedRoute) {
                    Route.HOME -> 4
                    Route.CONNECTOR -> 88
                    Route.QUESTIONS -> 120
                    Route.PROFILE -> 144
                    Route.TOOLS -> 230
                    null -> 4
                }.dp
                LaunchedEffect(Unit) { state.show() }
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Transparent)
                ) {
                    Scaffold(
                        modifier = Modifier.fillMaxSize(),
                        bottomBar = {
                            Column {
                                AnimatedVisibility(visible = spotLightVisibility) {
                                    ToolTip(
                                        modifier = Modifier
                                            .onGloballyPositioned {
                                                targetRect2 = it.boundsInRoot()
                                            },
                                        text = "This is the ${selectedRoute?.title} Screen",
                                        arrowPosition = when (selectedRoute) {
                                            Route.HOME -> ArrowPosition.Start
                                            Route.CONNECTOR -> ArrowPosition.Start
                                            Route.QUESTIONS -> ArrowPosition.Center
                                            Route.PROFILE -> ArrowPosition.End
                                            Route.TOOLS -> ArrowPosition.End
                                            else -> ArrowPosition.Start
                                        },
                                        paddingOffset = padding
                                    )
                                }

                                NavigationBar {
                                    routes.forEach { route ->
                                        NavigationItem(
                                            modifier = Modifier.onGloballyPositioned {
                                                if (navController.currentDestination?.route == route.name)
                                                    targetRect = it.boundsInRoot()
                                            },
                                            isSelected = navController.currentDestination?.route == route.name,
                                            route = route,
                                            onClick = {
                                                navController.navigate(route.name)
                                            }
                                        )
                                    }
                                }
                            }
                        }
                    ) { innerPadding ->
                        NavHost(
                            modifier = Modifier
                                .padding(innerPadding)
                                .fillMaxSize(),
                            navController = navController,
                            startDestination = Route.HOME.name
                        ) {
                            composable(route = Route.HOME.name) {
                                HomeRoute(
                                    modifier = Modifier.fillMaxSize()
                                    , items = listOf(
                                        StudyUnit(id = 1, title = "Introduction", isLocked = false),
                                        StudyUnit(id = 2, title = "Unit 1", isLocked = true),
                                        StudyUnit(id = 3, title = "Unit 2", isLocked = true),
                                        StudyUnit(id = 4, title = "Unit 3", isLocked = true),

                                        )
                                )
                            }
                            composable(route = Route.PROFILE.name) {
                                ProfilesRoute(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .padding(innerPadding)
                                )
                            }
                            composable(route = Route.QUESTIONS.name) {
                                QuestionsRoute(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .padding(innerPadding),
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
                                    oralCategories = listOf(
                                        QuestionOral(
                                            category = "Communication Skills", taskNumber = 2, answersNumber = 5, date = "20 Feb 2024",
                                            questionBullets = listOf(
                                                "Describe a challenging communication scenario you've handled.",
                                                "How do you adapt your communication style for different audiences?"
                                            ),
                                        ),
                                        QuestionOral(
                                            category = "Communication Skills", taskNumber = 2, answersNumber = 5, date = "20 Feb 2024",
                                            questionBullets = listOf(
                                                "Describe a challenging communication scenario you've handled.",
                                                "How do you adapt your communication style for different audiences?"
                                            ),
                                        ),
                                        QuestionOral(
                                            category = "Communication Skills", taskNumber = 2, answersNumber = 5, date = "20 Feb 2024",
                                            questionBullets = listOf(
                                                "Describe a challenging communication scenario you've handled.",
                                                "How do you adapt your communication style for different audiences?"
                                            ),
                                        ),
                                        QuestionOral(
                                            category = "Communication Skills", taskNumber = 2, answersNumber = 5, date = "20 Feb 2024",
                                            questionBullets = listOf(
                                                "Describe a challenging communication scenario you've handled.",
                                                "How do you adapt your communication style for different audiences?"
                                            ),
                                        ),
                                        QuestionOral(
                                            category = "Communication Skills", taskNumber = 2, answersNumber = 5, date = "20 Feb 2024",
                                            questionBullets = listOf(
                                                "Describe a challenging communication scenario you've handled.",
                                                "How do you adapt your communication style for different audiences?"
                                            ),
                                        ),
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
                            composable(route = Route.CONNECTOR.name) {
                                ConnectionsRoute(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .padding(innerPadding), partners = listOf(
                                        Partner(
                                            name = "Alice Johnson",
                                            lastSeen = "3 hours ago",
                                            languages = listOf("English", "Spanish"),
                                            targetLevel = "B2",
                                            country = "El Salvador",
                                            gender = "Female",
                                            age = 28,
                                            examDate = "16 Jun 2025"
                                        ),
                                        Partner(
                                            name = "Carlos Martinez",
                                            lastSeen = "2 days ago",
                                            languages = listOf("English", "French"),
                                            targetLevel = "C1",
                                            country = "Brazil",
                                            gender = "Male",
                                            age = 32,
                                            examDate = "10 Jul 2024"
                                        ),
                                        Partner(
                                            name = "Maria Thompson",
                                            lastSeen = "yesterday",
                                            languages = listOf("English", "Arabic", "German"),
                                            targetLevel = "B1",
                                            country = "Canada",
                                            gender = "Female",
                                            age = 27,
                                            examDate = "20 Oct 2024"
                                        )
                                    )
                                )
                            }
                            composable(route = Route.TOOLS.name) {
                                ToolsRoute(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .padding(innerPadding)
                                )
                            }
                        }
                    }

                    targetRect?.let {
                        Spotlight(
                            targetRect = it,
                            targetRect2 = targetRect2,
                            showSpotlight = spotLightVisibility,
                            onDismiss = {
                                spotLightVisibility = false
                            }, onProceed = {
                                val currentRoute = routes.indexOfFirst { it.name == currentRoute }
                                if (currentRoute != -1 && currentRoute < routes.size - 1) {
                                    navController.navigate(routes[currentRoute + 1].name)
                                } else {
                                    spotLightVisibility = false
                                }
                            }
                        )
                    }
                    WelcomeOverlay(welcomeOverlayVisibility){
                        welcomeOverlayVisibility = false
                    }

                }
            }
        }
    }
}

@Composable
fun Spotlight(
    targetRect: Rect,
    targetRect2: Rect? = null,
    onProceed: () -> Unit,
    onDismiss: () -> Unit,
    showSpotlight: Boolean
) {
    var showSpotlightState by remember { mutableStateOf(showSpotlight) }

    AnimatedVisibility(
        visible = showSpotlight,
        exit = fadeOut(tween(1000))
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Canvas(modifier = Modifier
                .fillMaxSize()
                .pointerInput(Unit) {
                    detectTapGestures(onTap = { _ ->
                        showSpotlightState = false
                        onDismiss()
                    })
                }) {

                val spotlightPath = Path().apply {
                    addRoundRect(RoundRect(rect = targetRect, cornerRadius = CornerRadius(16.dp.toPx())))
                    targetRect2?.let {
                        addRoundRect(RoundRect(rect = it, cornerRadius = CornerRadius(16.dp.toPx())))
                    }
                }

                clipPath(path = spotlightPath, clipOp = ClipOp.Difference) {
                    drawRect(Color.Black.copy(alpha = 0.8f))
                }
            }

            Text(
                text = "Tap anywhere to dismiss",
                color = Color.White,
                modifier = Modifier
                    .align(Alignment.Center)
                    .clickable {
                        showSpotlightState = false
                        onDismiss()
                    }
            )
            Text(
                text = "Tap here to proceed",
                color = Color.White,
                modifier = Modifier
                    .padding(bottom = 200.dp, end = 20.dp)
                    .align(Alignment.BottomEnd)
                    .clickable { onProceed() }
            )
        }
    }
}

@Composable
fun ToolTip(modifier: Modifier, text: String, arrowPosition: ArrowPosition, paddingOffset: Dp) {
    ConstraintLayout(modifier = Modifier.padding(vertical = 8.dp)) {
        val (card, arrow) = createRefs()
        Card(
            Modifier
                .wrapContentWidth()
                .wrapContentHeight()
                .padding(start = paddingOffset, bottom = 8.dp)
                .constrainAs(card) {
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    bottom.linkTo(parent.bottom)
                },
            colors = CardDefaults.cardColors().copy(containerColor = Color(0xFF1F2937))
        ) {
            Text(
                modifier = modifier
                    .width(180.dp)
                    .padding(8.dp),
                text = text
            )
        }

        Canvas(
            modifier = Modifier.padding(start = paddingOffset)

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
                        ArrowPosition.Start -> paddingOffset
                        ArrowPosition.Center -> paddingOffset / 2
                        ArrowPosition.End -> 0.dp
                    }
                )
        ) {
            drawBottomTriangle(arrowWidth = 80f, arrowHeight = 40f, position = arrowPosition)
        }
    }
}

@Composable
private fun RowScope.NavigationItem(
    modifier: Modifier,
    isSelected: Boolean,
    route: Route,
    onClick: () -> Unit
) {
    NavigationBarItem(
        modifier = modifier,
        label = {
            Text(text = route.name, maxLines = 1, fontSize = 8.sp)
        },
        icon = {
            Icon(
                modifier = Modifier.size(24.dp),
                imageVector = route.icon,
                contentDescription = null
            )
        },
        selected = isSelected,
        onClick = onClick
    )
}

enum class ArrowPosition {
    Start,
    Center,
    End
}

fun DrawScope.drawBottomTriangle(arrowWidth: Float, arrowHeight: Float, position: ArrowPosition) {
    val startX = when (position) {
        ArrowPosition.Start -> 0f
        ArrowPosition.Center -> (size.width / 2) - (arrowWidth / 2)
        ArrowPosition.End -> 0f
    }

    val path = Path().apply {
        moveTo(startX, 0f)
        lineTo(startX + (arrowWidth / 2), arrowHeight)
        lineTo(startX + (arrowWidth), 0f)
        close()
    }
    drawPath(path, color = Color(0xFF1F2937))
}

val routes = listOf(Route.HOME, Route.CONNECTOR, Route.QUESTIONS, Route.PROFILE, Route.TOOLS)

enum class Route(val title: String, val icon: ImageVector) {
    HOME("Home", Icons.Default.Home),
    CONNECTOR("connector", Icons.Default.Person),
    QUESTIONS("questions", Icons.Default.Check),
    PROFILE("profile", Icons.Default.KeyboardArrowUp),
    TOOLS("tools", Icons.Default.Call)
}