package com.lczv.viewblur

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.renderscript.Allocation
import android.renderscript.Element
import android.renderscript.RenderScript
import android.renderscript.ScriptIntrinsicBlur
import android.view.View
import android.view.ViewTreeObserver
import com.lczv.viewblur.ViewBlur.Companion.applyBlur
import com.lczv.viewblur.ViewBlur.Companion.getBehindView

/**
 * Applies a blur effect on this View, making it's background transparent and displaying whatever is behind it.
 * @param radius Must be a float value between 0 (exclusive) and 25 (inclusive)
 *
 * @throws android.renderscript.RSIllegalArgumentException If the [radius] is out of range.
 */
fun View.setBlurredBackground(radius: Float) {

    val view = this

    view.viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
        override fun onGlobalLayout() {
            background = applyBlur(context, getBehindView(view), radius)
            view.viewTreeObserver.removeOnGlobalLayoutListener(this)
        }
    })
}

class ViewBlur {

    companion object {

        internal fun getBehindView(view: View): Bitmap {

            // Retrieve the position of the view on screen
            val viewLocation = IntArray(2)
            view.getLocationOnScreen(viewLocation)

            // Retrieve the root of this view
            val root = view.rootView

            // Hides the current View
            view.visibility = View.INVISIBLE

            // Takes a screenshot of the root View
            val screenshot =
                Bitmap.createBitmap(
                    root.width,
                    root.height,
                    Bitmap.Config.ARGB_8888
                )
            val canvas = Canvas(screenshot)
            root.draw(canvas)

            // Sets the View visible again
            view.visibility = View.VISIBLE

            // Returns a cropped Bitmap containing whatever is behind this View
            return Bitmap.createBitmap(
                screenshot,
                viewLocation[0],
                viewLocation[1],
                view.width,
                view.height
            )
        }

        fun applyBlur(context: Context, inputBitmap: Bitmap, radius: Float): BitmapDrawable {
            val outputBitmap = Bitmap.createBitmap(inputBitmap.width, inputBitmap.height, Bitmap.Config.ARGB_8888)
            val renderScript = RenderScript.create(context)

            val inputAllocation = Allocation.createFromBitmap(renderScript, inputBitmap)
            val outputAllocation = Allocation.createFromBitmap(renderScript, outputBitmap)

            ScriptIntrinsicBlur.create(renderScript, Element.U8_4(renderScript)).apply {
                setRadius(radius)
                setInput(inputAllocation)
                forEach(outputAllocation)
            }

            outputAllocation.copyTo(outputBitmap)

            return BitmapDrawable(context.resources, outputBitmap)
        }
    }
}