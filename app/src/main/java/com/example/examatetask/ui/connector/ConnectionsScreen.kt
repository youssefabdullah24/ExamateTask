package com.example.examatetask.ui.connector

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.DateRange
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import com.example.examatetask.R

@Composable
fun ConnectionsRoute(
    modifier: Modifier = Modifier,
    partners: List<Partner>
) {
    ConnectionsScreen(
        modifier = modifier,
        partners = partners
    )
}

@Composable
fun ConnectionsScreen(
    modifier: Modifier,
    partners: List<Partner>
) {
    ConnectionsColumn(
        modifier = modifier,
        items = partners
    )
}

@Composable
fun ConnectionsColumn(
    modifier: Modifier,
    items: List<Partner>
) {
    LazyColumn(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        items(items.size) { index ->
            PartnerItem(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .padding(vertical = 8.dp, horizontal = 16.dp),
                partner = items[index]
            )
        }
    }
}

@Composable
fun PartnerItem(
    modifier: Modifier,
    partner: Partner
) {
    val colorPrimary = MaterialTheme.colorScheme.primary
    val colorSecondary = MaterialTheme.colorScheme.inversePrimary
    val initials: String = partner.name.split(" ").let {
        if (it.size > 1) it[0].first().uppercase() + it[1].first().uppercase()
        else it[0].first().uppercase().toString()
    }

    ElevatedCard(modifier = modifier) {
        ConstraintLayout(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp)
        ) {
            val (img, name, target, lastSeen, langs, bottomRow) = createRefs()

            Box(
                modifier = Modifier
                    .size(80.dp)
                    .clip(CircleShape)
                    .background(colorSecondary)
                    .constrainAs(img) {
                        start.linkTo(parent.start)
                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom)
                    }
            ) {
                Text(
                    text = initials,
                    modifier = Modifier.align(Alignment.Center),
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Bold,
                    fontSize = 24.sp,
                    color = colorPrimary
                )
            }

            Text(
                text = partner.name,
                modifier = Modifier
                    .padding(top = 8.dp, start = 8.dp)
                    .constrainAs(name) {
                        top.linkTo(img.top)
                        start.linkTo(img.end)
                    },
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.surfaceTint
            )
            Text(
                text = "Last seen online: ${partner.lastSeen}",
                modifier = Modifier
                    .padding(top = 4.dp, start = 8.dp)
                    .constrainAs(lastSeen) {
                        start.linkTo(name.start)
                        top.linkTo(name.bottom)
                    }
            )
            Box(
                modifier = Modifier
                    .height(28.dp)
                    .padding(top = 8.dp, start = 8.dp)
                    .constrainAs(target) {
                        top.linkTo(name.top)
                        start.linkTo(name.end)
                        bottom.linkTo(name.bottom)
                    }
                    .background(MaterialTheme.colorScheme.inversePrimary),
            ) {
                Text(
                    text = "Targeting: ${partner.targetLevel}",
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(2.dp)
                )
            }
            Row(
                Modifier
                    .padding(top = 8.dp, start = 8.dp)
                    .constrainAs(langs) {
                        start.linkTo(lastSeen.start)
                        top.linkTo(lastSeen.bottom)
                    },
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                partner.languages.forEach {
                    Text(
                        text = it,
                        modifier = Modifier
                            .background(colorSecondary)
                            .padding(2.dp)
                    )
                }
            }

            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .constrainAs(bottomRow) {
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        bottom.linkTo(parent.bottom)
                    },
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Outlined.LocationOn,
                    contentDescription = "Country"
                )
                Text(
                    text = partner.country,
                    fontSize = 8.sp
                )
                Spacer(modifier = Modifier.width(2.dp))

                Icon(
                    painter = painterResource(R.drawable.outline_female_24),
                    contentDescription = "Gender"
                )
                Text(
                    text = partner.gender,
                    fontSize = 8.sp
                )
                Spacer(modifier = Modifier.width(2.dp))

                Icon(
                    painter = painterResource(R.drawable.outline_cake_24),
                    contentDescription = "Age"
                )
                Text(
                    text = partner.age.toString(),
                    fontSize = 8.sp
                )
                Spacer(modifier = Modifier.width(2.dp))

                Icon(
                    imageVector = Icons.Outlined.DateRange,
                    contentDescription = "Exam Date"
                )
                Text(
                    text = partner.examDate,
                    fontSize = 8.sp
                )
            }
        }
    }
}

data class Partner(
    val name: String,
    val lastSeen: String,
    val languages: List<String>,
    val targetLevel: String,
    val country: String,
    val gender: String,
    val age: Int,
    val examDate: String
)
