package com.equationl.calculator_compose.view

import androidx.compose.foundation.layout.*
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.equationl.calculator_compose.dataModel.StandardKeyBoardBtn
import com.equationl.calculator_compose.viewModel.StandardAction
import com.equationl.calculator_compose.viewModel.StandardViewModel

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
                            onClick = { viewModel.dispatch(StandardAction.ClickBtn(btn.clickInfo)) },
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
fun KeyBoardButton(
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
fun PreviewKeyBoard() {
    Column(modifier = Modifier.padding(16.dp)) {
        StandardKeyBoard(StandardViewModel())
    }
}