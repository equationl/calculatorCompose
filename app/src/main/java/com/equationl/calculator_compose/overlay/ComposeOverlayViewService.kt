package com.equationl.calculator_compose.overlay

import android.graphics.PixelFormat
import android.view.Gravity
import android.view.WindowManager
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.ComposeView
import androidx.lifecycle.ViewTreeLifecycleOwner
import androidx.lifecycle.ViewTreeViewModelStoreOwner
import androidx.savedstate.setViewTreeSavedStateRegistryOwner
import kotlin.math.roundToInt

/**
 * Service that is ready to display compose overlay view
 * @author Quentin Nivelais
 * @link https://gist.github.com/KONFeature/2f84436e1c0a1926505cac934d470f90
 */
abstract class ComposeOverlayViewService : ViewReadyService() {

    // Build the layout param for our popup
    private val layoutParams by lazy {
        WindowManager.LayoutParams(
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
            PixelFormat.TRANSLUCENT
        ).apply {
            gravity = Gravity.TOP or Gravity.CENTER_HORIZONTAL
            width = 200
            height = 500
        }
    }

    // The current offset of our overlay composable
    private var overlayOffset by mutableStateOf(Offset.Zero)

    // Access our window manager
    private val windowManager by lazy {
        overlayContext.getSystemService(WindowManager::class.java)
    }

    // Build our compose view
    private val composeView by lazy {
        ComposeView(overlayContext)
    }

    override fun onCreate() {
        super.onCreate()

        // Bound the compose lifecycle, view model and view tree saved state, into our view service
        ViewTreeLifecycleOwner.set(composeView, this)
        ViewTreeViewModelStoreOwner.set(composeView) { viewModelStore }
        composeView.setViewTreeSavedStateRegistryOwner(this)

        // Set the content of our compose view
        composeView.setContent { Content() }

        // Push the compose view into our window manager
        windowManager.addView(composeView, layoutParams)
    }

    override fun onDestroy() {
        super.onDestroy()
        // Remove our compose view from the window manager
        windowManager.removeView(composeView)
    }

    @Composable
    abstract fun Content()

    /**
     * Draggable box container (not used by default, since not every overlay should be draggable)
     */
    @Composable
    internal fun OverlayDraggableContainer(modifier: Modifier = Modifier, content: @Composable BoxScope.() -> Unit) =
        Box(
            modifier = modifier.pointerInput(Unit) {
                detectDragGestures { change, dragAmount ->
                    change.consume()

                    // Update our current offset
                    val newOffset = overlayOffset + dragAmount
                    overlayOffset = newOffset

                    // Update the layout params, and then the view
                    layoutParams.apply {
                        x = overlayOffset.x.roundToInt()
                        y = overlayOffset.y.roundToInt()
                    }
                    windowManager.updateViewLayout(composeView, layoutParams)
                }
            },
            content = content
        )
}