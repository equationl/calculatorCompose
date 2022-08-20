package com.equationl.calculator_compose.viewModel

import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewModelScope
import com.equationl.calculator_compose.MainActivity
import com.equationl.calculator_compose.database.HistoryDb
import com.equationl.calculator_compose.overlay.OverlayService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OverLayViewModel @Inject constructor(
    dataBase: HistoryDb
): StandardViewModel(dataBase) {

    var overlayState by mutableStateOf(OverlayState())
        private set

    private val _viewEvents = Channel<OverlayEvent>(Channel.BUFFERED)
    val viewEvents = _viewEvents.receiveAsFlow()

    private var viewScale: Float = 3f

    override fun dispatch(action: StandardAction) {
        super.dispatch(action)

        when (action) {
            is OverlayAction.ClickClose -> clickClose(action.context)
            is OverlayAction.ClickAdjustSize -> clickAdjustSize()
            is OverlayAction.ClickAdjustAlpha -> clickAdjustAlpha()
            is OverlayAction.ClickBackFullScreen -> clickBackFullScreen(action.context)
            else -> {

            }
        }
    }

    private fun clickBackFullScreen(context: Context) {
        context.startActivity(
            Intent(context, MainActivity::class.java).apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
        )
    }

    private fun clickAdjustAlpha() {
        var alpha = overlayState.backgroundAlpha

        alpha += 0.2f

        if (alpha > 1f) {
            alpha = 0.2f
        }

        overlayState = overlayState.copy(backgroundAlpha = alpha)
    }

    private fun clickClose(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            context.stopService(Intent(context, OverlayService::class.java))
        }
    }

    private fun clickAdjustSize() {
        viewScale += 0.5f
        if (viewScale > 3) viewScale = 1f

        viewModelScope.launch {
            _viewEvents.send(OverlayEvent.ChangeSize(viewScale))
        }
    }

}

data class OverlayState(
    val backgroundAlpha: Float = 1f
)

sealed class OverlayAction: StandardAction() {
    object ClickAdjustSize: OverlayAction()
    object ClickAdjustAlpha: OverlayAction()
    data class ClickClose(val context: Context): OverlayAction()
    data class ClickBackFullScreen(val context: Context): OverlayAction()
}

sealed class OverlayEvent {
    data class ChangeSize(val scale: Float): OverlayEvent()
}