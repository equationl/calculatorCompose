package com.equationl.calculator_compose.viewModel

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.widget.Toast
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.equationl.calculator_compose.overlay.OverlayService
import com.equationl.calculator_compose.utils.VibratorHelper
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
) : ViewModel() {

    var viewStates by mutableStateOf(HomeState())
        private set

    fun dispatch(action: HomeAction) {
        when (action) {
            is HomeAction.ClickMenu -> changeScreenOrientation(action.orientation, action.context)
            is HomeAction.ClickOverlay -> clickOverlay(action.context)
        }
    }

    private fun clickOverlay(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            if (Settings.canDrawOverlays(context)) {
                context.startService(Intent(context, OverlayService::class.java))

                // 返回主页
                Intent(Intent.ACTION_MAIN).apply{
                    addCategory(Intent.CATEGORY_HOME)
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK
                }.let { context.startActivity(it) }
            }
            else {
                Toast.makeText(context, "请授予“显示在其他应用上层”权限后重试", Toast.LENGTH_LONG).show()
                val intent = Intent(
                    Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    Uri.parse("package:${context.packageName}")
                )
                context.startActivity(intent)
            }
        }
        else {
            Toast.makeText(context, "当前系统不支持！", Toast.LENGTH_LONG).show()
        }
    }

    private fun changeScreenOrientation(orientation: Int, context: Context) {
        VibratorHelper.instance.vibrateOnClick()
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
    data class ClickOverlay(val context: Context): HomeAction()
    data class ClickMenu(val orientation: Int, val context: Context): HomeAction()
}