package com.equationl.calculator_compose.view

import androidx.compose.animation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.equationl.calculator_compose.database.HistoryDb
import com.equationl.calculator_compose.utils.formatNumber
import com.equationl.calculator_compose.utils.noRippleClickable
import com.equationl.calculator_compose.viewModel.StandardAction
import com.equationl.calculator_compose.viewModel.StandardViewModel

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
        Text(text = viewState.showText, modifier = Modifier.padding(8.dp), fontSize = 22.sp, fontWeight = FontWeight.Light)
        Text(text = viewState.inputValue.formatNumber(viewState.isFinalResult), modifier = Modifier.padding(8.dp), fontSize = 32.sp, fontWeight = FontWeight.Bold)
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

@Preview(showSystemUi = true)
@Composable
fun PreviewStandardScreen() {
    StandardScreen(StandardViewModel(HistoryDb.create(LocalContext.current, false)))
}