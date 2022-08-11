package com.equationl.calculator_compose.viewModel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.equationl.calculator_compose.dataModel.*
import com.equationl.calculator_compose.utils.calculate
import com.equationl.calculator_compose.utils.formatNumber

class StandardViewModel: ViewModel() {
    var viewStates by mutableStateOf(StandardState())
        private set

    fun dispatch(action: StandardAction) {
        when (action) {
            is StandardAction.ClickBtn -> clickBtn(action.no)
        }
    }

    /**标记第一个值输入后，是否开始输入第二个值*/
    private var isInputSecondValue: Boolean = false
    /**标记是否已计算最终结果*/
    private var isCalculated: Boolean = false
    /**标记是否添加了非四则运算的“高级”运算符*/
    private var isAdvancedCalculated: Boolean = false
    /**标记是否处于错误状态*/
    private var isErr: Boolean = false

    private fun clickBtn(no: Int) {
        if (isErr) {
            viewStates = StandardState()
            isErr = false
            isAdvancedCalculated = false
            isCalculated = false
            isInputSecondValue = false
        }

        if (no in KeyIndex_0..KeyIndex_9) {
            val newValue =
                if (viewStates.inputValue == "0") no.toString()
                else if (viewStates.inputOperator != Operator.NUll && !isInputSecondValue) {
                    isInputSecondValue = true
                    no.toString()
                }
                else if (isCalculated) {
                    isCalculated = false
                    isInputSecondValue = false
                    viewStates = StandardState()
                    no.toString()
                }
                else viewStates.inputValue + no.toString()

            viewStates = viewStates.copy(inputValue = newValue, isFinalResult = false)
        }

        when (no) {
            KeyIndex_Add -> { // "+"
                clickArithmetic(Operator.ADD)
            }
            KeyIndex_Minus -> { // "-"
                clickArithmetic(Operator.MINUS)
            }
            KeyIndex_Multiply -> { // "×"
                clickArithmetic(Operator.MULTIPLY)
            }
            KeyIndex_Divide -> { // "÷"
                clickArithmetic(Operator.Divide)
            }
            KeyIndex_NegativeNumber -> { // "+/-"
                if (viewStates.inputValue != "0") {
                    val newValue: String =
                        if (viewStates.inputValue.substring(0, 1) == "-") viewStates.inputValue.substring(1, viewStates.inputValue.length)
                        else "-" + viewStates.inputValue
                    viewStates = viewStates.copy(inputValue = newValue, isFinalResult = false)
                }
            }
            KeyIndex_Point -> { // "."
                if (viewStates.inputValue.indexOf('.') == -1) {
                    viewStates = viewStates.copy(inputValue = viewStates.inputValue + ".")
                }
            }
            KeyIndex_Reciprocal -> { // "1/x"
                clickReciprocal()
            }
            KeyIndex_Pow2 -> { // "x²"
                clickPow2()
            }
            KeyIndex_Sqrt -> { // "√x"
                clickSqrt()
            }
            KeyIndex_Percentage -> { // "%"
                if (isInputSecondValue && viewStates.lastInputValue != "" && viewStates.inputOperator != Operator.NUll) {
                    var result: String = calculate(viewStates.inputValue, "100", Operator.Divide).getOrNull().toString()
                    result = calculate(viewStates.lastInputValue, result, Operator.MULTIPLY).getOrNull().toString()

                    viewStates = viewStates.copy(
                        inputValue = result,
                        showText = "${viewStates.lastInputValue}${viewStates.inputOperator.showText}" +
                                result.formatNumber(formatDecimal = true, formatInteger = false),
                        isFinalResult = true
                    )
                }
                else {
                    viewStates = viewStates.copy(
                        inputValue = "0",
                        showText = "0",
                        lastInputValue = "",
                        inputOperator = Operator.NUll
                    )
                }
            }
            KeyIndex_Equal -> { // "="
                clickEqual()
            }
            KeyIndex_CE -> { // "CE"
                viewStates = viewStates.copy(inputValue = "0")
            }
            KeyIndex_C -> {  // "C"
                isInputSecondValue = false
                viewStates = StandardState()
            }
            KeyIndex_Back -> { // "←"
                if (viewStates.inputValue != "0") {
                    var newValue = viewStates.inputValue.substring(0, viewStates.inputValue.length - 1)
                    if (newValue.isEmpty()) newValue = "0"
                    viewStates = viewStates.copy(inputValue = newValue)
                }
            }
        }
    }

    private fun clickReciprocal() {
        val result = calculate("1", viewStates.inputValue, Operator.Divide)
        val resultText = if (result.isSuccess) {
            result.getOrNull().toString()
        } else {
            isErr = true
            result.exceptionOrNull()?.message ?: "Err"
        }

        val newState = viewStates.copy(
            inputValue = resultText
        )

        if (isInputSecondValue) {
            viewStates = newState.copy(
                showText = "${viewStates.lastInputValue}${viewStates.inputOperator.showText}1/(${viewStates.inputValue})",
                isFinalResult = false
            )
        }
        else {
            viewStates = newState.copy(
                inputOperator = Operator.NUll,
                lastInputValue = viewStates.inputValue,
                showText = "1/(${viewStates.inputValue})",
                isFinalResult = false
            )
            isInputSecondValue = true
        }

        isAdvancedCalculated = true
    }

