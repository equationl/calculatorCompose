package com.equationl.calculator_compose.viewModel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.equationl.calculator_compose.dataModel.*
import com.equationl.calculator_compose.database.HistoryDb
import com.equationl.calculator_compose.utils.VibratorHelper
import com.equationl.calculator_compose.utils.calculate
import com.equationl.calculator_compose.utils.formatNumber
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
open class StandardViewModel @Inject constructor(
    private val dataBase: HistoryDb
): ViewModel() {

    var viewStates by mutableStateOf(StandardState())
        private set

    open fun dispatch(action: StandardAction) {
        when (action) {
            is StandardAction.ClickBtn -> clickBtn(action.no)
            is StandardAction.ToggleHistory -> toggleHistory(action.forceClose)
            is StandardAction.ReadFromHistory -> readFromHistory(action.item)
            is StandardAction.DeleteHistory -> deleteHistory(action.item)
            else -> {  }
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

    private fun toggleHistory(forceClose: Boolean) {
        VibratorHelper.instance.vibrateOnClick()
        if (viewStates.historyList.isNotEmpty() || forceClose) {
            viewStates = viewStates.copy(historyList = listOf())
        }
        else {
            viewStates = viewStates.copy(historyList = listOf(
                HistoryData(-1, showText = "加载中……", "null", "null", Operator.NUll, "请稍候")
            ))

            viewModelScope.launch {
                withContext(Dispatchers.IO) {
                    var list = dataBase.history().getAll()
                    if (list.isEmpty()) {
                        list = listOf(
                            HistoryData(-1, showText = "", "null", "null", Operator.NUll, "没有历史记录")
                        )
                    }
                    viewStates = viewStates.copy(historyList = list)
                }
            }
        }
    }

    private fun readFromHistory(item: HistoryData) {
        if (item.id != -1) {
            VibratorHelper.instance.vibrateOnEqual()
            viewStates = StandardState(
                inputValue = item.result,
                lastInputValue = item.lastInputText,
                inputOperator = item.operator,
                showText = item.showText,
                isFinalResult = true
            )
        }
    }

    private fun deleteHistory(item: HistoryData?) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                VibratorHelper.instance.vibrateOnError()
                viewStates = if (item == null) {
                    dataBase.history().deleteAll()
                    viewStates.copy(historyList = listOf())
                } else {
                    VibratorHelper.instance.vibrateOnClick()
                    dataBase.history().delete(item)
                    val newList = mutableListOf<HistoryData>()
                    newList.addAll(viewStates.historyList)
                    newList.remove(item)

                    viewStates.copy(historyList = newList)
                }

            }
        }
    }

    private fun clickBtn(no: Int) {
        if (isErr) {
            viewStates = StandardState()
            isErr = false
            isAdvancedCalculated = false
            isCalculated = false
            isInputSecondValue = false
        }

        if (no in KeyIndex_0..KeyIndex_9) {
            VibratorHelper.instance.vibrateOnClick()
            val newValue =
                if (viewStates.inputValue == "0") {
                    if (viewStates.inputOperator != Operator.NUll) isInputSecondValue = true
                    if (isAdvancedCalculated && viewStates.inputOperator == Operator.NUll) {  // 如果在输入高级运算符后直接输入数字，则重置状态
                        isAdvancedCalculated = false
                        isCalculated = false
                        isInputSecondValue = false
                        viewStates = StandardState()
                        no.toString()
                    }
                    no.toString()
                }
                else if (viewStates.inputOperator != Operator.NUll && !isInputSecondValue) {
                    isCalculated = false
                    isInputSecondValue = true
                    no.toString()
                }
                else if (isCalculated) {
                    isCalculated = false
                    isInputSecondValue = false
                    viewStates = StandardState(
                        lastShowText =
                            if (!isAdvancedCalculated)
                                viewStates.showText+viewStates.inputValue
                            else viewStates.lastShowText
                    )
                    no.toString()
                }
                else if (isAdvancedCalculated && viewStates.inputOperator == Operator.NUll) { // 如果在输入高级运算符后直接输入数字，则重置状态
                    isAdvancedCalculated = false
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
                VibratorHelper.instance.vibrateOnClick()
                if (viewStates.inputValue != "0") {
                    val newValue: String =
                        if (viewStates.inputValue.substring(0, 1) == "-") viewStates.inputValue.substring(1, viewStates.inputValue.length)
                        else "-" + viewStates.inputValue
                    viewStates = viewStates.copy(inputValue = newValue, isFinalResult = false)
                }
            }
            KeyIndex_Point -> { // "."
                VibratorHelper.instance.vibrateOnClick()
                if (viewStates.inputValue.indexOf('.') == -1) {
                    viewStates = viewStates.copy(inputValue = viewStates.inputValue + ".")
                }
            }
            KeyIndex_Reciprocal -> { // "1/x"
                VibratorHelper.instance.vibrateOnClick()
                clickReciprocal()
            }
            KeyIndex_Pow2 -> { // "x²"
                VibratorHelper.instance.vibrateOnClick()
                clickPow2()
            }
            KeyIndex_Sqrt -> { // "√x"
                VibratorHelper.instance.vibrateOnClick()
                clickSqrt()
            }
            KeyIndex_Percentage -> { // "%"
                if (isInputSecondValue && viewStates.lastInputValue != "" && viewStates.inputOperator != Operator.NUll) {
                    VibratorHelper.instance.vibrateOnClick()
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
                    VibratorHelper.instance.vibrateOnClear()
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
                VibratorHelper.instance.vibrateOnClear()
                if (isCalculated) {
                    clickClear()
                }
                else {
                    viewStates = viewStates.copy(inputValue = "0")
                }
            }
            KeyIndex_Clear -> {  // "C"
                VibratorHelper.instance.vibrateOnClear()
                clickClear()
            }
            KeyIndex_Back -> { // "←"
                VibratorHelper.instance.vibrateOnClick()
                if (viewStates.inputValue != "0") {
                    var newValue = viewStates.inputValue.substring(0, viewStates.inputValue.length - 1)
                    if (newValue.isEmpty()) newValue = "0"
                    viewStates = viewStates.copy(inputValue = newValue)
                }
            }
        }
    }

    private fun clickClear() {
        isInputSecondValue = false
        isCalculated = false
        isAdvancedCalculated = false
        isErr = false
        viewStates = StandardState()
    }

    private fun clickReciprocal() {
        val result = calculate("1", viewStates.inputValue, Operator.Divide)
        val resultText = if (result.isSuccess) {
            result.getOrNull()?.toPlainString() ?: "Null"
        } else {
            VibratorHelper.instance.vibrateOnError()
            isErr = true
            result.exceptionOrNull()?.message ?: "Err"
        }

        val newState = viewStates.copy(
            inputValue = resultText
        )

        if (isInputSecondValue) {
            viewStates = newState.copy(
                showText = "${viewStates.lastInputValue}${viewStates.inputOperator.showText}1/(${viewStates.inputValue})",
                isFinalResult = false,
                lastShowText =
                if (viewStates.showText.indexOf("=") != -1)
                    viewStates.showText+viewStates.inputValue
                else viewStates.lastShowText
            )
        }
        else {
            viewStates = newState.copy(
                inputOperator = Operator.NUll,
                lastInputValue = viewStates.inputValue,
                showText = "1/(${viewStates.inputValue})",
                isFinalResult = false
            )
           // isInputSecondValue = true
        }

        isAdvancedCalculated = true
    }

    private fun clickSqrt() {
        val result = calculate(viewStates.inputValue, "0", Operator.SQRT)

        val resultText = if (result.isSuccess) {
            result.getOrNull()?.toPlainString() ?: "Null"
        } else {
            VibratorHelper.instance.vibrateOnError()
            isErr = true
            result.exceptionOrNull()?.message ?: "Err"
        }

        val newState = viewStates.copy(
            inputValue = resultText
        )

        if (isInputSecondValue) {
            viewStates = newState.copy(
                showText = "${viewStates.lastInputValue}${viewStates.inputOperator.showText}${Operator.SQRT.showText}(${viewStates.inputValue})",
                isFinalResult = false,
                lastShowText =
                if (viewStates.showText.indexOf("=") != -1)
                    viewStates.showText+viewStates.inputValue
                else viewStates.lastShowText
            )
        }
        else {
            viewStates = newState.copy(
                inputOperator = Operator.NUll,
                lastInputValue = resultText,
                showText = "${Operator.SQRT.showText}(${viewStates.inputValue})",
                isFinalResult = false
            )
            //isInputSecondValue = true
        }

        isAdvancedCalculated = true
    }

    private fun clickPow2() {
        val result = calculate(viewStates.inputValue, "0", Operator.POW2)

        val resultText = if (result.isSuccess) {
            result.getOrNull().toString()
        } else {
            VibratorHelper.instance.vibrateOnError()
            isErr = true
            result.exceptionOrNull()?.message ?: "Err"
        }

        val newState = viewStates.copy(
            inputValue = resultText
        )

        if (isInputSecondValue) {
            viewStates = newState.copy(
                showText = "${viewStates.lastInputValue}${viewStates.inputOperator.showText}(${viewStates.inputValue})${Operator.POW2.showText}",
                isFinalResult = false,
                lastShowText =
                    if (viewStates.showText.indexOf("=") != -1)
                        viewStates.showText+viewStates.inputValue
                    else viewStates.lastShowText
            )
        }
        else {
            viewStates = newState.copy(
                inputOperator = Operator.NUll,
                lastInputValue = result.getOrNull().toString(),
                showText = "(${viewStates.inputValue})${Operator.POW2.showText}",
                isFinalResult = false
            )
            //isInputSecondValue = true
        }

        isAdvancedCalculated = true
    }

    private fun clickEqual() {
        val inputValueCache = viewStates.inputValue

        if (viewStates.inputOperator == Operator.NUll) {
            VibratorHelper.instance.vibrateOnEqual()
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
                VibratorHelper.instance.vibrateOnEqual()
                val resultText = result.getOrNull()?.toPlainString() ?: "Null"
                val inputValue = if (viewStates.inputValue.substring(0, 1) == "-") "(${viewStates.inputValue})" else viewStates.inputValue
                if (isAdvancedCalculated) {
                    val index = viewStates.showText.indexOf(viewStates.inputOperator.showText)
                    viewStates = if (index != -1 && index == viewStates.showText.lastIndex) {
                        viewStates.copy(
                            inputValue = resultText,
                            showText = "${viewStates.showText}$inputValue=",
                            isFinalResult = true
                        )
                    } else {
                        viewStates.copy(
                            inputValue = resultText,
                            showText = "${viewStates.showText}=",
                            isFinalResult = true
                        )
                    }
                }
                else {
                    viewStates = viewStates.copy(
                        inputValue = resultText,
                        showText = "${viewStates.lastInputValue}${viewStates.inputOperator.showText}$inputValue=",
                        isFinalResult = true
                    )
                }
                isCalculated = true
            }
            else {
                VibratorHelper.instance.vibrateOnError()
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

        // 将计算内容保存到数据库
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                if (!isErr) {  // 不保存错误结果
                    dataBase.history().insert(
                        HistoryData(
                            showText = viewStates.showText,
                            lastInputText = viewStates.lastInputValue,
                            operator = viewStates.inputOperator,
                            result = viewStates.inputValue,
                            inputText = inputValueCache
                        )
                    )
                }
            }
        }
    }

    private fun clickArithmetic(operator: Operator) {
        VibratorHelper.instance.vibrateOnClick()
        var newState = viewStates.copy(
            inputOperator = operator,
            lastInputValue = viewStates.inputValue,
            isFinalResult = false
        )
        if (isCalculated) {
            isCalculated = false
            isInputSecondValue = false
            newState = newState.copy(
                lastShowText =
                if (!isAdvancedCalculated)
                    viewStates.showText+viewStates.inputValue
                else viewStates.lastShowText
            )
        }

        if (isAdvancedCalculated) {
            isInputSecondValue = false

            if (viewStates.inputOperator == Operator.NUll) {  // 第一次添加操作符
                newState = newState.copy(
                    showText = "${viewStates.showText}${operator.showText}"
                )
            }
            else {  // 不是第一次添加操作符，则需要把计算结果置于左边，并去掉高级运算的符号
                isCalculated = false
                isInputSecondValue = false

                clickEqual()

                newState = newState.copy(
                    lastInputValue = viewStates.inputValue,
                    showText = "${viewStates.inputValue}${operator.showText}",
                    inputValue = viewStates.inputValue
                )
            }

        }
        else {
            if (viewStates.inputOperator == Operator.NUll) { // 第一次添加操作符
                newState = newState.copy(
                    showText = "${viewStates.inputValue}${operator.showText}"
                )
            }
            else { // 不是第一次添加操作符，则应该把结果算出来后放到左边
                isCalculated = false
                isInputSecondValue = false

                clickEqual()

                newState = newState.copy(
                    lastInputValue = viewStates.inputValue,
                    showText = "${viewStates.inputValue}${operator.showText}",
                    inputValue = viewStates.inputValue
                )
            }
        }

        viewStates = newState
    }
}

data class StandardState(
    val inputValue: String = "0",
    val inputOperator: Operator = Operator.NUll,
    val lastInputValue: String = "",
    val showText: String = "",
    val isFinalResult: Boolean = false,
    val historyList: List<HistoryData> = listOf(),
    val lastShowText: String = ""
)

sealed class StandardAction {
    data class ToggleHistory(val forceClose: Boolean = false): StandardAction()
    data class ClickBtn(val no: Int): StandardAction()
    data class ReadFromHistory(val item: HistoryData): StandardAction()
    data class DeleteHistory(val item: HistoryData?): StandardAction()
}