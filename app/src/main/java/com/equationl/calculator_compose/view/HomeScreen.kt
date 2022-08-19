package com.equationl.calculator_compose.view

import android.content.pm.ActivityInfo
import android.content.res.Configuration
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.FullscreenExit
import androidx.compose.material.icons.outlined.History
import androidx.compose.material.icons.outlined.ScreenRotation
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
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
    val configuration = LocalConfiguration.current
    val context = LocalContext.current

    Column(
        Modifier
            .fillMaxSize()
    ) {

        MenuTitle(
            configuration = configuration,
            onClickMenu = {
                homeViewModel.dispatch(
                    HomeAction.ClickMenu(
                        orientation =
                            if (configuration.orientation == Configuration.ORIENTATION_LANDSCAPE)
                                ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
                            else ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE,
                        context = context
                    )
                )
            },
            onClickHistory = {
                standardViewModel.dispatch(StandardAction.ToggleHistory())
            },
            onClickOverlay = {
                homeViewModel.dispatch(HomeAction.ClickOverlay(context))
            }
        )

        if (configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            ProgrammerScreen()
        }
        else {
            StandardScreen()
        }

    }
}

@Composable
private fun MenuTitle(
    configuration: Configuration,
    onClickMenu: () -> Unit,
    onClickHistory: () -> Unit,
    onClickOverlay: () -> Unit
) {
    Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.clickable { onClickMenu() }
        ) {
            Icon(imageVector = Icons.Outlined.ScreenRotation,
                contentDescription = "ScreenRotation",
                modifier = Modifier.padding(4.dp))
            Text(
                text = if (configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) "程序员" else "标准",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        }
        if (configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
            Row {
                Icon(imageVector = Icons.Outlined.History,
                    contentDescription = "history",
                    modifier = Modifier
                        .padding(4.dp)
                        .clickable { onClickHistory() }
                )
                Icon(imageVector = Icons.Outlined.FullscreenExit,
                    contentDescription = "overlay View",
                    modifier = Modifier
                        .padding(4.dp)
                        .clickable { onClickOverlay() }
                )
            }
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun PreviewMenuTitle() {
    val configuration = LocalConfiguration.current

    MenuTitle(configuration = configuration, onClickMenu = {  }, onClickHistory = {}, onClickOverlay = {})
}