    private fun clickSqrt() {
        val result = calculate(viewStates.inputValue, "0", Operator.SQRT)

        val resultText = if (result.isSuccess) {
            result.getOrNull().toString()
        } else {
            isErr = true
            result.exceptionOrNull()?.message ?: "Err"
        }

        val newState = viewStates.copy(
            inputValue = resultText
        )

        if (isInputSecondValue) {
            viewStates = newState.copy(
                showText = "${viewStates.lastInputValue}${viewStates.inputOperator.showText}${Operator.SQRT.showText}(${viewStates.inputValue})",
                isFinalResult = false
            )
        }
        else {
            viewStates = newState.copy(
                inputOperator = Operator.NUll,
                lastInputValue = resultText,
                showText = "${Operator.SQRT.showText}(${viewStates.inputValue})",
                isFinalResult = false
            )
            isInputSecondValue = true
        }

        isAdvancedCalculated = true
    }

    private fun clickPow2() {
        val result = calculate(viewStates.inputValue, "0", Operator.POW2)

        val newState = viewStates.copy(
            inputValue = result.getOrNull().toString()
        )

        if (isInputSecondValue) {
            viewStates = newState.copy(
                showText = "${viewStates.lastInputValue}${viewStates.inputOperator.showText}(${viewStates.inputValue})${Operator.POW2.showText}",
                isFinalResult = false
            )
        }
        else {
            viewStates = newState.copy(
                inputOperator = Operator.NUll,
                lastInputValue = result.getOrNull().toString(),
                showText = "(${viewStates.inputValue})${Operator.POW2.showText}",
                isFinalResult = false
            )
            isInputSecondValue = true
        }

        isAdvancedCalculated = true
    }

    private fun clickEqual() {
        if (viewStates.inputOperator == Operator.NUll) {
            viewStates = if (isAdvancedCalculated) {
                viewStates.copy(
                    lastInputValue = viewStates.inputValue,
                    showText = "${viewStates.showText}=",
                    isFinalResult = true
                )
            } else {
                viewStates.copy(
                    lastInputValue = viewStates.inputValue,
                    showText = "${viewStates.inputValue}=",
                    isFinalResult = true
                )
            }

            isCalculated = true
        }
        else {
            val result = calculate(viewStates.lastInputValue, viewStates.inputValue, viewStates.inputOperator)
            if (result.isSuccess) {
                val inputValue = if (viewStates.inputValue.substring(0, 1) == "-") "(${viewStates.inputValue})" else viewStates.inputValue
                if (isAdvancedCalculated) {
                    val index = viewStates.showText.indexOf(viewStates.inputOperator.showText)
                    viewStates = if (index != -1 && index == viewStates.showText.lastIndex) {
                        viewStates.copy(
                            inputValue = result.getOrNull().toString(),
                            showText = "${viewStates.showText}$inputValue=",
                            isFinalResult = true
                        )
                    } else {
                        viewStates.copy(
                            inputValue = result.getOrNull().toString(),
                            showText = "${viewStates.showText}=",
                            isFinalResult = true
                        )
                    }
                }
                else {
                    viewStates = viewStates.copy(
                        inputValue = result.getOrNull().toString(),
                        showText = "${viewStates.lastInputValue}${viewStates.inputOperator.showText}$inputValue=",
                        isFinalResult = true
                    )
                }
                isCalculated = true
            }
            else {
                viewStates = viewStates.copy(
                    inputValue = result.exceptionOrNull()?.message ?: "Err",
                    showText = "",
                    isFinalResult = true
                )
                isCalculated = false
                isErr = true
            }
        }

        isAdvancedCalculated = false
    }

    private fun clickArithmetic(operator: Operator) {
        var newState = viewStates.copy(
            inputOperator = operator,
            lastInputValue = viewStates.inputValue,
            isFinalResult = false
        )
        if (isCalculated) {
            isCalculated = false
            isInputSecondValue = false
        }

        if (isAdvancedCalculated) {
            isInputSecondValue = false
            newState = newState.copy(
                showText = "${viewStates.showText}${operator.showText}"
            )
        }
        else {
            newState = newState.copy(
                showText = "${viewStates.inputValue}${operator.showText}"
            )
        }

        viewStates = newState
    }
}

data class StandardState(
    val inputValue: String = "0",
    val inputOperator: Operator = Operator.NUll,
    val lastInputValue: String = "",
    val showText: String = "",
    val isFinalResult: Boolean = false
)

sealed class StandardAction {
    data class ClickBtn(val no: Int): StandardAction()
}