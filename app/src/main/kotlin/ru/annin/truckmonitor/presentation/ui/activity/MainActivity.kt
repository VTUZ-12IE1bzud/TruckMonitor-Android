package ru.annin.truckmonitor.presentation.ui.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.app.DialogFragment
import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout
import android.view.View
import com.arellomobile.mvp.MvpAppCompatActivity
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.PresenterType
import com.arellomobile.mvp.presenter.ProvidePresenter
import ru.annin.truckmonitor.R
import ru.annin.truckmonitor.data.repository.KeyStoreRepository
import ru.annin.truckmonitor.data.repository.SettingsRepository
import ru.annin.truckmonitor.domain.value.NavigationMenuItem
import ru.annin.truckmonitor.presentation.common.BaseViewDelegate
import ru.annin.truckmonitor.presentation.presenter.MainPresenter
import ru.annin.truckmonitor.presentation.ui.alert.ErrorAlert
import ru.annin.truckmonitor.presentation.ui.view.MainView
import ru.annin.truckmonitor.utils.visible

/**
 * Главный экран.
 *
 * @author Pavel Annin.
 */
class MainActivity : MvpAppCompatActivity(), MainView {

    companion object {
        @JvmStatic fun start(context: Context) {
            val intent = Intent(context, MainActivity::class.java)
            context.startActivity(intent)
        }
    }

    // Component's
    @InjectPresenter(type = PresenterType.LOCAL)
    lateinit var presenter: MainPresenter
    private lateinit var viewDelegate: ViewDelegate

    @ProvidePresenter(type = PresenterType.LOCAL)
    fun providePresenter() = MainPresenter(KeyStoreRepository, SettingsRepository)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        viewDelegate = ViewDelegate(findViewById(R.id.root)).apply {
            onItemClick= {
                when(it) {
                    NavigationMenuItem.HOME -> TODO()
                    NavigationMenuItem.HISTORY -> presenter.onHistoryOpen()
                    NavigationMenuItem.USER_INFO -> presenter.onUserInfoOpen()
                    NavigationMenuItem.ABOUT -> TODO()
                    NavigationMenuItem.LOG_OUT -> presenter.onLogOut()
                }
            }
        }
    }

    override fun navigate2Me() = MeActivity.start(this)

    override fun navigate2History() = HistoryActivity.start(this)

    override fun navigate2Login() {
        LoginActivity.start(this)
        finish()
    }

    override fun toggleLoad(isLoad: Boolean) = viewDelegate.run {
        this.isLoad = isLoad
    }

    override fun error(err: Throwable) {
        var fragment = supportFragmentManager.findFragmentByTag(ErrorAlert.TAG)
        if (fragment == null) {
            fragment = ErrorAlert.newInstance(err)
            (fragment as DialogFragment).show(supportFragmentManager, ErrorAlert.TAG)
        }
    }

    /**
     * View Delegate главного экрана.
     *
     * @property isNavigationOpen Состояние навигационного меню.
     * @property isLoad Индикатор загрузки.
     * @property onItemClick Событие, выбран пнукт навигации.
     */
    private class ViewDelegate(vRoot: View) : BaseViewDelegate(vRoot) {

        // View's
        private val vLoad by findView<View>(R.id.v_load_indicator)
        private val vDrawer by findView<DrawerLayout>(R.id.root)
        private val vNavigation by findView<NavigationView>(R.id.v_navigation)

        // Data's
        private var tempSelectItem: NavigationMenuItem? = null

        // Properties
        var isNavigationOpen: Boolean
            get() = vDrawer.isDrawerVisible(GravityCompat.START)
            set(value) = vDrawer.run {
                if (isNavigationOpen != value) {
                    if (value) vDrawer.openDrawer(GravityCompat.START) else vDrawer.closeDrawer(GravityCompat.START)
                }
            }
        var isLoad: Boolean
            get() = vLoad.visible()
            set(value) = vLoad.visible(value)

        // Listener's
        var onItemClick: ((NavigationMenuItem) -> Unit)? = null

        init {
            // Listener's
            vDrawer.addDrawerListener(object : DrawerLayout.DrawerListener {
                override fun onDrawerStateChanged(newState: Int) {
                    /** Empty */
                }

                override fun onDrawerSlide(drawerView: View?, slideOffset: Float) {
                    /** Empty */
                }

                override fun onDrawerOpened(drawerView: View?) {
                    /** Empty */
                }

                override fun onDrawerClosed(drawerView: View?) {
                    tempSelectItem?.let { onItemClick?.invoke(it) }
                    tempSelectItem = null
                }
            })
            vNavigation.setNavigationItemSelectedListener {
                tempSelectItem = when (it.itemId) {
                    R.id.action_home -> NavigationMenuItem.HOME
                    R.id.action_history -> NavigationMenuItem.HISTORY
                    R.id.action_user_info -> NavigationMenuItem.USER_INFO
                    R.id.action_about -> NavigationMenuItem.ABOUT
                    R.id.action_log_out -> NavigationMenuItem.LOG_OUT
                    else -> return@setNavigationItemSelectedListener false
                }
                isNavigationOpen = false
                return@setNavigationItemSelectedListener true
            }
        }
    }
}