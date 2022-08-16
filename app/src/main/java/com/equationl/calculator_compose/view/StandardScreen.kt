package com.equationl.calculator_compose.view

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.equationl.calculator_compose.dataModel.StandardKeyBoardBtn
import com.equationl.calculator_compose.database.HistoryDb
import com.equationl.calculator_compose.ui.theme.InputLargeFontSize
import com.equationl.calculator_compose.ui.theme.ShowNormalFontSize
import com.equationl.calculator_compose.utils.formatNumber
import com.equationl.calculator_compose.utils.noRippleClickable
import com.equationl.calculator_compose.view.widgets.AutoSizeText
import com.equationl.calculator_compose.viewModel.StandardAction
import com.equationl.calculator_compose.viewModel.StandardViewModel

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun StandardScreen(
    viewModel: StandardViewModel = hiltViewModel()
) {
    val viewState = viewModel.viewStates

    Column(
        Modifier
            .fillMaxWidth()
            .height(150.dp)
            .noRippleClickable { viewModel.dispatch(StandardAction.ToggleHistory(true)) }
        ,
        horizontalAlignment = Alignment.End,
        verticalArrangement = Arrangement.Center
    ) {
        AnimatedContent(targetState = viewState.showText) { targetState: String ->
            Row(modifier = Modifier.padding(8.dp)) {
                AutoSizeText(
                    text = targetState,
                    fontSize = ShowNormalFontSize,
                    fontWeight = FontWeight.Light)
            }
        }
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

            Row(modifier = Modifier.padding(8.dp)) {
                AutoSizeText(
                    text = targetState.formatNumber(formatDecimal = viewState.isFinalResult),
                    fontSize = InputLargeFontSize,
                    fontWeight = FontWeight.Bold,
                )
            }
        }
    }

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

@Composable
fun StandardKeyBoard(viewModel: StandardViewModel) {
    Column(modifier = Modifier.fillMaxSize()) {
        for (btnRow in StandardKeyBoardBtn) {
            Row(modifier = Modifier
                .fillMaxWidth()
                .weight(1f)) {
                for (btn in btnRow) {
                    Row(modifier = Modifier.weight(1f)) {
                        KeyBoardButton(
                            text = btn.text,
                            onClick = { viewModel.dispatch(StandardAction.ClickBtn(btn.index)) },
                            backGround = btn.background,
                            paddingValues = PaddingValues(0.5.dp)
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
    paddingValues: PaddingValues = PaddingValues(0.dp)
) {
    Card(
        onClick = { onClick() },
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues),
        backgroundColor = backGround,
        shape = MaterialTheme.shapes.large
    ) {
        Row(Modifier.fillMaxSize(), horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically) {
            Text(text, fontSize = 24.sp)
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun PreviewStandardScreen() {
    Column(
        Modifier
            .fillMaxSize()
            .background(Color.Gray)) {
        StandardScreen(StandardViewModel(HistoryDb.create(LocalContext.current, false)))
    }
}