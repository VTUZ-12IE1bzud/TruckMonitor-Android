package ru.annin.truckmonitor.presentation.ui.activity

import android.content.Context
import android.content.Intent
import com.arellomobile.mvp.MvpAppCompatActivity

/**
 * @author Pavel Annin.
 */
class MainActivity : MvpAppCompatActivity() {

    companion object {
        @JvmStatic fun start(context: Context) {
            val intent = Intent(context, MainActivity::class.java)
            context.startActivity(intent)
        }
    }
}