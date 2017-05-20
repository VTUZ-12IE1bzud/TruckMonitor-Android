package ru.annin.truckmonitor.utils

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
