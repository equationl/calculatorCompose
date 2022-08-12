package com.equationl.calculator_compose.view

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.History
import androidx.compose.material.icons.outlined.Menu
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.equationl.calculator_compose.viewModel.HomeAction
import com.equationl.calculator_compose.viewModel.HomeViewModel
import com.equationl.calculator_compose.viewModel.StandardAction
import com.equationl.calculator_compose.viewModel.StandardViewModel

@Composable
fun HomeScreen(
    homeViewModel: HomeViewModel = hiltViewModel(),
    standardViewModel: StandardViewModel = hiltViewModel()
) {
    Column(
        Modifier
            .fillMaxSize()
            .background(Color.Gray)) {

        Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(imageVector = Icons.Outlined.Menu,
                    contentDescription = "menu",
                    modifier = Modifier
                        .padding(4.dp)
                        .clickable {
                            homeViewModel.dispatch(HomeAction.ClickMenu)
                        }
                )
                Text(text = "标准", fontSize = 18.sp, fontWeight = FontWeight.Bold)
            }
            Icon(imageVector = Icons.Outlined.History,
                contentDescription = "history",
                modifier = Modifier
                    .padding(4.dp)
                    .clickable {
                        standardViewModel.dispatch(StandardAction.ToggleHistory())
                    }
            )
        }

        StandardScreen()
    }
}

@Preview(showSystemUi = true)
@Composable
fun PreviewHome() {
    HomeScreen(homeViewModel = HomeViewModel())
}