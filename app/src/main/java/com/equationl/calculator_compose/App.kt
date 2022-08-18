package com.equationl.calculator_compose

import android.app.Application
import com.equationl.calculator_compose.utils.VibratorHelper
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class App: Application() {
    override fun onCreate() {
        super.onCreate()
        VibratorHelper.instance.init(this)
    }
}