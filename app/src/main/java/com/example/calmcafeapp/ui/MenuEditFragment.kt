package com.example.calmcafeapp.ui

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import androidx.fragment.app.activityViewModels
import com.bumptech.glide.Glide
import com.example.calmcafeapp.R
import com.example.calmcafeapp.base.BaseFragment
import com.example.calmcafeapp.data.Menu
import com.example.calmcafeapp.databinding.FragmentMenuEditBinding
import com.example.calmcafeapp.viewmodel.M_SettingViewModel
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.io.FileOutputStream

class MenuEditFragment : BaseFragment<FragmentMenuEditBinding>(R.layout.fragment_menu_edit) {

    private lateinit var menu: Menu
    private val viewModel: M_SettingViewModel by activityViewModels()
    private var selectedImageUri: Uri? = null
    private lateinit var currentMenu: Menu

    companion object {
        private const val ARG_MENU = "menu"
        private const val REQUEST_IMAGE_PICK = 1001


        fun newInstance(menu: Menu): MenuEditFragment {
            val fragment = MenuEditFragment()
            val args = Bundle()
            args.putParcelable(ARG_MENU, menu)
            fragment.arguments = args
            return fragment
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            currentMenu = it.getParcelable(ARG_MENU) // Menu 객체 초기화
                ?: throw IllegalArgumentException("Menu argument is missing")
        }
    }

    override fun initStartView() {
        super.initStartView()

        menu = requireArguments().getParcelable(ARG_MENU)!!

        binding.menuNameEditText.setText(menu.name)
        binding.menuPriceEditText.setText(menu.price.toString())
        Glide.with(this).load(menu.image).into(binding.menuImageView)

        binding.uploadImageButton.setOnClickListener {
            openGallery()
        }

        // Save button
        binding.saveButton.setOnClickListener {
            saveMenu()
        }

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
                binding.menuImageView.setImageURI(it)
            }
        }
    }

    private fun saveMenu() {
        val menuId = currentMenu.id
        val price = binding.menuPriceEditText.text.toString().toIntOrNull()

        var menuImage: MultipartBody.Part? = null
        selectedImageUri?.let { uri ->
            val file = createTempFileFromUri(uri)
            file?.let {
                val requestFile = it.asRequestBody("image/*".toMediaTypeOrNull())
                menuImage = MultipartBody.Part.createFormData("menuImage", it.name, requestFile)
            }
        }

        viewModel.updateMenu(menuId.toLong(), price, menuImage)

        // Observe ViewModel for success response
        viewModel.updateMenuResult.observe(viewLifecycleOwner) { isSuccess ->
            if (isSuccess) {
                Log.d("MenuEditFragment", "Menu updated successfully!")
                parentFragmentManager.popBackStack() // 이전 화면으로 이동
            } else {
                Log.e("MenuEditFragment", "Failed to update menu.")
            }
        }
    }


    private fun createTempFileFromUri(uri: Uri): File? {
        try {
            // 이미지 읽기
            val inputStream = requireContext().contentResolver.openInputStream(uri)
            val originalBitmap = BitmapFactory.decodeStream(inputStream)
            inputStream?.close()

            // 리사이즈 설정 (너비와 높이를 원하는 크기로 조정)
            val maxWidth = 800 // 최대 너비
            val maxHeight = 800 // 최대 높이
            val scaledBitmap = Bitmap.createScaledBitmap(originalBitmap, maxWidth, maxHeight, true)

            // 압축 및 파일 저장
            val tempFile = File.createTempFile("temp_image", ".jpg", requireContext().cacheDir)
            val outputStream = FileOutputStream(tempFile)
            scaledBitmap.compress(Bitmap.CompressFormat.JPEG, 80, outputStream) // JPEG로 압축 (품질 80%)
            outputStream.flush()
            outputStream.close()

            return tempFile
        } catch (e: Exception) {
            Log.e("MenuEditFragment", "Error resizing and creating temp file from URI: ${e.message}")
            return null
        }
    }

}
