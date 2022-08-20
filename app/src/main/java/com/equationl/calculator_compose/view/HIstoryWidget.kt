package com.equationl.calculator_compose.view

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.equationl.calculator_compose.dataModel.HistoryData
import com.equationl.calculator_compose.dataModel.Operator
import com.equationl.calculator_compose.ui.theme.CalculatorComposeTheme
import java.text.SimpleDateFormat
import java.util.*

/**
 * @param onDelete 如果 item 为 null 则表示删除所有历史记录，否则删除指定的 item
 * */
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HistoryWidget(
    historyList: List<HistoryData>,
    onClick: (item: HistoryData) -> Unit,
    onDelete: (item: HistoryData?) -> Unit
) {

    Column(
        Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.background)) {
        LazyColumn(modifier = Modifier.weight(9f)) {
            items(
                items = historyList,
                key = { it.id },
            ) {
                Column(
                    horizontalAlignment = Alignment.End,
                    modifier = Modifier
                        .fillMaxWidth()
                        .animateItemPlacement()
                        .padding(8.dp)
                        .combinedClickable(
                            onClick = { onClick(it) },
                            onLongClick = { onDelete(it) }
                        )) {
                    Row(
                        Modifier
                            .fillMaxWidth()
                            .padding(start = 8.dp), horizontalArrangement = Arrangement.Start) {
                        val simpleDateFormat = SimpleDateFormat("MM-dd HH:mm:ss", Locale.CHINA)
                        Text(text = simpleDateFormat.format(Date(it.createTime)))
                    }
                    Text(text = it.showText,fontSize = 22.sp, fontWeight = FontWeight.Light)
                    Text(text = it.result, fontSize = 32.sp, fontWeight = FontWeight.Bold)
                }
            }
        }

        Row(
            Modifier
                .fillMaxSize()
                .weight(1f)
                .padding(16.dp),
            horizontalArrangement = Arrangement.End, verticalAlignment = Alignment.Bottom
        ) {
            Icon(
                imageVector = Icons.Outlined.Delete,
                contentDescription = "delete",
                Modifier
                    .fillMaxHeight()
                    .clickable {
                        onDelete(null)
                    })
        }
    }


}

@Preview(showSystemUi = true)
@Composable
fun HistoryPreview() {
    val testList = listOf(
        HistoryData(0, "1+1=", "1", "1", Operator.ADD, "2"),
        HistoryData(1, "1+1=", "1", "1", Operator.ADD, "2"),
        HistoryData(2, "1+1=", "1", "1", Operator.ADD, "2"),
        HistoryData(3, "1+1=", "1", "1", Operator.ADD, "2"),
        HistoryData(5, "1+1=", "1", "1", Operator.ADD, "2"),
        HistoryData(6, "1+1=", "1", "1", Operator.ADD, "2"),
        HistoryData(7, "1+1=", "1", "1", Operator.ADD, "2"),
        HistoryData(8, "1+1=", "1", "1", Operator.ADD, "2"),
        HistoryData(9, "1+1=", "1", "1", Operator.ADD, "2"),
        HistoryData(10, "1+1=", "1", "1", Operator.ADD, "2"),
        HistoryData(11, "1+1=", "1", "1", Operator.ADD, "2"),
        HistoryData(12, "1+1=", "1", "1", Operator.ADD, "2"),
        HistoryData(13, "1+1=", "1", "1", Operator.ADD, "2"),
        HistoryData(14, "1+1=", "1", "1", Operator.ADD, "2"),
        HistoryData(15, "1+1=", "1", "1", Operator.ADD, "2"),
        HistoryData(16, "1+1=", "1", "1", Operator.ADD, "2"),
        HistoryData(17, "1+1=", "1", "1", Operator.ADD, "2"),
        HistoryData(18, "1+1=", "1", "1", Operator.ADD, "2"),
        HistoryData(19, "1+1=", "1", "1", Operator.ADD, "2"),
        HistoryData(20, "1+1=", "1", "1", Operator.ADD, "2"),
        HistoryData(21, "1+1=", "1", "1", Operator.ADD, "2"),
        HistoryData(22, "1+1=", "1", "1", Operator.ADD, "2"),
        HistoryData(23, "1+1=", "1", "1", Operator.ADD, "2"),
        HistoryData(24, "1+1=", "1", "1", Operator.ADD, "2"),
        HistoryData(25, "1+1=", "1", "1", Operator.ADD, "2"),
        HistoryData(26, "1+1=", "1", "1", Operator.ADD, "2"),
        HistoryData(27, "1+1=", "1", "1", Operator.ADD, "2"),
        HistoryData(28, "1+1=", "1", "1", Operator.ADD, "2"),
        HistoryData(29, "1+1=", "1", "1", Operator.ADD, "2"),
        HistoryData(30, "1+1=", "1", "1", Operator.ADD, "2"),
    )

    CalculatorComposeTheme(false) {
        HistoryWidget(
            historyList = testList,
            onClick = {},
            onDelete = {}
        )
    }
}