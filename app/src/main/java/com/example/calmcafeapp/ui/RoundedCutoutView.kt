import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View

class RoundedCutoutView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.BLUE // 원하는 색상
        style = Paint.Style.FILL
    }

    private val cutoutPath = Path()

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        // 전체 사각형 그리기
        val rect = RectF(0f, 0f, width.toFloat(), height.toFloat())
        canvas.drawRect(rect, paint)

        // 하단 둥근 컷아웃 만들기
        val cutoutRadius = width / 2f // 반지름은 뷰 너비의 절반
        val cutoutCenterX = width / 2f // 가운데 정렬
        val cutoutCenterY = height.toFloat()

        cutoutPath.reset()
        cutoutPath.addCircle(cutoutCenterX, cutoutCenterY, cutoutRadius, Path.Direction.CW)

        // 컷아웃 영역 클리핑
        paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR)
        canvas.drawPath(cutoutPath, paint)

        // Xfermode 초기화
        paint.xfermode = null
    }
}
