package com.equationl.calculator_compose.viewModel

import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.lifecycle.viewModelScope
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

    private val _viewEvents = Channel<OverlayEvent>(Channel.BUFFERED)
    val viewEvents = _viewEvents.receiveAsFlow()

    private var viewScale: Float = 3f

    override fun dispatch(action: StandardAction) {
        super.dispatch(action)

        when (action) {
            is OverlayAction.ClickClose -> clickClose(action.context)
            is OverlayAction.ClickAdjustSize -> clickAdjustSize()
            else -> {

            }
        }
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

sealed class OverlayAction: StandardAction() {
    object ClickAdjustSize: OverlayAction()
    data class ClickClose(val context: Context): OverlayAction()
}

sealed class OverlayEvent {
    data class ChangeSize(val scale: Float): OverlayEvent()
}