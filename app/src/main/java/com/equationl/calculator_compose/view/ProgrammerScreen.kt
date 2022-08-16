package com.equationl.calculator_compose.view

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.equationl.calculator_compose.dataModel.InputBase
import com.equationl.calculator_compose.dataModel.ProgrammerKeyBoardBtn
import com.equationl.calculator_compose.ui.theme.InputLargeFontSize
import com.equationl.calculator_compose.ui.theme.InputNormalFontSize
import com.equationl.calculator_compose.ui.theme.InputTitleContentSize
import com.equationl.calculator_compose.ui.theme.ShowNormalFontSize
import com.equationl.calculator_compose.utils.formatNumber
import com.equationl.calculator_compose.view.widgets.AutoSizeText
import com.equationl.calculator_compose.viewModel.ProgrammerAction
import com.equationl.calculator_compose.viewModel.ProgrammerViewModel

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun ProgrammerScreen(
    viewModel: ProgrammerViewModel = hiltViewModel()
) {
    val viewState = viewModel.viewStates
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp

    Row(
        Modifier
            .fillMaxWidth()
            .height(180.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            Modifier.fillMaxHeight(),
            verticalArrangement = Arrangement.Center
        ) {
            Row(verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .padding(2.dp)
                    .clickable { viewModel.dispatch(ProgrammerAction.ChangeInputBase(InputBase.HEX)) }
            ) {
                Text(
                    text = "HEX",
                    fontSize =
                        if (viewState.inputBase == InputBase.HEX) InputTitleContentSize
                        else InputNormalFontSize,
                    fontWeight = if (viewState.inputBase == InputBase.HEX) FontWeight.Bold else null
                )

                Text(
                    text = viewState.inputHexText.formatNumber(addSplitChar = " ", splitLength = 4),
                    fontSize = InputNormalFontSize,
                    modifier = Modifier.padding(start = 8.dp)
                )
            }
            Row(verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .padding(2.dp)
                    .clickable { viewModel.dispatch(ProgrammerAction.ChangeInputBase(InputBase.DEC)) }
            ) {
                Text(
                    text = "DEC",
                    fontSize =
                        if (viewState.inputBase == InputBase.DEC) InputTitleContentSize
                        else InputNormalFontSize,
                    fontWeight = if (viewState.inputBase == InputBase.DEC) FontWeight.Bold else null
                )

                Text(
                    text = viewState.inputDecText.formatNumber(),
                    fontSize = InputNormalFontSize,
                    modifier = Modifier.padding(start = 8.dp)
                )
            }
            Row(verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .padding(2.dp)
                    .clickable { viewModel.dispatch(ProgrammerAction.ChangeInputBase(InputBase.OCT)) }
            ) {
                Text(
                    text = "OCT",
                    fontSize =
                        if (viewState.inputBase == InputBase.OCT) InputTitleContentSize
                        else InputNormalFontSize,
                    fontWeight = if (viewState.inputBase == InputBase.OCT) FontWeight.Bold else null
                )

                Text(
                    text = viewState.inputOctText.formatNumber(addSplitChar = " "),
                    fontSize = InputNormalFontSize,
                    modifier = Modifier.padding(start = 8.dp)
                )
            }
            Row(verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .padding(2.dp)
                    .clickable { viewModel.dispatch(ProgrammerAction.ChangeInputBase(InputBase.BIN)) }
            ) {
                Text(
                    text = "BIN",
                    fontSize =
                        if (viewState.inputBase == InputBase.BIN) InputTitleContentSize
                        else InputNormalFontSize,
                    fontWeight = if (viewState.inputBase == InputBase.BIN) FontWeight.Bold else null
                )

                Text(
                    text = viewState.inputBinText.formatNumber(addSplitChar = " ", splitLength = 4, isAddLeadingZero = viewState.inputBinText != "0"),
                    fontSize = InputNormalFontSize,
                    modifier = Modifier.widthIn(0.dp, screenWidth/2).padding(start = 8.dp),
                )
            }
        }
        
        Column(
            Modifier
                .fillMaxHeight(),
            verticalArrangement = Arrangement.Center
        ) {
            AnimatedContent(targetState = viewState.showText) { targetState: String ->
                Text(text = targetState, modifier = Modifier.padding(8.dp), fontSize = ShowNormalFontSize, fontWeight = FontWeight.Light)
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
                        text = targetState.formatNumber(
                            formatDecimal = false, // 程序员计算没有小数
                            addSplitChar = if (viewState.inputBase == InputBase.DEC) "," else " ",
                            splitLength = if (viewState.inputBase == InputBase.HEX || viewState.inputBase == InputBase.BIN) 4 else 3,
                            isAddLeadingZero = false, // 即使是二进制，在输入时也不应该有前导0
                            formatInteger = true
                        )
                        ,
                        fontSize = InputLargeFontSize,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }

    Column(Modifier.fillMaxSize()) {
        ProgrammerKeyBoard(viewModel)
    }
}

@Composable
fun ProgrammerKeyBoard(viewModel: ProgrammerViewModel) {
    val viewState = viewModel.viewStates

    Column(modifier = Modifier.fillMaxSize()) {
        for (btnRow in ProgrammerKeyBoardBtn) {
            Row(modifier = Modifier
                .fillMaxWidth()
                .weight(1f)) {
                for (btn in btnRow) {
                    val isAvailable = if (btn.isAvailable) {
                        btn.index !in viewState.inputBase.forbidBtn
                    }
                    else {
                        false
                    }

                    Row(modifier = Modifier.weight(1f)) {
                        KeyBoardButton(
                            text = btn.text,
                            onClick = { viewModel.dispatch(ProgrammerAction.ClickBtn(btn.index)) },
                            isAvailable = isAvailable,
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
    isAvailable: Boolean = true,
    backGround: Color = Color.White,
    paddingValues: PaddingValues = PaddingValues(0.dp)
) {
    Card(
        onClick = { onClick() },
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues),
        backgroundColor = backGround,
        shape = MaterialTheme.shapes.large,
        enabled = isAvailable
    ) {
        Row(Modifier.fillMaxSize(), horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically) {
            Text(text, fontSize = 24.sp, color = if (isAvailable) Color.Unspecified else Color.LightGray)
        }
    }
}

@Preview(showSystemUi = true, device = Devices.AUTOMOTIVE_1024p, widthDp = 1024, heightDp = 720)
@Composable
fun PreviewProgrammerScreen() {
    Column(
        Modifier
            .fillMaxSize()
            .background(Color.Gray)) {
        ProgrammerScreen(ProgrammerViewModel())
    }
}