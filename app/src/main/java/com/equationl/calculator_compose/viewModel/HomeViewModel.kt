package com.equationl.calculator_compose.viewModel

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
                // TODO
            }
        }
    }
}

data class HomeState(
    val test: String = ""
)

sealed class HomeAction {
    object ClickMenu: HomeAction()
}