package com.equationl.calculator_compose.dataModel

import androidx.compose.ui.graphics.Color

// 数字按键
const val KeyIndex_0 = 0
const val KeyIndex_1 = 1
const val KeyIndex_2 = 2
const val KeyIndex_3 = 3
const val KeyIndex_4 = 4
const val KeyIndex_5 = 5
const val KeyIndex_6 = 6
const val KeyIndex_7 = 7
const val KeyIndex_8 = 8
const val KeyIndex_9 = 9
const val KeyIndex_A = 17  // 不按照顺序往下编号是因为在程序员键盘中使用的是 ascii 索引， 而数字 9 和 A 间隔了 7 位
const val KeyIndex_B = 18
const val KeyIndex_C = 19
const val KeyIndex_D = 20
const val KeyIndex_E = 21
const val KeyIndex_F = 22

// 运算按键
const val KeyIndex_Add = 100
const val KeyIndex_Minus = 101
const val KeyIndex_Multiply = 102
const val KeyIndex_Divide = 103
const val KeyIndex_NegativeNumber = 104
const val KeyIndex_Point = 105
const val KeyIndex_Reciprocal = 106
const val KeyIndex_Pow2 = 107
const val KeyIndex_Sqrt = 108
const val KeyIndex_Percentage = 109
const val KeyIndex_Lsh = 110
const val KeyIndex_Rsh = 111
const val KeyIndex_And = 112
const val KeyIndex_Or = 113
const val KeyIndex_Not = 114
const val KeyIndex_NAnd = 115
const val KeyIndex_NOr = 116
const val KeyIndex_XOr = 117

// 功能按键
const val KeyIndex_Equal = 1000
const val KeyIndex_CE = 1001
const val KeyIndex_Clear = 1002
const val KeyIndex_Back = 1003

// 预留按键
const val KeyIndex_Null = -1


val NumberColor = Color.White
val FunctionColor = Color.LightGray
val EqualColor = Color.Cyan
val UnavailableColor = Color.Transparent

val StandardKeyBoardBtn = listOf(
    listOf(
        KeyBoardData("%", FunctionColor,  KeyIndex_Percentage),
        KeyBoardData("CE", FunctionColor, KeyIndex_CE),
        KeyBoardData("C", FunctionColor,  KeyIndex_Clear),
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

val ProgrammerLeftKeyBoardBtn = listOf(
    listOf(
        KeyBoardData("D", NumberColor,  KeyIndex_D),
        KeyBoardData("E", NumberColor,  KeyIndex_E),
        KeyBoardData("F", NumberColor,  KeyIndex_F)
    ),
    listOf(
        KeyBoardData("A", NumberColor,  KeyIndex_A),
        KeyBoardData("B", NumberColor,  KeyIndex_B),
        KeyBoardData("C", NumberColor,  KeyIndex_C)
        ),
    listOf(
        KeyBoardData("7", NumberColor, KeyIndex_7),
        KeyBoardData("8", NumberColor,  KeyIndex_8),
        KeyBoardData("9", NumberColor,  KeyIndex_9)
    ),
    listOf(
        KeyBoardData("4", NumberColor, KeyIndex_4),
        KeyBoardData("5", NumberColor,  KeyIndex_5),
        KeyBoardData("6", NumberColor,  KeyIndex_6)
    ),
    listOf(
        KeyBoardData("1", NumberColor, KeyIndex_1),
        KeyBoardData("2", NumberColor,  KeyIndex_2),
        KeyBoardData("3", NumberColor,  KeyIndex_3)
    ),
    listOf(
        KeyBoardData("<<", FunctionColor, KeyIndex_Lsh),
        KeyBoardData("0", NumberColor,  KeyIndex_0),
        KeyBoardData(">>", FunctionColor,  KeyIndex_Rsh)
    )
)

val ProgrammerRightKeyBoardBtn = listOf(
    listOf(
        KeyBoardData("C", FunctionColor,  KeyIndex_Clear),
        KeyBoardData("←", FunctionColor,  KeyIndex_Back)
    ),
    listOf(
        KeyBoardData("CE", FunctionColor,  KeyIndex_CE),
        KeyBoardData(Operator.Divide.showText, FunctionColor,  KeyIndex_Divide)
    ),
    listOf(
        KeyBoardData("NOT", FunctionColor,  KeyIndex_Not),
        KeyBoardData(Operator.MULTIPLY.showText, FunctionColor,  KeyIndex_Multiply)
    ),
    listOf(
        KeyBoardData("XOR", FunctionColor,  KeyIndex_XOr),
        KeyBoardData(Operator.MINUS.showText, FunctionColor,  KeyIndex_Minus)
    ),
    listOf(
        KeyBoardData("AND", FunctionColor, KeyIndex_And),
        KeyBoardData(Operator.ADD.showText, FunctionColor,  KeyIndex_Add)
    ),
    listOf(
        KeyBoardData("OR", FunctionColor,  KeyIndex_Or),
        KeyBoardData("=", FunctionColor,  KeyIndex_Equal)
    )
)

val BitOperationList = listOf(
    Operator.NOT,
    Operator.AND,
    Operator.OR,
    Operator.XOR,
    Operator.LSH,
    Operator.RSH
)

data class KeyBoardData(
    val text: String,
    val background: Color,
    val index: Int,
    val isAvailable: Boolean = true
)

enum class Operator(val showText: String) {
    ADD("+"),
    MINUS("-"),
    MULTIPLY("×"),
    Divide("÷"),
    SQRT("√"),
    POW2("²"),
    NOT("NOT"),
    AND(" AND "),
    OR(" OR "),
    XOR(" XOR "),
    LSH(" Lsh "),
    RSH(" Rsh "),
    NUll("")
}

enum class InputBase(val number: Int, val forbidBtn: List<Int>) {
    HEX(16, listOf()),
    DEC(10, listOf(
        KeyIndex_A,
        KeyIndex_B,
        KeyIndex_C,
        KeyIndex_D,
        KeyIndex_E,
        KeyIndex_F
    )),
    OCT(8, listOf(
        KeyIndex_A,
        KeyIndex_B,
        KeyIndex_C,
        KeyIndex_D,
        KeyIndex_E,
        KeyIndex_F,
        KeyIndex_8,
        KeyIndex_9,
    )),
    BIN(2, listOf(
        KeyIndex_A,
        KeyIndex_B,
        KeyIndex_C,
        KeyIndex_D,
        KeyIndex_E,
        KeyIndex_F,
        KeyIndex_9,
        KeyIndex_8,
        KeyIndex_7,
        KeyIndex_6,
        KeyIndex_5,
        KeyIndex_4,
        KeyIndex_3,
        KeyIndex_2
    ))
}