package com.equationl.calculator_compose.view

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.equationl.calculator_compose.dataModel.overlayKeyBoardBtn
import com.equationl.calculator_compose.database.HistoryDb
import com.equationl.calculator_compose.ui.theme.CalculatorComposeTheme
import com.equationl.calculator_compose.ui.theme.OverlayLargeTextSize
import com.equationl.calculator_compose.ui.theme.OverlayNormalTextSize
import com.equationl.calculator_compose.utils.formatNumber
import com.equationl.calculator_compose.view.widgets.scrollToLeftAnimation
import com.equationl.calculator_compose.viewModel.OverLayViewModel
import com.equationl.calculator_compose.viewModel.OverlayAction
import com.equationl.calculator_compose.viewModel.StandardAction

@Composable
fun OverlayScreen(
    viewModel: OverLayViewModel
) {
    Column(Modifier.fillMaxSize()) {
        // 菜单
        TopMenu(viewModel)

        // 显示数据
        ShowScreen(viewModel)

        Divider(modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 0.dp))

        // 键盘
        StandardKeyBoard(viewModel)
    }
}

@Composable
private fun TopMenu(viewModel: OverLayViewModel) {
    val context = LocalContext.current

    Row(
        Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row {
            Icon(
                imageVector = Icons.Outlined.FormatSize,
                contentDescription = "adjust size",
                Modifier.clickable {
                    viewModel.dispatch(OverlayAction.ClickAdjustSize)
                }
            )

            Icon(
                imageVector = Icons.Outlined.InvertColors,
                contentDescription = "adjust transparent",
                Modifier.clickable {
                    viewModel.dispatch(OverlayAction.ClickAdjustAlpha)
                }
            )
        }

        Row {
            Icon(
                imageVector = Icons.Outlined.Fullscreen,
                contentDescription = "Back Full Screen",
                Modifier.clickable {
                    viewModel.dispatch(OverlayAction.ClickBackFullScreen(context))
                }
            )

            Icon(
                imageVector = Icons.Outlined.Close,
                contentDescription = "close",
                Modifier.clickable {
                    viewModel.dispatch(OverlayAction.ClickClose(context))
                }
            )
        }
    }
}

@Composable
private fun ShowScreen(viewModel: OverLayViewModel) {
    val viewState = viewModel.viewStates
    val inputScrollerState = rememberScrollState()
    val showTextScrollerState = rememberScrollState()

    Column(
        Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.End,
        verticalArrangement = Arrangement.Center
    ) {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween) {
            if (showTextScrollerState.value != showTextScrollerState.maxValue) {
                Icon(
                    imageVector = Icons.Outlined.ArrowLeft,
                    contentDescription = "scroll left",
                    modifier = Modifier.absoluteOffset(x = scrollToLeftAnimation(-10f).dp)
                )
            }
            Row(
                modifier = Modifier
                    .padding(vertical = 8.dp)
                    .padding(end = 8.dp)
                    .horizontalScroll(showTextScrollerState, reverseScrolling = true)
            ) {
                Text(
                    text = viewState.showText,
                    fontSize = OverlayNormalTextSize,
                    fontWeight = FontWeight.Light,
                    color = if (MaterialTheme.colors.isLight) Color.Unspecified else MaterialTheme.colors.primary
                )
            }
        }

        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween) {
            if (inputScrollerState.value != inputScrollerState.maxValue) {
                Icon(
                    imageVector = Icons.Outlined.ArrowLeft,
                    contentDescription = "scroll left",
                    modifier = Modifier.absoluteOffset(x = scrollToLeftAnimation(-10f).dp)
                )
            }

            Row(modifier = Modifier
                .padding(vertical = 8.dp)
                .padding(end = 8.dp)
                .horizontalScroll(inputScrollerState, reverseScrolling = true)
            ) {
                Text(
                    text = viewState.inputValue.formatNumber(formatDecimal = viewState.isFinalResult),
                    fontSize = OverlayLargeTextSize,
                    fontWeight = FontWeight.Bold,
                    color = if (MaterialTheme.colors.isLight) Color.Unspecified else MaterialTheme.colors.primary
                )
                LaunchedEffect(Unit) {
                    inputScrollerState.scrollTo(0)
                }
            }
        }
    }
}

@Composable
private fun StandardKeyBoard(viewModel: OverLayViewModel) {
    Column(modifier = Modifier.fillMaxSize()) {
        for (btnRow in overlayKeyBoardBtn()) {
            Row(modifier = Modifier
                .fillMaxWidth()
                .weight(1f)) {
                for (btn in btnRow) {
                    Row(modifier = Modifier.weight(1f)) {
                        KeyBoardButton(
                            text = btn.text,
                            onClick = { viewModel.dispatch(StandardAction.ClickBtn(btn.index)) },
                            backGround = btn.background,
                            paddingValues = PaddingValues(0.5.dp),
                            isFilled = btn.isFilled
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun KeyBoardButton(
    text: String,
    onClick: () -> Unit,
    backGround: Color = Color.White,
    isFilled: Boolean = false,
    paddingValues: PaddingValues = PaddingValues(0.dp)
) {
    Card(
        onClick = { onClick() },
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues),
        backgroundColor = if (isFilled) backGround else MaterialTheme.colors.surface,
        shape = MaterialTheme.shapes.large,
        elevation = 0.dp,
        border = BorderStroke(0.dp, Color.Transparent)
    ) {
        Row(Modifier.fillMaxSize(), horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically) {
            Text(text, fontSize = OverlayLargeTextSize, color = if (isFilled) Color.Unspecified else backGround)
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun PreviewOverlayScreen() {
    CalculatorComposeTheme(false) {
        Column(
            Modifier
                .fillMaxSize()
                .background(MaterialTheme.colors.background)) {
            OverlayScreen(OverLayViewModel(HistoryDb.create(LocalContext.current, false)))
        }
    }
}