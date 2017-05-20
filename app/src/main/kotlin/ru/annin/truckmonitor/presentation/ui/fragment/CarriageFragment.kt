package ru.annin.truckmonitor.presentation.ui.fragment

import android.os.Bundle
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.arellomobile.mvp.MvpAppCompatFragment
import ru.annin.truckmonitor.R
import ru.annin.truckmonitor.presentation.common.BaseViewDelegate
import ru.annin.truckmonitor.presentation.ui.adapter.CarriageAdapter
import ru.annin.truckmonitor.presentation.ui.adapter.StickyHeaderAdapter
import ru.annin.truckmonitor.presentation.ui.decoration.StickyHeaderDecoration
import ru.annin.truckmonitor.presentation.ui.view.CarriageView
import ru.annin.truckmonitor.presentation.ui.viewmodel.CarriageViewModel

/**
 * Экран "Грузоперевозки".
 *
 * @author Pavel Annin.
 */
class CarriageFragment : MvpAppCompatFragment(), CarriageView {

    companion object {
        private const val ARG_CARRIAGE = "ru.annin.truckmonitor.arg.carriages"
        @JvmStatic fun newInstance(data: ArrayList<CarriageViewModel>? = null) = CarriageFragment().apply {
            arguments = Bundle().apply { putSerializable(ARG_CARRIAGE, data) }
        }
    }

    // Component's
    private lateinit var viewDelegate: ViewDelegate

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.fragment_history, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Date's
        val data = arguments?.getSerializable(ARG_CARRIAGE) as ArrayList<CarriageViewModel>?

        viewDelegate = ViewDelegate(view).apply {
            items = data
        }
    }

    override fun showData(data: List<CarriageViewModel>?) = viewDelegate.run {
        items = data
    }

    /**
     * ViewDelegate экрана "Грузопервозки".
     *
     * @property items Элементы списка.
     */
    private class ViewDelegate(vRoot: View) : BaseViewDelegate(vRoot) {

        // View's
        private val rvCarriages by findView<RecyclerView>(R.id.rv_carriages)

        // Adapter's
        private val carriagesAdapter = CarriageAdapter()

        var items: List<CarriageViewModel>? = null
            set(value) = carriagesAdapter.run {
                this.items = value
                notifyDataSetChanged()
            }

        init {
            rvCarriages.run {
                setHasFixedSize(false)
                itemAnimator = DefaultItemAnimator()
                adapter = carriagesAdapter
                if (carriagesAdapter is StickyHeaderAdapter<*>) {
                    @Suppress("UNCHECKED_CAST")
                    addItemDecoration(StickyHeaderDecoration(carriagesAdapter as StickyHeaderAdapter<RecyclerView.ViewHolder>))
                }
            }
        }
    }
}