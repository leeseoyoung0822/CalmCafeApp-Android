package com.example.calmcafeapp.ui

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat
import com.example.calmcafeapp.R


class CircularProgressView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyle: Int = 0
) : View(context, attrs, defStyle) {

    private val paintBackground = Paint().apply {
        color = ContextCompat.getColor(context, R.color.lightgray)
        style = Paint.Style.STROKE
        strokeWidth = 20f
        isAntiAlias = true
    }

    private val paintForeground = Paint().apply {
        color = ContextCompat.getColor(context, R.color.green)
        style = Paint.Style.STROKE
        strokeWidth = 20f
        isAntiAlias = true
    }

    // 외곽선 페인트 (흰색 테두리 추가)
    private val paintBorder = Paint().apply {
        color = Color.WHITE // 테두리 흰색
        style = Paint.Style.STROKE
        strokeWidth = 5f // 테두리 두께를 줄여서 원이 잘 보이도록
        isAntiAlias = true
    }

    // 텍스트 표시를 위한 페인트
    private val paintText = Paint().apply {
        color = Color.BLACK
        textSize = 40f // 텍스트 크기 조정
        textAlign = Paint.Align.CENTER
        isAntiAlias = true
    }

    private var percentage: Float = 0f // 혼잡도를 0~100 사이의 값으로 설정
    private var crowdLevel: String = "한산" // 기본 텍스트 값

    // 퍼센티지 및 텍스트 설정 메서드
    fun setPercentage(percentage: Float, crowdLevel: String) {
        this.percentage = percentage
        this.crowdLevel = crowdLevel
        invalidate() // 뷰를 다시 그리도록 요청
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val radius = (width.coerceAtMost(height)) / 2f - paintForeground.strokeWidth / 2f
        val centerX = width / 2f
        val centerY = height / 2f

        // 배경 원
        canvas.drawCircle(centerX, centerY, radius, paintBackground)

        // 채워진 부분 (호)
        val sweepAngle = (percentage / 100f) * 360f
        canvas.drawArc(
            centerX - radius, centerY - radius,
            centerX + radius, centerY + radius,
            -90f, sweepAngle, false, paintForeground
        )

        // 외곽선 (흰색 테두리)
        canvas.drawCircle(centerX, centerY, radius, paintBorder)

        // 중앙 텍스트
        val textY = centerY - (paintText.descent() + paintText.ascent()) / 2 // 텍스트를 중앙에 맞추기 위한 Y 좌표 계산
        canvas.drawText(crowdLevel, centerX, textY, paintText) // 중앙에 텍스트 그리기
    }
}
