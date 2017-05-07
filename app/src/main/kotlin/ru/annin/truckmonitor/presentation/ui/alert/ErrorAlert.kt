package ru.annin.truckmonitor.presentation.ui.alert

import android.app.Dialog
import android.content.DialogInterface
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AlertDialog
import com.arellomobile.mvp.MvpAppCompatDialogFragment
import ru.annin.truckmonitor.R
import ru.annin.truckmonitor.data.InvalidEmailPasswordException
import ru.annin.truckmonitor.data.NoInternetConnectionException
import ru.annin.truckmonitor.data.ServerNotAvailableException

/**
 * Диалог информирующий об ошибке.
 *
 * @author Pavel Annin.
 */
class ErrorAlert : MvpAppCompatDialogFragment() {

    companion object {
        @JvmField val TAG: String = "ru.annin.truckmonitor.ErrorAlert"
        private const val ARG_ERROR = "ru.annin.truckmonitor.arg.error"

        @JvmStatic fun newInstance(error: Throwable) = ErrorAlert().apply {
            arguments = Bundle().apply {
                putSerializable(ARG_ERROR, error)
            }
        }
    }

    private enum class Icon {NONE, INTERNET, WARNING }

    private data class ErrorContent(val icon: Icon, val title: String? = null, val description: String? = null)

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        super.onCreateDialog(savedInstanceState)

        // Data's
        val error = arguments?.getSerializable(ARG_ERROR) as Throwable
        val (icon, title, description) = generateContent(error)

        val iconDrawable: Drawable? = when (icon) {
            ErrorAlert.Icon.NONE -> null
            ErrorAlert.Icon.WARNING -> ContextCompat.getDrawable(activity, R.drawable.ic_error_warning)
            ErrorAlert.Icon.INTERNET -> ContextCompat.getDrawable(activity, R.drawable.ic_error_wifi)
            else -> null
        }

        return AlertDialog.Builder(activity)
                .setIcon(iconDrawable)
                .setTitle(title)
                .setMessage(description)
                .setPositiveButton(R.string.alert_error_positive, { dialogInterface: DialogInterface, _: Int -> dialogInterface.dismiss() })
                .create()
    }

    private fun generateContent(err: Throwable?): ErrorContent {
        val icon: Icon
        val title: String?
        val description: String?
        when (err) {
            is NoInternetConnectionException -> {
                icon = Icon.INTERNET
                title = getString(R.string.alert_error_no_internet_connection_title)
                description = getString(R.string.alert_error_no_internet_connection_description)
            }
            is ServerNotAvailableException -> {
                icon = Icon.WARNING
                title = getString(R.string.alert_error_server_not_available_title)
                description = getString(R.string.alert_error_server_not_available_description)
            }
            is InvalidEmailPasswordException -> {
                icon = Icon.WARNING
                title = getString(R.string.alert_error_invalid_email_password_title)
                description = getString(R.string.alert_error_invalid_email_password_description)
            }
            else -> {
                icon = Icon.WARNING
                title = getString(R.string.alert_error_unknown_title)
                description = getString(R.string.alert_error_unknown_description)
            }
        }
        return ErrorContent(icon, title, description)
    }
}