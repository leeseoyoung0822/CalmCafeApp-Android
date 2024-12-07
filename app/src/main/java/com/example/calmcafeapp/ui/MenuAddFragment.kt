package com.example.calmcafeapp.ui

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import com.example.calmcafeapp.R
import com.example.calmcafeapp.base.BaseFragment
import com.example.calmcafeapp.databinding.FragmentMenuAddBinding
import com.example.calmcafeapp.viewmodel.M_SettingViewModel
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.io.FileOutputStream

class MenuAddFragment : BaseFragment<FragmentMenuAddBinding>(R.layout.fragment_menu_add) {

    private val viewModel: M_SettingViewModel by activityViewModels()
    private var selectedImageUri: Uri? = null

    companion object {
        private const val REQUEST_IMAGE_PICK = 1001
    }

    override fun initStartView() {
        super.initStartView()

        // 이미지 업로드 버튼 클릭 이벤트
        binding.uploadImageButton.setOnClickListener {
            openGallery()
        }

        // 저장 버튼 클릭 이벤트
        binding.saveButton.setOnClickListener {
            saveMenu()
            Toast.makeText(requireContext(), "저장 완료!", Toast.LENGTH_SHORT).show()
            parentFragmentManager.popBackStack()
        }

        // 뒤로 가기 버튼 클릭 이벤트
        binding.btnBack.setOnClickListener {
            parentFragmentManager.popBackStack()
        }
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, REQUEST_IMAGE_PICK)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_IMAGE_PICK && resultCode == Activity.RESULT_OK) {
            selectedImageUri = data?.data
            selectedImageUri?.let {
                binding.menuImageView.setImageURI(it) // 이미지 미리보기
            }
        }
    }

    private fun saveMenu() {
        val menuName = binding.menuNameEditText.text.toString()
        val price = binding.menuPriceEditText.text.toString().toIntOrNull()

        if (menuName.isBlank() || price == null) {
            Log.e("MenuAddFragment", "Menu name or price is invalid")
            return
        }

        var menuImage: MultipartBody.Part? = null
        selectedImageUri?.let { uri ->
            val file = createTempFileFromUri(uri)
            file?.let {
                val requestFile = it.asRequestBody("image/*".toMediaTypeOrNull())
                menuImage = MultipartBody.Part.createFormData("menuImage", it.name, requestFile)
            }
        }


        viewModel.registerMenu(menuName, price, menuImage)
    }

    private fun createTempFileFromUri(uri: Uri): File? {
        return try {
            // 이미지 읽기
            val inputStream = requireContext().contentResolver.openInputStream(uri)
            val originalBitmap = BitmapFactory.decodeStream(inputStream)
            inputStream?.close()

            // 리사이즈 설정
            val maxWidth = 800
            val maxHeight = 800
            val scaledBitmap = Bitmap.createScaledBitmap(originalBitmap, maxWidth, maxHeight, true)

            // 압축 및 파일 저장
            val tempFile = File.createTempFile("temp_image", ".jpg", requireContext().cacheDir)
            val outputStream = FileOutputStream(tempFile)
            scaledBitmap.compress(Bitmap.CompressFormat.JPEG, 80, outputStream) // JPEG로 압축
            outputStream.flush()
            outputStream.close()

            tempFile
        } catch (e: Exception) {
            Log.e("MenuAddFragment", "Error resizing and creating temp file from URI: ${e.message}")
            null
        }
    }
}
