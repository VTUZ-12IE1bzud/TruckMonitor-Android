package ru.annin.truckmonitor.presentation.ui.activity

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.arellomobile.mvp.MvpAppCompatActivity
import com.bumptech.glide.Glide
import jp.wasabeef.glide.transformations.CropCircleTransformation
import ru.annin.truckmonitor.R
import ru.annin.truckmonitor.domain.model.DetailCarriageResponse
import ru.annin.truckmonitor.presentation.common.BaseViewDelegate
import ru.annin.truckmonitor.presentation.ui.adapter.DetailContractAdapter

/**
 * Экран просмотра контракта.
 *
 * @author Pavel Annin.
 */
class DetailContractActivity : MvpAppCompatActivity() {

    companion object {
        private const val EXTRA_CONTRACT = "ru.annin.truckmonitor.extra.contract"

        @JvmStatic fun start(context: Context, contract: DetailCarriageResponse) {
            val intent = Intent(context, DetailContractActivity::class.java).apply {
                putExtra(EXTRA_CONTRACT, contract)
            }
            context.startActivity(intent)
        }
    }

    // Component's
    private lateinit var viewDelegate: ViewDelegate

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_contract)

        // Data's
        val data = intent?.extras?.getSerializable(EXTRA_CONTRACT) as DetailCarriageResponse?

        viewDelegate = ViewDelegate(findViewById(R.id.root)).apply {
            onBackClick = { navigate2Finish() }
            onCustomsLinkClick = { data?.contract?.customsLink?.let { navigate2Gallery(it) } }
            this.data = data
        }
    }

    private fun navigate2Gallery(url: String) {
        val intent = Intent().apply {
            action = Intent.ACTION_VIEW
            setDataAndType(Uri.parse(url), "image/*")
        }
        startActivity(intent)
    }

    private fun navigate2Finish() = finish()

    private class ViewDelegate(vRoot: View) : BaseViewDelegate(vRoot) {

        // View's
        private val vToolbar by findView<Toolbar>(R.id.v_toolbar)
        private val txtContractNumber by findView<TextView>(R.id.txt_contract_number)
        private val txtStoreBeforeName by findView<TextView>(R.id.txt_store_before_name)
        private val txtStoreBeforeAddress by findView<TextView>(R.id.txt_store_before_address)
        private val txtStoreFromName by findView<TextView>(R.id.txt_store_from_name)
        private val txtStoreFromAddress by findView<TextView>(R.id.txt_store_from_address)
        private val ivManagerPhoto by findView<ImageView>(R.id.iv_manager_photo)
        private val txtManagerName by findView<TextView>(R.id.txt_manager_name)
        private val txtManagerPhone by findView<TextView>(R.id.txt_manager_phone)
        private val ivCustomsLink by findView<ImageView>(R.id.iv_customs_link)
        private val rvNomenclature by findView<RecyclerView>(R.id.rv_nomenclature)

        // Adapter's
        private val nomenclatureAdapter by lazy { DetailContractAdapter() }

        // Properties
        var data: DetailCarriageResponse? = null
            set(value) {
                value?.let {
                    txtContractNumber.text = resource.getString(R.string.detail_contract_contract_number_format, it.contract.number)
                    txtStoreBeforeName.text = it.contract.before.name
                    txtStoreBeforeAddress.text = it.contract.before.address
                    txtStoreFromName.text = it.contract.from.name
                    txtStoreFromAddress.text = it.contract.from.address
                    Glide.with(context).load(it.contract.manager.photolink).bitmapTransform(CropCircleTransformation(context)).crossFade().into(ivManagerPhoto)
                    txtManagerName.text = it.contract.manager.name
                    txtManagerPhone.text = it.contract.manager.phone
                    Glide.with(context).load(it.contract.customsLink).crossFade().into(ivCustomsLink)

                    nomenclatureAdapter.items = it.nomenclatures
                    nomenclatureAdapter.packaging = it.contract.packaging
                    nomenclatureAdapter.notifyDataSetChanged()
                }
            }

        // Listener's
        var onBackClick: (() -> Unit)? = null
        var onCustomsLinkClick: (() -> Unit)? = null

        init {
            rvNomenclature.run {
                setHasFixedSize(false)
                itemAnimator = DefaultItemAnimator()
                adapter = nomenclatureAdapter
            }
            vToolbar.setNavigationOnClickListener { onBackClick?.invoke() }
            ivCustomsLink.setOnClickListener { onCustomsLinkClick?.invoke() }
        }
    }
}