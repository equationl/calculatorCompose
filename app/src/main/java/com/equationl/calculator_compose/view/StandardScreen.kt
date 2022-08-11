package com.equationl.calculator_compose.view

import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.equationl.calculator_compose.utils.formatNumber
import com.equationl.calculator_compose.viewModel.StandardViewModel

@Composable
fun StandardScreen(viewModel: StandardViewModel) {
    val viewState = viewModel.viewStates
    Column(
        Modifier
            .fillMaxWidth()
            .height(150.dp), horizontalAlignment = Alignment.End, verticalArrangement = Arrangement.Center) {
        Text(text = viewState.showText, modifier = Modifier.padding(8.dp), fontSize = 22.sp, fontWeight = FontWeight.Light)
        Text(text = viewState.inputValue.formatNumber(viewState.isFinalResult), modifier = Modifier.padding(8.dp), fontSize = 32.sp, fontWeight = FontWeight.Bold)
    }

    StandardKeyBoard(viewModel)
}

@Preview(showSystemUi = true)
@Composable
fun PreviewStandardScreen() {
    StandardScreen(StandardViewModel())
}