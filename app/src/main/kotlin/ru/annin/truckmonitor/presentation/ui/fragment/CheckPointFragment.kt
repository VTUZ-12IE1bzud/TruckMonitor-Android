package ru.annin.truckmonitor.presentation.ui.fragment

import android.content.Context
import android.os.Bundle
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.arellomobile.mvp.MvpAppCompatFragment
import ru.annin.truckmonitor.R
import ru.annin.truckmonitor.domain.model.CheckPoints
import ru.annin.truckmonitor.presentation.common.BaseViewDelegate
import ru.annin.truckmonitor.presentation.ui.adapter.CheckPointAdapter
import ru.annin.truckmonitor.presentation.ui.view.MainView
import ru.annin.truckmonitor.utils.Analytic

/**
 * Экран контрольных точек.
 *
 * @author Pavel Annin.
 */
class CheckPointFragment : MvpAppCompatFragment() {

    companion object {
        private const val ARG_CHECK_POINTS = "ru.annin.truckmonitor.arg.check_points"
        @JvmStatic fun newInstance(contracts: List<CheckPoints>?) = CheckPointFragment().apply {
            arguments = Bundle().apply {
                putSerializable(ARG_CHECK_POINTS, contracts?.let { ArrayList(it) })
            }
        }
    }

    // Component's
    private lateinit var viewDelegate: ViewDelegate
    private var viewMain: MainView? = null

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        try {
            viewMain = context as MainView
        } catch (error: ClassCastException) {
            Analytic.error(RuntimeException("Must implement MainView"))
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.fragment_check_point, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Data's
        val checkPoints = arguments?.getSerializable(ARG_CHECK_POINTS) as ArrayList<CheckPoints>? ?: ArrayList()

        viewDelegate = ViewDelegate(view).apply {
            onQrScannerClick = { viewMain?.navigate2QrScanner() }
            items = checkPoints
        }
    }

    fun updateCheckPoints(checkPoints: List<CheckPoints>?) = viewDelegate.run {
        items = checkPoints
    }

    private class ViewDelegate(vRoot: View) : BaseViewDelegate(vRoot) {

        // View's
        private val btnQrScanner by findView<Button>(R.id.btn_qr_scanner)
        private val rvCheckPoints by findView<RecyclerView>(R.id.rv_check_points)

        // Adapter's
        private val checkPointAdapters by lazy { CheckPointAdapter() }

        // Properties
        var items: List<CheckPoints>? = null
            set(value) = checkPointAdapters.run {
                items = value
                notifyDataSetChanged()
            }

        // Listener's
        var onQrScannerClick: (() -> Unit)? = null

        init {
            rvCheckPoints.run {
                setHasFixedSize(false)
                itemAnimator = DefaultItemAnimator()
                adapter = checkPointAdapters
            }
            btnQrScanner.setOnClickListener { onQrScannerClick?.invoke() }
        }
    }
}