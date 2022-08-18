package com.equationl.calculator_compose.overlay

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.equationl.calculator_compose.database.HistoryDb
import com.equationl.calculator_compose.ui.theme.CalculatorComposeTheme
import com.equationl.calculator_compose.view.StandardScreen
import com.equationl.calculator_compose.viewModel.StandardViewModel
import com.google.accompanist.systemuicontroller.rememberSystemUiController

class OverlayService : ComposeOverlayViewService() {
    @Composable
    override fun Content() = OverlayDraggableContainer {
        val viewModel: StandardViewModel = remember {
            StandardViewModel(
                HistoryDb.create(this@OverlayService)
            )
        }
        CalculatorComposeTheme {
            val backgroundColor = MaterialTheme.colors.background

            Surface(
                modifier = Modifier.fillMaxSize(),
                color = backgroundColor
            ) {
                val systemUiController = rememberSystemUiController()
                val useDarkIcons = MaterialTheme.colors.isLight

                SideEffect {
                    systemUiController.setSystemBarsColor(
                        color = backgroundColor,
                        darkIcons = useDarkIcons
                    )
                }

                StandardScreen(viewModel)
            }
        }
    }
}