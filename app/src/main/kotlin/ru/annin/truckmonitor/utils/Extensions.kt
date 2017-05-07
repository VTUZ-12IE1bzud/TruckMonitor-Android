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