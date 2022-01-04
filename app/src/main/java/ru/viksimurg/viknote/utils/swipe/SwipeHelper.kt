package ru.viksimurg.viknote.utils.swipe

import android.content.Context
import android.graphics.*
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import ru.viksimurg.viknote.R

abstract class SwipeHelper(
    private val context: Context
): ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT or ItemTouchHelper.LEFT) {

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        return false
    }

    override fun onChildDraw(
        canvas: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {
        RecyclerViewSwipeDecorator.Builder(canvas, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
            .addSwipeLeftBackgroundColor(ContextCompat.getColor(context, R.color.recycler_view_item_swipe_left_background))
            .addSwipeLeftActionIcon(R.drawable.ic_baseline_edit_note_24)
            .setSwipeLeftActionIconTint(Color.WHITE)
            .addSwipeRightBackgroundColor(ContextCompat.getColor(context, R.color.recycler_view_item_swipe_right_background))
            .addSwipeRightActionIcon(R.drawable.ic_delete_white_24dp)
            .addSwipeRightLabel(context.getString(R.string.action_delete))
            .setSwipeRightLabelColor(Color.WHITE)
            .addSwipeLeftLabel(context.getString(R.string.action_edit))
            .setSwipeLeftLabelColor(Color.WHITE)
            .create()
            .decorate()
        super.onChildDraw(canvas, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
    }
}