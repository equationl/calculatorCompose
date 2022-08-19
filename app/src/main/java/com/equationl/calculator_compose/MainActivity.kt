package com.equationl.calculator_compose

import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Modifier
import com.equationl.calculator_compose.overlay.OverlayService
import com.equationl.calculator_compose.ui.theme.CalculatorComposeTheme
import com.equationl.calculator_compose.view.HomeScreen
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
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

                    HomeScreen()
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()

        // 每次打开主页都要把悬浮界面关闭
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            stopService(Intent(this, OverlayService::class.java))
        }
    }
}