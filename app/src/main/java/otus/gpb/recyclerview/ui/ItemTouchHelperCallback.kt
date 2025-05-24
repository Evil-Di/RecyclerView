package otus.gpb.recyclerview.ui

import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.drawable.Drawable
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.graphics.drawable.toBitmap
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import otus.gpb.recyclerview.data.ChatItem

class ItemTouchHelperCallback(private val getChatItems: ()->List<ChatItem>,
                              private val setChatItems: (List<ChatItem>)->Unit):
    ItemTouchHelper.Callback() {

    private val density = Resources.getSystem().displayMetrics.density

    private var swipeEdgeCornerRadius = 0
    private var edgeColor = 0xFF000000.toInt()
    private var backgroundColor = 0xFF000000.toInt()
    private var archIcon: Drawable? = null
    private var archIconTopOffset = (25 * density)
    private var archIconEndOffset = (20 * density)
    private var bitmap: Bitmap? = null
    private var showArchIcon = false
    private var archIconSize = 0
    private var archIconText = ""

    override fun getMovementFlags(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder
    ): Int {
        return makeMovementFlags(0, ItemTouchHelper.LEFT)
    }

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        return false
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        removeItem(viewHolder.layoutPosition)
    }

    @RequiresApi(Build.VERSION_CODES.P)
    override fun onChildDraw(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {
        if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
            val paintOnSwipe = Paint().apply {
                setColor(backgroundColor)
            }
            val paintEdge = Paint().apply {
                setColor(edgeColor)
                textSize = 30F
            }

            if (dX < 0) {
                val v = viewHolder.itemView
                c.drawRect(
                    v.right.toFloat() + dX - swipeEdgeCornerRadius,
                    v.top.toFloat(),
                    v.right.toFloat(),
                    v.bottom.toFloat(),
                    paintOnSwipe
                )
                c.drawArc(
                    v.right.toFloat() + dX - (swipeEdgeCornerRadius*2),
                    v.top.toFloat(),
                    v.right.toFloat() + dX,
                    v.top.toFloat() + (swipeEdgeCornerRadius*2),
                    0F, -90F, true,
                    paintEdge
                )
                c.drawRect(
                    v.right.toFloat() + dX - swipeEdgeCornerRadius,
                    v.top.toFloat() + swipeEdgeCornerRadius,
                    v.right.toFloat() + dX,
                    v.bottom.toFloat() - swipeEdgeCornerRadius,
                    paintEdge
                )
                c.drawArc(
                    v.right.toFloat() + dX - (swipeEdgeCornerRadius*2),
                    v.bottom.toFloat() - (swipeEdgeCornerRadius*2),
                    v.right.toFloat() + dX,
                    v.bottom.toFloat(),
                    0F, 90F, true,
                    paintEdge
                )
            }
            if (dX <= -200) {
                if (archIcon!= null && archIconSize != 0) {
                    if (!showArchIcon) {
                        showArchIcon = true
                        bitmap = archIcon!!.toBitmap(archIconSize, archIconSize)
                    }

                    if (bitmap != null) {
                        c.drawBitmap(
                            bitmap!!,
                            (viewHolder.itemView.width - (archIconSize + archIconEndOffset)),
                            viewHolder.itemView.y + archIconTopOffset,
                            null
                        )
                        c.drawText(archIconText,
                            (viewHolder.itemView.width - (archIconSize + archIconEndOffset + 10*density)),
                            viewHolder.itemView.y + archIconSize + 40*density,
                            paintEdge)
                    }
                }
            }
            else showArchIcon = false
        }

        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
    }

    private fun removeItem(idx: Int) {
        var list = getChatItems()
        if (idx < 0) return
        list = list.toMutableList().also {
            it.removeAt(idx)
        }.toList()
        setChatItems(list)
    }

    fun setSwipeEdgeParams(radius: Int, edgeColor: Int, backgroundColor: Int) { //20
        swipeEdgeCornerRadius = radius
        this.edgeColor = edgeColor
        this.backgroundColor = backgroundColor
    }

    fun setArchIcon(archIcon: Drawable, topOffset: Int, endOffset: Int, size: Int, text: String) {
        this.archIcon = archIcon
        archIconTopOffset = (topOffset * density)
        archIconEndOffset = (endOffset * density)
        archIconSize = (size * density).toInt()
        archIconText = text
    }

}