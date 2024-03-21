package com.softsquared.template.kotlin.src.main.goods

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.recyclerview.widget.GridLayoutManager
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.softsquared.template.kotlin.config.BaseActivity
import com.softsquared.template.kotlin.databinding.ActivityGallaryBinding
import com.softsquared.template.kotlin.util.ETC
import com.softsquared.template.kotlin.util.TAG

class GallaryActivity : BaseActivity<ActivityGallaryBinding>(ActivityGallaryBinding::inflate) {

    /** 파이어베이스 : 갤러리 저장 */
    private val storage by lazy {
        Firebase.storage
    }

    data class ImgGridData (
        var imgGrid : String
    )

//    private lateinit var mSuccessHandler: Handler
//    private lateinit var mErrorHandler: ErrorHandler
    private lateinit var imageURI : Uri
    private val datas = mutableListOf<ImgGridData>()
    lateinit var imgGridRVAdapter: ImgGridRVAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.btnGoBack.setOnClickListener { finish() }

        datas.removeAll(datas)

        initSubmitItemButton()
        initRecycler()
        initAddImageButton()
    }

    private fun initAddImageButton() {
        binding.btnAddPhoto.setOnClickListener {
            Log.d(TAG,"사진 추가 -> 권한 확인")
            when {
                ContextCompat.checkSelfPermission(this,
                    Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                -> {
                    // 권한이 존재하는 경우
                    getImageFromAlbum()
                    Log.d(TAG,"권한 확인 -> 갤러리 이동")
                }
                shouldShowRequestPermissionRationale(Manifest.permission.READ_EXTERNAL_STORAGE) -> {
                    // 권한이 거부 되어 있는 경우
                    showPermissionContextPopup()
                    Log.d(TAG,"권한 확인 -> 권한 실패")
                }
                else -> {
                    // 처음 권한을 시도했을 때 띄움
                    requestPermissions(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                        ETC.REQUEST_FIRST
                    )
                }
            }
        }
        Log.e(TAG, "initAddImageButton: 끝", )
    }

    private fun showPermissionContextPopup() {
    }

    private fun getImageFromAlbum() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        startActivityForResult(intent, ETC.REQUEST_GET_IMAGE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode != Activity.RESULT_OK) {
            showCustomToast("잘못된 접근입니다")
            return
        }
        when(requestCode){
            ETC.REQUEST_GET_IMAGE -> {
                val selectedImageURI : Uri? = data?.data
                if( selectedImageURI != null ) {
                    imageURI = selectedImageURI
                    addTask("$imageURI")
                    binding.tvTextCount.text = imgGridRVAdapter.datas.size.toString()
                }else {
                    showCustomToast("이미지를 가져오지 못했습니다")
                }
            }
            else -> {
                showCustomToast("이미지를 가져오지 못했습니다")
            }
        }
        Log.e(TAG, "onActivityResult: 끝", )
    }

    private fun initSubmitItemButton() {
        binding.btnCheck.setOnClickListener {
            // 선택된 이미지 uri가 존재할 경우
            // 이미지를 스토리지에 저장시킴
            // 스토리지에 저장 시킨 후 다시 값을 받아와 모델에 uri값을 넣어 만들어 줘야함
            showLoadingDialog(this)
            var imageuri = arrayListOf<String>()
            if (imageURI != null) {
                uploadPhoto(
                    imageURI,
                    mSuccessHandler = { uri ->
                        Log.i(TAG, "사진 업로드 성공")
                        showCustomToast("사진 업로드 성공")

                        val count = prefs.getInt("IMAGECOUNT", 100)+1

                        if (count == datas.size) {
                            imageuri.add(uri)
                            prefs.setStringArray("IMAGEARRAY", imageuri)
                            Log.i(TAG, "완성된 배열 : $imageuri")
                            dismissLoadingDialog()
                            finish()
                        } else {
                            imageuri.add(uri)
                        }
                    },
                    mErrorHandler = {
                        showCustomToast("사진 업로드에 실패")
                        dismissLoadingDialog()
                    })
            } else {
                // 이미지 uri가 존재하지 않는 경우
                Log.e(TAG, "이미지 정보(URI)가 없어요!")
                dismissLoadingDialog()
            }
        }
        Log.e(TAG, "initSubmitItemButton: 끝", )
    }

    private fun uploadPhoto(
        imageURI: Uri,
        mSuccessHandler: (String) -> Unit,
        mErrorHandler: () -> Unit,
    ) {
        for (i in 0 until datas.size) {
            val fileName = "${System.currentTimeMillis()}.png"
            storage.reference.child("article/photo").child(fileName)
                .putFile(datas[i].imgGrid.toUri())
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        // 파일 업로드에 성공했기 때문에 파일을 다시 받아 오도록 해야함
                        storage.reference.child("article/photo").child(fileName).downloadUrl
                            .addOnSuccessListener { uri ->
                                mSuccessHandler(uri.toString())
                            }.addOnFailureListener {
                                mErrorHandler()
                            }
                        prefs.setInt("IMAGECOUNT", i)
                    } else {
                        mErrorHandler()
                    }
                }
            /** 사진 업로드 되면 */
            prefs.setInt("URIMAXSIZE", datas.size)
            prefs.setString("URIFAKEIMAGE", "${datas[i].imgGrid.toUri()}")
            Log.e(TAG, "$i 번째 갤러리 이미지 URI : ${datas[i].imgGrid.toUri()}")
        }
    }

    /** 배열 추가 */
    private fun initRecycler() {
        imgGridRVAdapter = ImgGridRVAdapter(this)
        binding.rvAddPhoto.adapter = imgGridRVAdapter

        datas.apply {
        }

        val gridLayoutManager = GridLayoutManager(applicationContext, 3)
        binding.rvAddPhoto.layoutManager = gridLayoutManager
        imgGridRVAdapter.datas = datas
        imgGridRVAdapter.notifyDataSetChanged() // 검색결과 배열 갱신
    }

    /** 외부에서 배열에 추가할 수 있게 사용하는 곳 */
    fun addTask(AddImage: String) {
        //editText의 결과를 가져온다.
        val result = ImgGridData(AddImage)
        datas.add(result)

        Log.e(TAG, "$datas")

        binding.rvAddPhoto.adapter?.notifyDataSetChanged() // 검색결과 배열 갱신
    }

    override fun onDestroy() {
        super.onDestroy()

        var imageuri = arrayListOf<String>()
        imageuri.removeAll(imageuri)
        prefs.setStringArray("IMAGEARRAY", imageuri)
        datas.removeAll(datas)
    }
}