package com.example.calmcafeapp.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.NumberPicker
import com.example.calmcafeapp.R
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class TimeRangePickerBottomSheet(
    private val initialStartHour: Int,
    private val initialStartMinute: Int,
    private val initialEndHour: Int,
    private val initialEndMinute: Int,
    private val onTimeRangeSelected: (startHour: Int, startMinute: Int, endHour: Int, endMinute: Int) -> Unit
) : BottomSheetDialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.bottom_sheet_time_range_picker, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val startHourPicker: NumberPicker = view.findViewById(R.id.startHourPicker)
        val startMinutePicker: NumberPicker = view.findViewById(R.id.startMinutePicker)
        val endHourPicker: NumberPicker = view.findViewById(R.id.endHourPicker)
        val endMinutePicker: NumberPicker = view.findViewById(R.id.endMinutePicker)
        val saveButton: Button = view.findViewById(R.id.btnSave)

        // NumberPicker 설정
        startHourPicker.apply {
            minValue = 0
            maxValue = 23
            value = initialStartHour
        }

        startMinutePicker.apply {
            minValue = 0
            maxValue = 59
            value = initialStartMinute
        }

        endHourPicker.apply {
            minValue = 0
            maxValue = 23
            value = initialEndHour
        }

        endMinutePicker.apply {
            minValue = 0
            maxValue = 59
            value = initialEndMinute
        }

        // 저장 버튼 클릭 이벤트
        saveButton.setOnClickListener {
            val selectedStartHour = startHourPicker.value
            val selectedStartMinute = startMinutePicker.value
            val selectedEndHour = endHourPicker.value
            val selectedEndMinute = endMinutePicker.value

            onTimeRangeSelected(
                selectedStartHour,
                selectedStartMinute,
                selectedEndHour,
                selectedEndMinute
            )
            dismiss()
        }
    }
}
