package com.equationl.calculator_compose.dataModel

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp

val NumberColor = Color.White
val FunctionColor = Color.LightGray
val EqualColor = Color.Cyan

val StandardKeyBoardBtn = listOf(
    listOf(
        KeyBoardData("%", FunctionColor, KeyBoardBtnSize.Standard, 0),
        KeyBoardData("CE", FunctionColor, KeyBoardBtnSize.Standard, 1001),
        KeyBoardData("C", FunctionColor, KeyBoardBtnSize.Standard, 1002),
        KeyBoardData("←", FunctionColor, KeyBoardBtnSize.Standard, 1003),
    ),
    listOf(
        KeyBoardData("1/x", FunctionColor, KeyBoardBtnSize.Standard, 4),
        KeyBoardData("x²", FunctionColor, KeyBoardBtnSize.Standard, 5),
        KeyBoardData("√x", FunctionColor, KeyBoardBtnSize.Standard, 6),
        KeyBoardData(Operator.Divide.showText, FunctionColor, KeyBoardBtnSize.Standard, 13),
    ),
    listOf(
        KeyBoardData("7", NumberColor, KeyBoardBtnSize.Standard, 7),
        KeyBoardData("8", NumberColor, KeyBoardBtnSize.Standard, 8),
        KeyBoardData("9", NumberColor, KeyBoardBtnSize.Standard, 9),
        KeyBoardData(Operator.MULTIPLY.showText, FunctionColor, KeyBoardBtnSize.Standard, 12),
    ),
    listOf(
        KeyBoardData("4", NumberColor, KeyBoardBtnSize.Standard, 4),
        KeyBoardData("5", NumberColor, KeyBoardBtnSize.Standard, 5),
        KeyBoardData("6", NumberColor, KeyBoardBtnSize.Standard, 6),
        KeyBoardData(Operator.MINUS.showText, FunctionColor, KeyBoardBtnSize.Standard, 11),
    ),
    listOf(
        KeyBoardData("1", NumberColor, KeyBoardBtnSize.Standard, 1),
        KeyBoardData("2", NumberColor, KeyBoardBtnSize.Standard, 2),
        KeyBoardData("3", NumberColor, KeyBoardBtnSize.Standard, 3),
        KeyBoardData(Operator.ADD.showText, FunctionColor, KeyBoardBtnSize.Standard, 10),
    ),
    listOf(
        KeyBoardData("+/-", NumberColor, KeyBoardBtnSize.Standard, 14),
        KeyBoardData("0", NumberColor, KeyBoardBtnSize.Standard, 0),
        KeyBoardData(".", NumberColor, KeyBoardBtnSize.Standard, 15),
        KeyBoardData("=", EqualColor, KeyBoardBtnSize.Standard, 200),
    )
)

data class KeyBoardData(
    val text: String,
    val background: Color,
    val size: KeyBoardBtnSize,
    val clickInfo: Int
)

enum class KeyBoardBtnSize(val size: DpSize) {
    Standard(DpSize(40.dp, 25.dp)),
    Small(DpSize(25.dp, 8.dp))
}

enum class Operator(val showText: String) {
    ADD("+"),
    MINUS("-"),
    MULTIPLY("×"),
    Divide("÷"),
    NUll("")
}