package ru.annin.truckmonitor.presentation.common

import android.content.Context
import android.content.res.Resources
import android.support.annotation.IdRes
import android.view.View

/**
 * Базовый ViewDelegate.
 *
 * @author Pavel Annin.
 */
abstract class BaseViewDelegate(protected val vRoot: View) {

    protected val context: Context
        get() = vRoot.context

    protected val resource: Resources
        get() = vRoot.resources

    protected inline fun <reified T : View> findView(@IdRes id: Int, root: View = vRoot) = lazy { root.findViewById(id) as T }
}