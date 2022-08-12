package com.equationl.calculator_compose.viewModel

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
) : ViewModel() {

    var viewStates by mutableStateOf(HomeState())
        private set

    fun dispatch(action: HomeAction) {
        when (action) {
            is HomeAction.ClickMenu -> {
                changeScreenOrientation(action.orientation, action.context)
            }
        }
    }

    private fun changeScreenOrientation(orientation: Int, context: Context) {
        val activity = context.findActivity() ?: return
        activity.requestedOrientation = orientation
    }

    private fun Context.findActivity(): Activity? = when (this) {
        is Activity -> this
        is ContextWrapper -> baseContext.findActivity()
        else -> null
    }
}

data class HomeState(
    val test: String = ""
)

sealed class HomeAction {
    data class ClickMenu(val orientation: Int, val context: Context): HomeAction()
}