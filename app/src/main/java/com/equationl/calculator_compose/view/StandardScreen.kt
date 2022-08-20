package com.equationl.calculator_compose.view

import androidx.compose.animation.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowLeft
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.equationl.calculator_compose.dataModel.standardKeyBoardBtn
import com.equationl.calculator_compose.database.HistoryDb
import com.equationl.calculator_compose.ui.theme.CalculatorComposeTheme
import com.equationl.calculator_compose.ui.theme.InputLargeFontSize
import com.equationl.calculator_compose.ui.theme.ShowNormalFontSize
import com.equationl.calculator_compose.ui.theme.ShowSmallFontSize
import com.equationl.calculator_compose.utils.formatNumber
import com.equationl.calculator_compose.utils.noRippleClickable
import com.equationl.calculator_compose.view.widgets.AutoSizeText
import com.equationl.calculator_compose.view.widgets.scrollToLeftAnimation
import com.equationl.calculator_compose.viewModel.StandardAction
import com.equationl.calculator_compose.viewModel.StandardViewModel

@Composable
fun StandardScreen(
    viewModel: StandardViewModel = hiltViewModel()
) {
    val viewState = viewModel.viewStates

    // 显示数据
    ShowScreen(viewModel)

    Divider(modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 16.dp, vertical = 0.dp))

    // 键盘与历史记录
    Box(Modifier.fillMaxSize()) {
        val isShowKeyBoard = viewState.historyList.isEmpty()

        StandardKeyBoard(viewModel)

        AnimatedVisibility(
            visible = !isShowKeyBoard,
            enter = slideInVertically(initialOffsetY = { it }) + fadeIn(),
            exit = slideOutVertically(targetOffsetY = { it }) + fadeOut()
        ) {
            HistoryWidget(
                historyList = viewState.historyList,
                onClick = { viewModel.dispatch(StandardAction.ReadFromHistory(it)) },
                onDelete = { viewModel.dispatch(StandardAction.DeleteHistory(it)) })
        }
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
private fun ShowScreen(viewModel: StandardViewModel) {
    val viewState = viewModel.viewStates
    val inputScrollerState = rememberScrollState()
    val showTextScrollerState = rememberScrollState()

    Column(
        Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.4f)
            .noRippleClickable { viewModel.dispatch(StandardAction.ToggleHistory(true)) }
        ,
        horizontalAlignment = Alignment.End,
        verticalArrangement = Arrangement.SpaceAround
    ) {
        // 上一个计算结果
        AnimatedContent(targetState = viewState.lastShowText) { targetState: String ->
            SelectionContainer {
                AutoSizeText(
                    text = targetState,
                    fontSize = ShowSmallFontSize,
                    fontWeight = FontWeight.Light,
                    color = if (MaterialTheme.colors.isLight) Color.Unspecified else MaterialTheme.colors.primary,
                    modifier = Modifier
                        .padding(horizontal = 12.dp)
                        .padding(bottom = 16.dp)
                        .alpha(0.5f),
                    minSize = 10.sp
                )
            }
        }

        Column(horizontalAlignment = Alignment.End) {
            // 计算公式
            AnimatedContent(targetState = viewState.showText) { targetState: String ->
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
                        SelectionContainer {
                            Text(
                                text = if (targetState.length > 5000) "数字过长" else targetState,
                                fontSize = ShowNormalFontSize,
                                fontWeight = FontWeight.Light,
                                color = if (MaterialTheme.colors.isLight) Color.Unspecified else MaterialTheme.colors.primary
                            )
                        }
                    }
                }
            }

            // 输入值或计算结果
            AnimatedContent(
                targetState = viewState.inputValue,
                transitionSpec = {
                    if (targetState.length > initialState.length) {
                        slideInVertically { height -> height } + fadeIn() with
                                slideOutVertically { height -> -height } + fadeOut()
                    } else {
                        slideInVertically { height -> -height } + fadeIn() with
                                slideOutVertically { height -> height } + fadeOut()
                    }.using(
                        SizeTransform(clip = false)
                    )
                }
            ) { targetState: String ->
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
                        SelectionContainer {
                            Text(
                                text = targetState.formatNumber(formatDecimal = viewState.isFinalResult),
                                fontSize = InputLargeFontSize,
                                fontWeight = FontWeight.Bold,
                                color = if (MaterialTheme.colors.isLight) Color.Unspecified else MaterialTheme.colors.primary
                            )
                        }
                        LaunchedEffect(Unit) {
                            inputScrollerState.scrollTo(0)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun StandardKeyBoard(viewModel: StandardViewModel) {
    Column(modifier = Modifier.fillMaxSize()) {
        for (btnRow in standardKeyBoardBtn()) {
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
            Text(text, fontSize = 32.sp, color = if (isFilled) Color.Unspecified else backGround)
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun PreviewStandardScreen() {
    CalculatorComposeTheme(false) {
        Column(
            Modifier
                .fillMaxSize()
                .background(MaterialTheme.colors.background)) {
            StandardScreen(StandardViewModel(HistoryDb.create(LocalContext.current, false)))
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun PreviewStandardScreenDark() {
    CalculatorComposeTheme(true) {
        Column(
            Modifier
                .fillMaxSize()
                .background(MaterialTheme.colors.background)) {
            StandardScreen(StandardViewModel(HistoryDb.create(LocalContext.current, false)))
        }
    }
}