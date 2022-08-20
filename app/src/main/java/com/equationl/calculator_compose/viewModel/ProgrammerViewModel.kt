package com.equationl.calculator_compose.viewModel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.equationl.calculator_compose.dataModel.*
import com.equationl.calculator_compose.utils.VibratorHelper
import com.equationl.calculator_compose.utils.calculate
import dagger.hilt.android.lifecycle.HiltViewModel
import java.math.BigInteger
import javax.inject.Inject

@HiltViewModel
class ProgrammerViewModel @Inject constructor(): ViewModel() {

    var viewStates by mutableStateOf(ProgrammerState())
        private set

    fun dispatch(action: ProgrammerAction) {
        when (action) {
            is ProgrammerAction.ChangeInputBase -> changeInputBase(action.inputBase)
            is ProgrammerAction.ClickBtn -> clickBtn(action.no)
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

    private fun changeInputBase(inputBase: InputBase) {
        VibratorHelper.instance.vibrateOnClick()
        viewStates = when (inputBase) {
            InputBase.HEX -> {
                if (viewStates.lastInputValue.isNotEmpty()) {
                    viewStates.copy(
                        inputBase = inputBase,
                        inputValue = viewStates.inputHexText,
                        lastInputValue = viewStates.lastInputValue.baseConversion(inputBase)
                    )
                }
                else {
                    viewStates.copy(inputBase = inputBase, inputValue = viewStates.inputHexText)
                }
            }
            InputBase.DEC -> {
                if (viewStates.lastInputValue.isNotEmpty()) {
                    viewStates.copy(inputBase = inputBase,
                        inputValue = viewStates.inputDecText,
                        lastInputValue = viewStates.lastInputValue.baseConversion(inputBase)
                    )
                }
                else {
                    viewStates.copy(inputBase = inputBase, inputValue = viewStates.inputDecText)
                }
            }
            InputBase.OCT -> {
                if (viewStates.lastInputValue.isNotEmpty()) {
                    viewStates.copy(inputBase = inputBase,
                        inputValue = viewStates.inputOctText,
                        lastInputValue = viewStates.lastInputValue.baseConversion(inputBase)
                    )

                }
                else {
                    viewStates.copy(inputBase = inputBase, inputValue = viewStates.inputOctText)
                }
            }
            InputBase.BIN -> {
                if (viewStates.lastInputValue.isNotEmpty()) {
                    viewStates.copy(inputBase = inputBase,
                        inputValue = viewStates.inputBinText,
                        lastInputValue = viewStates.lastInputValue.baseConversion(inputBase)
                    )
                }
                else {
                    viewStates.copy(inputBase = inputBase, inputValue = viewStates.inputBinText)
                }
            }
        }
    }

    private fun clickBtn(no: Int) {
        if (isErr) {
            viewStates = ProgrammerState(inputBase = viewStates.inputBase)
            isErr = false
            isAdvancedCalculated = false
            isCalculated = false
            isInputSecondValue = false
        }

        // 48 == '0'.code
        if (no in KeyIndex_0..KeyIndex_F) {
            VibratorHelper.instance.vibrateOnClick()
            val newValue: String =
                if (viewStates.inputValue == "0") {
                    if (viewStates.inputOperator != Operator.NUll) isInputSecondValue = true
                    if (isAdvancedCalculated && viewStates.inputOperator == Operator.NUll) {  // 如果在输入高级运算符后直接输入数字，则重置状态
                        isAdvancedCalculated = false
                        isCalculated = false
                        isInputSecondValue = false
                        viewStates = ProgrammerState(inputBase = viewStates.inputBase)
                        no.toString()
                    }

                    (48 + no).toChar().toString()
                }
                else if (viewStates.inputOperator != Operator.NUll && !isInputSecondValue) {
                    isCalculated = false
                    isInputSecondValue = true
                    (48+no).toChar().toString()
                }
                else if (isCalculated) {
                    isCalculated = false
                    isInputSecondValue = false
                    viewStates = ProgrammerState(inputBase = viewStates.inputBase)
                    (48+no).toChar().toString()
                }
                else if (isAdvancedCalculated&& viewStates.inputOperator == Operator.NUll) { // 如果在输入高级运算符后直接输入数字，则重置状态
                    isAdvancedCalculated = false
                    isCalculated = false
                    isInputSecondValue = false
                    viewStates = ProgrammerState(inputBase = viewStates.inputBase)
                    no.toString()
                }
                else viewStates.inputValue + (48+no).toChar().toString()

            // 溢出判断
            try {
                newValue.toLong(viewStates.inputBase.number)
            } catch (e: NumberFormatException) {
                return
            }

            viewStates = viewStates.copy(
                inputValue = newValue,
                inputHexText = newValue.baseConversion(InputBase.HEX),
                inputDecText = newValue.baseConversion(InputBase.DEC),
                inputOctText = newValue.baseConversion(InputBase.OCT),
                inputBinText = newValue.baseConversion(InputBase.BIN),
                isFinalResult = false)
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
            KeyIndex_And -> {
                clickArithmetic(Operator.AND)
            }
            KeyIndex_Or -> {
                clickArithmetic(Operator.OR)
            }
            KeyIndex_XOr -> {
                clickArithmetic(Operator.XOR)
            }
            KeyIndex_Lsh -> {
                clickArithmetic(Operator.LSH)
            }
            KeyIndex_Rsh -> {
                clickArithmetic(Operator.RSH)
            }
            KeyIndex_Not -> {
                VibratorHelper.instance.vibrateOnClick()
                clickNot()
            }
            KeyIndex_CE -> { // "CE"
                VibratorHelper.instance.vibrateOnClear()
                if (isCalculated) {
                    clickClear()
                }
                else {
                    clickCE()
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
                    viewStates = viewStates.copy(
                        inputValue = newValue,
                        inputHexText = newValue.baseConversion(InputBase.HEX),
                        inputDecText = newValue.baseConversion(InputBase.DEC),
                        inputOctText = newValue.baseConversion(InputBase.OCT),
                        inputBinText = newValue.baseConversion(InputBase.BIN),
                    )
                }
            }
            KeyIndex_Equal -> { // "="
                clickEqual()
            }
        }
    }

    private fun clickCE() {
        viewStates = viewStates.copy(
            inputValue = "0",
            inputHexText = "0",
            inputDecText = "0",
            inputOctText = "0",
            inputBinText = "0",
        )
    }

    private fun clickClear() {
        isInputSecondValue = false
        isCalculated = false
        isAdvancedCalculated = false
        isErr = false
        viewStates = ProgrammerState(inputBase = viewStates.inputBase)
    }

    private fun String.baseConversion(target: InputBase, current: InputBase = viewStates.inputBase): String {
        if (current == target) return this

        // 如果直接转会出现无法直接转成有符号 long 的问题，所以这里使用 BigInteger 来转
        // 见： https://stackoverflow.com/questions/47452924/kotlin-numberformatexception
        val long = BigInteger(this, current.number).toLong()

        if (target == InputBase.BIN) {
            return java.lang.Long.toBinaryString(long)
        }

        if (target == InputBase.HEX) {
            return java.lang.Long.toHexString(long).uppercase()
        }

        if (target == InputBase.OCT) {
            return java.lang.Long.toOctalString(long)
        }

        // 如果直接使用 toString 会造成直接添加 - 号表示负数，例如十进制的 -10 转为二进制会变成 -1010
        // 这里需要的是无符号的表示方式，即 -10 的二进制数应该用 1111111111111111111111111111111111111111111111111111111111110110 表示
        return long.toString(target.number).uppercase()

        //return this.toLong(current.number).toString(target.number).uppercase()
    }

    private fun clickNot() {
        // 转换成十进制的 long 类型来计算， 然后转回当前进制
        val result = viewStates.inputValue.baseConversion(InputBase.DEC).toLong() // 转至十进制 long
            .inv().toString()  // 计算
            .baseConversion(viewStates.inputBase, InputBase.DEC) // 转回当前进制

        val newState = viewStates.copy(
            inputValue = result,
            inputHexText = result.baseConversion(InputBase.HEX),
            inputDecText = result.baseConversion(InputBase.DEC),
            inputOctText = result.baseConversion(InputBase.OCT),
            inputBinText = result.baseConversion(InputBase.BIN),
        )

        if (isInputSecondValue) {
            viewStates = newState.copy(
                showText = "${viewStates.lastInputValue}${viewStates.inputOperator.showText}${Operator.NOT.showText}(${viewStates.inputValue})",
                isFinalResult = false
            )
        }
        else {
            viewStates = newState.copy(
                inputOperator = Operator.NUll,
                lastInputValue = result,
                showText = "${Operator.NOT.showText}(${viewStates.inputValue})",
                isFinalResult = false
            )
            isInputSecondValue = true
        }

        isAdvancedCalculated = true
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
        }

        if (isAdvancedCalculated) {
            isInputSecondValue = false

            if (viewStates.inputOperator == Operator.NUll) {  // 第一次添加操作符
                newState = newState.copy(
                    showText = "${viewStates.showText}${operator.showText}"
                )
            }
            else { // 不是第一次添加操作符，则需要把计算结果置于左边，并去掉高级运算的符号
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


    private fun clickEqual() {
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
            val result = programmerCalculate()

            if (result.isSuccess) {
                VibratorHelper.instance.vibrateOnEqual()
                val resultText : String = try {
                   result.getOrNull().toString().baseConversion(viewStates.inputBase, InputBase.DEC)
                } catch (e: NumberFormatException) {
                    viewStates = viewStates.copy(
                        inputValue = "Err: 溢出",
                        inputHexText = "溢出",
                        inputDecText = "溢出",
                        inputOctText = "溢出",
                        inputBinText = "溢出",
                        showText = "",
                        isFinalResult = true
                    )
                    isCalculated = false
                    isErr = true
                    return
                }
                val inputValue = if (viewStates.inputValue.substring(0, 1) == "-") "(${viewStates.inputValue})" else viewStates.inputValue
                if (isAdvancedCalculated) {
                    val index = viewStates.showText.indexOf(viewStates.inputOperator.showText)
                    viewStates = if (index != -1 && index == viewStates.showText.lastIndex) {
                        viewStates.copy(
                            inputValue = resultText,
                            inputHexText = resultText.baseConversion(InputBase.HEX),
                            inputDecText = resultText.baseConversion(InputBase.DEC),
                            inputOctText = resultText.baseConversion(InputBase.OCT),
                            inputBinText = resultText.baseConversion(InputBase.BIN),
                            showText = "${viewStates.showText}$inputValue=",
                            isFinalResult = true
                        )
                    } else {
                        viewStates.copy(
                            inputValue = resultText,
                            inputHexText = resultText.baseConversion(InputBase.HEX),
                            inputDecText = resultText.baseConversion(InputBase.DEC),
                            inputOctText = resultText.baseConversion(InputBase.OCT),
                            inputBinText = resultText.baseConversion(InputBase.BIN),
                            showText = "${viewStates.showText}=",
                            isFinalResult = true
                        )
                    }
                }
                else {
                    viewStates = viewStates.copy(
                        inputValue = resultText,
                        inputHexText = resultText.baseConversion(InputBase.HEX),
                        inputDecText = resultText.baseConversion(InputBase.DEC),
                        inputOctText = resultText.baseConversion(InputBase.OCT),
                        inputBinText = resultText.baseConversion(InputBase.BIN),
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
                    inputHexText = "Err",
                    inputDecText = "Err",
                    inputOctText = "Err",
                    inputBinText = "Err",
                    showText = "",
                    isFinalResult = true
                )
                isCalculated = false
                isErr = true
            }
        }

        isAdvancedCalculated = false
    }

    /**
     * 该方法会将输入字符转换成十进制数字计算，并返回计算完成后的十进制数字的字符串形式
     * */
    private fun programmerCalculate(): Result<String> {
        val leftNumber = viewStates.lastInputValue.baseConversion(InputBase.DEC)
        val rightNumber = viewStates.inputValue.baseConversion(InputBase.DEC)

        if (viewStates.inputOperator in BitOperationList) {
            when (viewStates.inputOperator) {
                Operator.AND -> {
                    return Result.success(
                        (leftNumber.toLong() and rightNumber.toLong()).toString()
                    )
                }
                Operator.OR -> {
                    return Result.success(
                        (leftNumber.toLong() or rightNumber.toLong()).toString()
                    )
                }
                Operator.XOR -> {
                    return Result.success(
                        (leftNumber.toLong() xor rightNumber.toLong()).toString()
                    )
                }
                Operator.LSH -> {
                    return try {
                        Result.success(
                            (leftNumber.toLong() shl rightNumber.toInt()).toString()
                        )
                    } catch (e: NumberFormatException) {
                        Result.failure(NumberFormatException("Err: 结果未定义"))
                    }
                }
                Operator.RSH -> {
                    return try {
                        Result.success(
                            (leftNumber.toLong() shr rightNumber.toInt()).toString()
                        )
                    } catch (e: NumberFormatException) {
                        Result.failure(NumberFormatException("Err: 结果未定义"))
                    }
                }
                else -> {
                    // 剩下的操作不应该由此处计算，所以直接返回错误
                    return Result.failure(NumberFormatException("Err: 错误的调用2"))
                }
            }
        }
        else {
            calculate(
                leftNumber,
                rightNumber,
                viewStates.inputOperator,
                scale = 0
            ).fold({
                try {
                    it.toPlainString().toLong()
                } catch (e: NumberFormatException) {
                    e.printStackTrace()
                    return Result.failure(NumberFormatException("Err: 结果溢出"))
                }
                return Result.success(it.toPlainString())
            }, {
                return Result.failure(it)
            })
        }
    }
}

data class ProgrammerState(
    val showText: String = "",
    val inputOperator: Operator = Operator.NUll,
    val lastInputValue: String = "",
    val inputValue: String = "0",
    val inputHexText: String = "0",
    val inputDecText: String = "0",
    val inputOctText: String = "0",
    val inputBinText: String = "0",
    val inputBase: InputBase = InputBase.DEC,
    val isFinalResult: Boolean = false
)

sealed class ProgrammerAction {
    data class ChangeInputBase(val inputBase: InputBase): ProgrammerAction()
    data class ClickBtn(val no: Int): ProgrammerAction()
}