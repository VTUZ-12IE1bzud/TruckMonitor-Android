package ru.annin.truckmonitor.presentation.ui.decoration

import android.graphics.Canvas
import android.graphics.Rect
import android.support.v4.view.ViewCompat
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import ru.annin.truckmonitor.presentation.ui.adapter.StickyHeaderAdapter

/**
 * @author Pavel Annin.
 */
class StickyHeaderDecoration(
        val adapter: StickyHeaderAdapter<RecyclerView.ViewHolder>,
        val renderInline: Boolean = false) : RecyclerView.ItemDecoration() {

    companion object {
        const val NO_HEADER_ID = 1L
    }

    private val headerCache: MutableMap<Long, RecyclerView.ViewHolder> = HashMap()

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        val position = parent.getChildAdapterPosition(view)
        var headerHeight = 0

        if (position != RecyclerView.NO_POSITION
                && hasHeader(position)
                && showHeaderAboveItem(position)) {

            val header = getHeader(parent, position).itemView
            headerHeight = getHeaderHeightForLayout(header)
        }

        outRect.set(0, headerHeight, 0, 0)
    }

    override fun onDrawOver(canvas: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        val count = parent.childCount
        var previousHeaderId: Long = -1

        for (layoutPos in 0..count - 1) {
            val child = parent.getChildAt(layoutPos)
            val adapterPos = parent.getChildAdapterPosition(child)

            if (adapterPos != RecyclerView.NO_POSITION && hasHeader(adapterPos)) {
                val headerId = adapter.getHeaderId(adapterPos)

                if (headerId != previousHeaderId) {
                    previousHeaderId = headerId
                    val header = getHeader(parent, adapterPos).itemView
                    canvas.save()

                    val left = child.left
                    val top = getHeaderTop(parent, child, header, adapterPos, layoutPos)
                    canvas.translate(left.toFloat(), top.toFloat())

                    header.translationX = left.toFloat()
                    header.translationY = top.toFloat()
                    header.draw(canvas)
                    canvas.restore()
                }
            }
        }
    }

    fun clearHeaderCache() {
        headerCache.clear()
    }

    fun findHeaderViewHolder(x: Float, y: Float): View? {
        for (holder in headerCache.values) {
            val child: View = holder.itemView
            val translationX: Float = ViewCompat.getTranslationX(child)
            val translationY: Float = ViewCompat.getTranslationY(child)

            if (x >=  child.left + translationX
                    && x <= child.right + translationX
                    && y >= child.top + translationY
                    && y <= child.bottom + translationY) {
                return child
            }
        }
        return null
    }

    private fun showHeaderAboveItem(itemAdapterPosition: Int): Boolean {
        if (itemAdapterPosition == 0) {
            return true
        }
        return adapter.getHeaderId(itemAdapterPosition - 1) != adapter.getHeaderId(itemAdapterPosition)
    }

    private fun hasHeader(position: Int): Boolean {
        return adapter.getHeaderId(position) != NO_HEADER_ID
    }

    private fun getHeader(parent: RecyclerView, position: Int): RecyclerView.ViewHolder {
        val key = adapter.getHeaderId(position)

        if (headerCache.containsKey(key)) {
            return headerCache[key]!!
        } else {
            val holder: RecyclerView.ViewHolder = adapter.onCreateHeaderViewHolder(parent)
            val header: View = holder.itemView

            adapter.onBindHeaderViewHolder(holder, position)

            val widthSpec: Int = View.MeasureSpec.makeMeasureSpec(parent.measuredWidth, View.MeasureSpec.EXACTLY)
            val heightSpec: Int = View.MeasureSpec.makeMeasureSpec(parent.measuredHeight, View.MeasureSpec.UNSPECIFIED)

            val childWidth: Int = ViewGroup.getChildMeasureSpec(widthSpec, parent.paddingLeft + parent.paddingRight, header.layoutParams.width)
            val childHeight: Int = ViewGroup.getChildMeasureSpec(heightSpec, parent.paddingTop + parent.paddingBottom, header.layoutParams.height)

            header.measure(childWidth, childHeight)
            header.layout(0,0, header.measuredWidth, header.measuredHeight)

            headerCache.put(key, holder)
            return holder
        }
    }

    private fun getHeaderTop(parent: RecyclerView, child: View, header: View, adapterPos: Int, layoutPos: Int): Int {
        val headerHeight = getHeaderHeightForLayout(header)
        var top: Int = (child.y - headerHeight).toInt()
        if (layoutPos == 0) {
            val count = parent.childCount
            val currentId = adapter.getHeaderId(adapterPos)
            // find next view with header and compute the offscreen push if needed
            for (i in 1..count - 1) {
                val adapterPosHere = parent.getChildAdapterPosition(parent.getChildAt(i))
                if (adapterPosHere != RecyclerView.NO_POSITION) {
                    val nextId = adapter.getHeaderId(adapterPosHere)
                    if (nextId != currentId) {
                        val next = parent.getChildAt(i)
                        val offset = next.y.toInt() - (headerHeight + getHeader(parent, adapterPosHere).itemView.height)
                        if (offset < 0) {
                            return offset
                        } else {
                            break
                        }
                    }
                }
            }

            top = Math.max(0, top)
        }

        return top
    }

    private fun getHeaderHeightForLayout(header: View): Int {
        return if (renderInline) 0 else header.height
    }
}