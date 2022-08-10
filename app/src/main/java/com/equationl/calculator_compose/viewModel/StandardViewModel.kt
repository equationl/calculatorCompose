package com.equationl.calculator_compose.viewModel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.equationl.calculator_compose.dataModel.Operator
import com.equationl.calculator_compose.utils.calculate

class StandardViewModel: ViewModel() {
    var viewStates by mutableStateOf(StandardState())
        private set

    fun dispatch(action: StandardAction) {
        when (action) {
            is StandardAction.ClickBtn -> clickBtn(action.no)
        }
    }

    // 标记第一个值输入后，是否开始输入第二个值
    private var isInputSecondValue: Boolean = false
    // 标记是否已计算
    private var isCalculated: Boolean = false
    // 标记是否处于错误状态
    private var isErr: Boolean = false

    private fun clickBtn(no: Int) {
        if (isErr) {
            viewStates = StandardState()
            isErr = false
        }

        if (no in 0..9) {
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

            viewStates = viewStates.copy(inputValue = newValue)
        }

        when (no) {
            10 -> { // "+"
                var newState = viewStates.copy()
                if (isCalculated) {
                    newState = newState.copy(showValueRight = "")
                    isCalculated = false
                    isInputSecondValue = false
                }
                viewStates = newState.copy(inputOperator = Operator.ADD, showValueLeft = viewStates.inputValue)
            }
            11 -> { // "-"
                var newState = viewStates.copy()
                if (isCalculated) {
                    newState = newState.copy(showValueRight = "")
                    isCalculated = false
                    isInputSecondValue = false
                }
                viewStates = newState.copy(inputOperator = Operator.MINUS, showValueLeft = viewStates.inputValue)

            }
            12 -> { // "×"
                var newState = viewStates.copy()
                if (isCalculated) {
                    newState = newState.copy(showValueRight = "")
                    isCalculated = false
                    isInputSecondValue = false
                }
                viewStates = newState.copy(inputOperator = Operator.MULTIPLY, showValueLeft = viewStates.inputValue)
            }
            13 -> { // "÷"
                var newState = viewStates.copy()
                if (isCalculated) {
                    newState = newState.copy(showValueRight = "")
                    isCalculated = false
                    isInputSecondValue = false
                }
                viewStates = newState.copy(inputOperator = Operator.Divide, showValueLeft = viewStates.inputValue)
            }
            14 -> { // "+/-"
                if (viewStates.inputValue != "0") {
                    val newValue: String =
                        if (viewStates.inputValue.substring(0, 1) == "-") viewStates.inputValue.substring(1, viewStates.inputValue.length)
                        else "-" + viewStates.inputValue
                    viewStates = viewStates.copy(inputValue = newValue)
                }
            }
            15 -> { // "."
                if (viewStates.inputValue.indexOf('.') == -1) {
                    viewStates = viewStates.copy(inputValue = viewStates.inputValue + ".")
                }
            }
            200 -> { // "="
                if (viewStates.inputOperator == Operator.NUll) {
                    viewStates = viewStates.copy(showValueLeft = viewStates.inputValue, showValueRight = "=")
                    isCalculated = true
                }
                else {
                    val result = calculate(viewStates.showValueLeft, viewStates.inputValue, viewStates.inputOperator)
                    if (result.isSuccess) {
                        viewStates = viewStates.copy(inputValue = result.getOrNull().toString(), showValueRight = viewStates.inputValue+"=")
                        isCalculated = true
                    }
                    else {
                        viewStates = viewStates.copy(inputValue = result.exceptionOrNull()?.message ?: "Err", showValueRight = "")
                        isCalculated = false
                        isErr = true
                    }
                }
            }
            1001 -> { // "CE"
                viewStates = viewStates.copy(inputValue = "0")
            }
            1002 -> {  // "C"
                isInputSecondValue = false
                viewStates = StandardState()
            }
            1003 -> { // "←"
                if (viewStates.inputValue != "0") {
                    var newValue = viewStates.inputValue.substring(0, viewStates.inputValue.length - 1)
                    if (newValue.isEmpty()) newValue = "0"
                    viewStates = viewStates.copy(inputValue = newValue)
                }
            }
        }
    }
}

data class StandardState(
    val inputValue: String = "0",
    val inputOperator: Operator = Operator.NUll,
    val showValueLeft: String = "",
    val showValueRight: String = "",
)

sealed class StandardAction {
    data class ClickBtn(val no: Int): StandardAction()
}