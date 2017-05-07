package ru.annin.truckmonitor.presentation.ui.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.TextInputLayout
import android.support.v4.app.DialogFragment
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Button
import android.widget.EditText
import com.arellomobile.mvp.MvpAppCompatActivity
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.PresenterType
import com.arellomobile.mvp.presenter.ProvidePresenter
import ru.annin.truckmonitor.R
import ru.annin.truckmonitor.data.repository.KeyStoreRepository
import ru.annin.truckmonitor.data.repository.RestApiRepository
import ru.annin.truckmonitor.data.repository.SettingsRepository
import ru.annin.truckmonitor.presentation.common.BaseViewDelegate
import ru.annin.truckmonitor.presentation.presenter.LoginPresenter
import ru.annin.truckmonitor.presentation.ui.alert.ErrorAlert
import ru.annin.truckmonitor.presentation.ui.view.LoginView
import ru.annin.truckmonitor.utils.visible

/**
 * Экран "Авторизации".
 *
 * @author Pavel Annin.
 */
class LoginActivity : MvpAppCompatActivity(), LoginView {

    companion object {
        @JvmStatic fun start(context: Context) {
            val intent = Intent(context, LoginActivity::class.java)
            context.startActivity(intent)
        }
    }

    // Component's
    @InjectPresenter(type = PresenterType.LOCAL)
    lateinit var presenter: LoginPresenter
    private lateinit var viewDelegate: ViewDelegate

    @ProvidePresenter(type = PresenterType.LOCAL)
    fun providePresenter() = LoginPresenter(RestApiRepository, KeyStoreRepository, SettingsRepository)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        viewDelegate = ViewDelegate(findViewById(R.id.root)).apply {
            isValid = false
            onDataChange = { email, password -> presenter.onValid(email, password) }
            onSignInClick = { email, password -> presenter.onSignIn(email, password) }
        }
    }

    override fun navigate2Main() = MainActivity.start(this)

    override fun toggleValid(isValid: Boolean) = viewDelegate.run {
        this.isValid = isValid
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
     * View Delegate экрана "авторизации".
     *
     * @property isValid Введенные данные валидные.
     * @property isLoad Индикатор загрузки.
     * @property onDataChange Соыбтие, изменились данные.
     * @property onSignInClick Соыбтие, войти в систему.
     */
    private class ViewDelegate(vRoot: View) : BaseViewDelegate(vRoot) {

        // View's
        private val vLoad by findView<View>(R.id.v_load_indicator)
        private val tilEmail by findView<TextInputLayout>(R.id.til_email)
        private val tilPassword by findView<TextInputLayout>(R.id.til_password)
        private val edtEmail by findView<EditText>(R.id.edt_email)
        private val edtPassword by findView<EditText>(R.id.edt_password)
        private val btnSignIn by findView<Button>(R.id.btn_sign_in)

        // Properties
        var isValid: Boolean? = null
            set(value) = btnSignIn.run { isEnabled = value ?: false }
        var isLoad: Boolean
            get() = vLoad.visible()
            set(value) = vLoad.visible(value)
        private val email: CharSequence
            get() = edtEmail.text
        private val password: CharSequence
            get() = edtPassword.text

        // listener
        var onDataChange: ((CharSequence, CharSequence) -> Unit)? = null
        var onSignInClick: ((CharSequence, CharSequence) -> Unit)? = null

        init {
            // Listener's
            val txtListener = object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                    /** Empty. */
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    onDataChange?.invoke(email, password)
                }

                override fun afterTextChanged(s: Editable?) {
                    /** Empty. */
                }
            }
            edtEmail.addTextChangedListener(txtListener)
            edtPassword.addTextChangedListener(txtListener)
            btnSignIn.setOnClickListener { onSignInClick?.invoke(email, password) }
        }
    }
}