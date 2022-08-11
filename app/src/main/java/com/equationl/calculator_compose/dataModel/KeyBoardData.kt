package com.equationl.calculator_compose.dataModel

import androidx.compose.ui.graphics.Color

const val KeyIndex_Percentage = 19
const val KeyIndex_CE = 1001
const val KeyIndex_C = 1002
const val KeyIndex_Back = 1003
const val KeyIndex_Reciprocal = 16
const val KeyIndex_Pow2 = 17
const val KeyIndex_Sqrt = 18
const val KeyIndex_Divide = 13
const val KeyIndex_7 = 7
const val KeyIndex_8 = 8
const val KeyIndex_9 = 9
const val KeyIndex_Multiply = 12
const val KeyIndex_4 = 4
const val KeyIndex_5 = 5
const val KeyIndex_6 = 6
const val KeyIndex_Minus = 11
const val KeyIndex_1 = 1
const val KeyIndex_2 = 2
const val KeyIndex_3 = 3
const val KeyIndex_Add = 10
const val KeyIndex_NegativeNumber = 14
const val KeyIndex_0 = 0
const val KeyIndex_Point = 15
const val KeyIndex_Equal = 200

val NumberColor = Color.White
val FunctionColor = Color.LightGray
val EqualColor = Color.Cyan

val StandardKeyBoardBtn = listOf(
    listOf(
        KeyBoardData("%", FunctionColor,  KeyIndex_Percentage),
        KeyBoardData("CE", FunctionColor, KeyIndex_CE),
        KeyBoardData("C", FunctionColor,  KeyIndex_C),
        KeyBoardData("←", FunctionColor,  KeyIndex_Back),
    ),
    listOf(
        KeyBoardData("1/x", FunctionColor, KeyIndex_Reciprocal),
        KeyBoardData("x²", FunctionColor, KeyIndex_Pow2),
        KeyBoardData("√x", FunctionColor, KeyIndex_Sqrt),
        KeyBoardData(Operator.Divide.showText, FunctionColor, KeyIndex_Divide),
    ),
    listOf(
        KeyBoardData("7", NumberColor, KeyIndex_7),
        KeyBoardData("8", NumberColor, KeyIndex_8),
        KeyBoardData("9", NumberColor, KeyIndex_9),
        KeyBoardData(Operator.MULTIPLY.showText, FunctionColor, KeyIndex_Multiply),
    ),
    listOf(
        KeyBoardData("4", NumberColor, KeyIndex_4),
        KeyBoardData("5", NumberColor, KeyIndex_5),
        KeyBoardData("6", NumberColor, KeyIndex_6),
        KeyBoardData(Operator.MINUS.showText, FunctionColor, KeyIndex_Minus),
    ),
    listOf(
        KeyBoardData("1", NumberColor, KeyIndex_1),
        KeyBoardData("2", NumberColor, KeyIndex_2),
        KeyBoardData("3", NumberColor, KeyIndex_3),
        KeyBoardData(Operator.ADD.showText, FunctionColor, KeyIndex_Add),
    ),
    listOf(
        KeyBoardData("+/-", NumberColor, KeyIndex_NegativeNumber),
        KeyBoardData("0", NumberColor, KeyIndex_0),
        KeyBoardData(".", NumberColor, KeyIndex_Point),
        KeyBoardData("=", EqualColor, KeyIndex_Equal),
    )
)

data class KeyBoardData(
    val text: String,
    val background: Color,
    val clickInfo: Int
)

enum class Operator(val showText: String) {
    ADD("+"),
    MINUS("-"),
    MULTIPLY("×"),
    Divide("÷"),
    SQRT("√"),
    POW2("²"),
    NUll("")
}