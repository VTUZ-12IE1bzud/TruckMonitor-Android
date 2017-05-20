package ru.annin.truckmonitor.presentation.ui.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.DialogFragment
import android.support.v4.app.FragmentManager
import android.support.v4.view.ViewPager
import android.support.v7.widget.Toolbar
import android.view.View
import com.arellomobile.mvp.MvpAppCompatActivity
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.PresenterType
import com.arellomobile.mvp.presenter.ProvidePresenter
import ru.annin.truckmonitor.R
import ru.annin.truckmonitor.data.repository.KeyStoreRepository
import ru.annin.truckmonitor.data.repository.RestApiRepository
import ru.annin.truckmonitor.presentation.common.BaseViewDelegate
import ru.annin.truckmonitor.presentation.presenter.HistoryPresenter
import ru.annin.truckmonitor.presentation.ui.adapter.CarriagePagerAdapter
import ru.annin.truckmonitor.presentation.ui.alert.ErrorAlert
import ru.annin.truckmonitor.presentation.ui.view.HistoryView
import ru.annin.truckmonitor.presentation.ui.viewmodel.CarriageViewModel
import ru.annin.truckmonitor.utils.visible

/**
 * Экран истории.
 *
 * @author Pavel Annin.
 */
class HistoryActivity : MvpAppCompatActivity(), HistoryView {

    companion object {
        @JvmStatic fun start(context: Context) {
            val intent = Intent(context, HistoryActivity::class.java)
            context.startActivity(intent)
        }
    }

    // Component's
    @InjectPresenter(type = PresenterType.LOCAL)
    lateinit var presenter: HistoryPresenter
    private lateinit var viewDelegate: ViewDelegate

    @ProvidePresenter(type = PresenterType.LOCAL)
    fun providePresenter() = HistoryPresenter(RestApiRepository, KeyStoreRepository)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history)
        viewDelegate = ViewDelegate(findViewById(R.id.root), supportFragmentManager).apply {
            onBackClick = { presenter.onBack() }
        }
    }

    override fun navigate2Finish() = finish()

    override fun showCarriage(archive: List<CarriageViewModel>?, onward: List<CarriageViewModel>?) = viewDelegate.run {
        showCarriages(archive, onward)
    }

    override fun toggleLoading(isLoad: Boolean) = viewDelegate.run {
        this.isLoad = isLoad
    }

    override fun error(err: Throwable) {
        var fragment = supportFragmentManager.findFragmentByTag(ErrorAlert.TAG)
        if (fragment == null) {
            fragment = ErrorAlert.newInstance(err)
            (fragment as DialogFragment).show(supportFragmentManager, ErrorAlert.TAG)
        }
    }

    private class ViewDelegate(vRoot: View,
                               private val fm: FragmentManager) : BaseViewDelegate(vRoot) {

        // View's
        private val vToolbar by findView<Toolbar>(R.id.v_toolbar)
        private val vTab by findView<TabLayout>(R.id.v_tab)
        private val cntPager by findView<ViewPager>(R.id.cnt_pager)
        private val vLoad by findView<View>(R.id.v_load_indicator)

        // Adapter's
        private val pagerAdapter = CarriagePagerAdapter(fm, resource)

        // Properties
        var isLoad: Boolean
            get() = vLoad.visible()
            set(value) = vLoad.visible(value)

        // Listener's
        var onBackClick: (() -> Unit)? = null


        init {
            // Setup
            vTab.setupWithViewPager(cntPager)

            // Listener's
            vToolbar.setNavigationOnClickListener { onBackClick?.invoke() }
        }

        fun showCarriages(archive: List<CarriageViewModel>?, onward: List<CarriageViewModel>?) {
            pagerAdapter.run {
                archiveCarriage = archive
                onwardCarriage = onward
                notifyDataSetChanged()
            }
            cntPager.run {
                adapter = pagerAdapter
            }
        }
    }
}