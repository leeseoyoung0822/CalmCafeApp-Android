package com.example.calmcafeapp.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.NumberPicker
import com.example.calmcafeapp.R
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
class DiscountBottomSheet(
    private val initialDiscount: Int,
    private val onDiscountSelected: (Int) -> Unit
) : BottomSheetDialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.bottom_sheet_discount, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val numberPicker: NumberPicker = view.findViewById(R.id.numberPicker)
        val saveButton: Button = view.findViewById(R.id.btnSave)

        // 5단위 값 설정
        val values = (0..100 step 5).toList().toIntArray()
        val displayedValues = values.map { "${it.toString()}%" }.toTypedArray()

        // NumberPicker 설정
        numberPicker.minValue = 0
        numberPicker.maxValue = displayedValues.size - 1
        numberPicker.value = values.indexOf(initialDiscount)
        numberPicker.displayedValues = displayedValues
        numberPicker.wrapSelectorWheel = false

        // 저장 버튼 클릭 이벤트
        saveButton.setOnClickListener {
            val selectedIndex = numberPicker.value
            val selectedDiscount = values[selectedIndex]
            onDiscountSelected(selectedDiscount)
            dismiss()
        }
    }
}

