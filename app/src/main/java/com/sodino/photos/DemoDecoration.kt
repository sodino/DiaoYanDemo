package com.sodino.photos

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class DemoDecoration : RecyclerView.ItemDecoration() {
    val halfDivide = 5
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val adapter = parent.adapter ?: return
        val allCount = adapter.itemCount
        val layParams = view.layoutParams
        if (layParams !is RecyclerView.LayoutParams) {
            return
        }
        val position = layParams.viewAdapterPosition

        outRect.bottom = 0
        outRect.top = 0
        outRect.left = if (position == 0) { halfDivide * 2} else { halfDivide }
        outRect.right = if (position == allCount -1) { halfDivide * 2 } else { halfDivide }
    }
}