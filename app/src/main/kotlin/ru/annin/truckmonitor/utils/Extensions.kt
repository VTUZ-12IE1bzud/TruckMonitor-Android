package ru.annin.truckmonitor.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.os.Build
import android.support.v4.graphics.drawable.DrawableCompat
import android.support.v7.widget.AppCompatDrawableManager
import android.view.View




/**
 * Kotlin Extensions
 *
 * @author Pavel Annin.
 */

fun View.visible(): Boolean = this.visibility == View.VISIBLE

fun View.visible(isVisible: Boolean) = this.run {
    visibility = if (isVisible) View.VISIBLE else View.GONE
}

fun <T1 : Any, T2 : Any, R : Any> safeLet(p1: T1?, p2: T2?, block: (T1, T2) -> R?): R? {
    return if (p1 != null && p2 != null) block(p1, p2) else null
}

fun Int.toBitmap(context: Context) : Bitmap {
    var drawable = AppCompatDrawableManager.get().getDrawable(context, this)
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
        drawable = DrawableCompat.wrap(drawable).mutate()
    }
    val bitmap = Bitmap.createBitmap(drawable.intrinsicWidth,
            drawable.intrinsicHeight, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(bitmap)
    drawable.setBounds(0, 0, canvas.width, canvas.height)
    drawable.draw(canvas)
    return bitmap
}