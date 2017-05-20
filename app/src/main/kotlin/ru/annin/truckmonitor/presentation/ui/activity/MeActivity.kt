package ru.annin.truckmonitor.presentation.ui.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v7.widget.Toolbar
import android.text.format.DateFormat
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.arellomobile.mvp.MvpAppCompatActivity
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.PresenterType
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.bumptech.glide.Glide
import jp.wasabeef.glide.transformations.CropCircleTransformation
import ru.annin.truckmonitor.R
import ru.annin.truckmonitor.data.repository.KeyStoreRepository
import ru.annin.truckmonitor.data.repository.RestApiRepository
import ru.annin.truckmonitor.domain.model.UserInfoResponse
import ru.annin.truckmonitor.presentation.common.BaseViewDelegate
import ru.annin.truckmonitor.presentation.presenter.MePresenter
import ru.annin.truckmonitor.presentation.ui.alert.ErrorAlert
import ru.annin.truckmonitor.presentation.ui.view.MeView
import ru.annin.truckmonitor.utils.visible

/**
 * Экран информации о водителе.
 *
 * @author Pavel Annin.
 */
class MeActivity : MvpAppCompatActivity(), MeView {

    companion object {
        @JvmStatic fun start(context: Context) {
            val intent = Intent(context, MeActivity::class.java)
            context.startActivity(intent)
        }
    }

    // Component's
    @InjectPresenter(type = PresenterType.LOCAL)
    lateinit var presenter: MePresenter
    private lateinit var viewDelegate: ViewDelegate

    @ProvidePresenter(type = PresenterType.LOCAL)
    fun providePresenter() = MePresenter(RestApiRepository, KeyStoreRepository)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_me)
        viewDelegate = ViewDelegate(findViewById(R.id.root)).apply {
            onBackClick = { presenter.onBack() }
        }
    }

    override fun navigate2Finish() = finish()

    override fun showUserData(data: UserInfoResponse?) = viewDelegate.run {
        this.data = data
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
     * View Delegate экрана "О водителе".
     *
     * @property isLoad Индикатор загрузки.
     * @property data Информация о водителе.
     * @property onBackClick Событие переход на предыдуший экран.
     */
    private class ViewDelegate(vRoot: View) : BaseViewDelegate(vRoot) {

        // View's
        private val vToolbar by findView<Toolbar>(R.id.v_toolbar)
        private val vLoad by findView<View>(R.id.v_load_indicator)
        private val ivUserPhoto by findView<ImageView>(R.id.iv_user_photo)
        private val txtUserName by findView<TextView>(R.id.txt_user_name)
        private val txtUserBirthDate by findView<TextView>(R.id.txt_user_birth_date)
        private val txtUserEmail by findView<TextView>(R.id.txt_user_email)
        private val txtUserPhone by findView<TextView>(R.id.txt_user_phone)

        // Properties
        var isLoad: Boolean
            get() = vLoad.visible()
            set(value) = vLoad.visible(value)
        var data: UserInfoResponse? = null
            set(value) {
                value?.run {
                    Glide.with(context).load(photo).bitmapTransform(CropCircleTransformation(context)).crossFade().into(ivUserPhoto)
                    txtUserName.text = "$surname $name $patronymic"
                    birthDate?.let { txtUserBirthDate.text = DateFormat.getDateFormat(context).format(it) }
                    txtUserEmail.text = email
                    txtUserPhone.text = phone
                }
            }

        // Listener's
        var onBackClick: (() -> Unit)? = null

        init {
            // Listener's
            vToolbar.setNavigationOnClickListener { onBackClick?.invoke() }
        }
    }
}