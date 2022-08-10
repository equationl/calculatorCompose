package com.equationl.calculator_compose.utils

import androidx.core.text.isDigitsOnly
import com.equationl.calculator_compose.dataModel.Operator
import java.math.BigDecimal

/**
 * BigDecimal 的开平方
 *
 * @link https://stackoverflow.com/a/19743026
 * */
fun BigDecimal.sqrt(scale: Int): BigDecimal {
    val two = BigDecimal.valueOf(2)
    var x0 = BigDecimal("0")
    var x1 = BigDecimal(kotlin.math.sqrt(this.toDouble()))
    while (x0 != x1) {
        x0 = x1
        x1 = this.divide(x0, scale, BigDecimal.ROUND_HALF_UP)
        x1 = x1.add(x0)
        x1 = x1.divide(two, scale, BigDecimal.ROUND_HALF_UP)
    }
    return x1
}

/**
 * 格式化数字（添加逗号分隔符）
 * */
fun String.formatNumber(): String {
    // 如果不是合法数字则不做处理
    if (this.substring(0, 1) != "-" && !this.isDigitsOnly()) return this

    val stringBuilder = StringBuilder(this)

    val pointIndex = stringBuilder.indexOf('.')

    val integer: StringBuilder
    val decimal: StringBuilder


    if (pointIndex == -1) {
        integer = stringBuilder // 整数部分
        decimal = StringBuilder() // 小数部分
    }
    else {
        val stringList = stringBuilder.split('.')
        integer = StringBuilder(stringList[0]) // 整数部分
        decimal = StringBuilder(stringList[1]) // 小数部分
        decimal.insert(0, '.')
    }

    // 添加逗号分隔符
    if (integer.length > 3) {
        val end = if (integer[0] == '-') 2 else 1 // 判断是否有前导符号
        for (i in integer.length-3 downTo end step 3) {
            integer.insert(i, ",")
        }
    }

    return integer.append(decimal).toString()
}


fun calculate(leftValue: String, rightValue: String, operator: Operator): Result<BigDecimal> {
    val left = BigDecimal(leftValue)
    val right = BigDecimal(rightValue)

    when (operator) {
        Operator.ADD -> {
            return Result.success(left.add(right))
        }
        Operator.MINUS -> {
            return Result.success(left.minus(right))
        }
        Operator.MULTIPLY -> {
            return  Result.success(left.multiply(right))
        }
        Operator.Divide -> {
            if (right.signum() == 0) {
                return Result.failure(ArithmeticException("除数不能为零"))
            }
            left.setScale(16)
            return  Result.success(left.divide(right))
        }
        Operator.NUll -> {
            return  Result.success(left)
        }
    }
}