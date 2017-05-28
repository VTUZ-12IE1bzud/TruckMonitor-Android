package ru.annin.truckmonitor.presentation.ui.fragment

import android.os.Bundle
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.arellomobile.mvp.MvpAppCompatFragment
import ru.annin.truckmonitor.R
import ru.annin.truckmonitor.domain.model.DetailCarriageResponse
import ru.annin.truckmonitor.presentation.common.BaseViewDelegate
import ru.annin.truckmonitor.presentation.ui.activity.DetailContractActivity
import ru.annin.truckmonitor.presentation.ui.adapter.ContractAdapter

/**
 * Экран "Договоров".
 *
 * @author Pavel Annin.
 */
class ContractFragment : MvpAppCompatFragment() {

    companion object {
        private const val ARG_CONTRACTS = "ru.annin.truckmonitor.arg.contracts"
        @JvmStatic fun newInstance(contracts: List<DetailCarriageResponse>?) = ContractFragment().apply {
            arguments = Bundle().apply {
                putSerializable(ARG_CONTRACTS, contracts?.let { ArrayList(it) })
            }
        }
    }

    // Component's
    private lateinit var viewDelegate: ViewDelegate

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.fragment_contract, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Data's
        val contracts = arguments?.getSerializable(ARG_CONTRACTS) as ArrayList<DetailCarriageResponse>? ?: ArrayList()

        viewDelegate = ViewDelegate(view).apply {
            onContractClick = { navigate2DetailContract(it) }
            items = contracts
        }
    }

    fun updateContract(contracts: List<DetailCarriageResponse>?) = viewDelegate.run {
        items = contracts
    }

    private fun navigate2DetailContract(contract: DetailCarriageResponse)
            = DetailContractActivity.start(activity, contract)

    private class ViewDelegate(vRoot: View) : BaseViewDelegate(vRoot) {

        // View's
        private val rvContracts by findView<RecyclerView>(R.id.rv_contract)

        // Adapter's
        private val contractAdapter by lazy { ContractAdapter(onItemClick = { onContractClick?.invoke(it) }) }

        // Properties
        var items: List<DetailCarriageResponse>? = null
            set(value) = contractAdapter.run {
                this.items = value
                notifyDataSetChanged()
            }

        // Listener's
        var onContractClick: ((DetailCarriageResponse) -> Unit)? = null

        init {
            rvContracts.run {
                setHasFixedSize(false)
                itemAnimator = DefaultItemAnimator()
                adapter = contractAdapter
            }
        }
    }
}