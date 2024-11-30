import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.view.View
import androidx.annotation.ColorInt
import androidx.recyclerview.widget.RecyclerView

class GridSpacingWithDividerDecoration(
    private val spanCount: Int,
    private val spacing: Int,
    private val dividerHeight: Int,
    @ColorInt private val dividerColor: Int
) : RecyclerView.ItemDecoration() {

    private val paint = Paint().apply {
        color = dividerColor
        style = Paint.Style.FILL
    }

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        val position = parent.getChildAdapterPosition(view)
        val column = position % spanCount

        outRect.left = column * spacing / spanCount
        outRect.right = spacing - (column + 1) * spacing / spanCount

        if (position >= spanCount) {
            outRect.top = spacing
        }
    }

    override fun onDrawOver(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        val childCount = parent.childCount
        val spanGroupCount = (childCount + spanCount - 1) / spanCount // 총 행 개수 계산

        for (i in 0 until spanGroupCount - 1) { // 마지막 행 제외
            val firstChildIndex = i * spanCount
            val firstChild = parent.getChildAt(firstChildIndex)
            val lastChild = parent.getChildAt(firstChildIndex + spanCount - 1)

            if (firstChild != null && lastChild != null) {
                val dividerTop = lastChild.bottom
                val dividerBottom = dividerTop + dividerHeight

                c.drawRect(
                    firstChild.left.toFloat(),
                    dividerTop.toFloat(),
                    lastChild.right.toFloat(),
                    dividerBottom.toFloat(),
                    paint
                )
            }
        }
    }
}
