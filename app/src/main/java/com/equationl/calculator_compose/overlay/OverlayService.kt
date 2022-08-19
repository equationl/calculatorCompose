package com.equationl.calculator_compose.overlay

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.equationl.calculator_compose.database.HistoryDb
import com.equationl.calculator_compose.ui.theme.CalculatorComposeTheme
import com.equationl.calculator_compose.view.OverlayScreen
import com.equationl.calculator_compose.viewModel.OverLayViewModel
import com.equationl.calculator_compose.viewModel.OverlayEvent

@RequiresApi(Build.VERSION_CODES.R)
class OverlayService : ComposeOverlayViewService() {
    @Composable
    override fun Content() = OverlayDraggableContainer {
        val viewModel: OverLayViewModel = remember {
            OverLayViewModel(
                HistoryDb.create(this@OverlayService)
            )
        }

        LaunchedEffect(Unit) {
            viewModel.viewEvents.collect {
                if (it is OverlayEvent.ChangeSize) {
                    updateSize(it.scale)
                }
            }
        }

        CalculatorComposeTheme {
            Surface(
                modifier = Modifier.fillMaxSize(),
                color = MaterialTheme.colors.background
            ) {
                OverlayScreen(viewModel)
            }
        }
    }
}