package com.example.goldenratio.hangover

import android.Manifest
import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.example.goldenratio.MainActivity
import com.example.goldenratio.R
import com.example.goldenratio.databinding.ActivityAddHangoverBinding
import com.example.goldenratio.hangover.models.*
import java.io.FileOutputStream
import java.net.URL
import java.text.SimpleDateFormat


class AddHangoverActivity : AppCompatActivity(), HangInterface {
    private lateinit var addHangoverBinding: ActivityAddHangoverBinding

    // storage 권한 처리에 필요한 변수
    val CAMERA = arrayOf(Manifest.permission.CAMERA)
    val CAMERA_CODE = 98
    val STORAGE_CODE = 99

    private lateinit var img_camera : ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        addHangoverBinding = ActivityAddHangoverBinding.inflate(layoutInflater)
        setContentView(addHangoverBinding.root)

        img_camera = findViewById(R.id.img_hangover)

        // 이미지
        val imgList: ArrayList<Img> = arrayListOf()
        val size = ingredientList.size

        for (i in 0 ..size -1) {
            if (size != 0) {
                imgList.apply {
                    add(Img(url_ingredient))
                }
            }
        }

        if(url_hangover != null){
            Glide.with(this)
                .load(url_hangover.toString()) // 불러올 이미지 url
                .into(img_camera) // 이미지를 넣을 뷰
            Log.d("tag", "url_hangover:"+"{$url_hangover}")
        }

        // 뒤로가기
        addHangoverBinding.btBack.setOnClickListener {
            finish()
        }

        // 메인 화면으로 이동
        addHangoverBinding.btNext.setOnClickListener {

            // 서버에 값 보냄
            val title = title_hangover
            val hangoverMainImageUrl = url_hangover
            val content = addHangoverBinding.etContent.text.toString()
            val category = "category"
            val gradientList: ArrayList<HangRequest> = arrayListOf()

            for (i in 0 ..size -1) {
                if (size != 0) {
                    gradientList.apply{
                        add(
                            HangRequest(
                                Name = ingredientList[i].name,
                                url = ingredientList[i].img
                            )
                        )
                    }
                }
            }

            val PostRegisterRequest = PostHangRequest(title = title, hangoverMainImageUrl = hangoverMainImageUrl, content = content, category = category, gradientList = gradientList)
            HangService(this).tryPostRegister(PostRegisterRequest)
            Toast.makeText(this, "서버 요청", Toast.LENGTH_SHORT).show()
        }

        // ViewPager 여백, 너비 정의
            val pageMarginPx = resources.getDimensionPixelOffset(R.dimen.pageMargin) // 페이지끼리 간격
            val pagerWidth = resources.getDimensionPixelOffset(R.dimen.pageWidth) // 페이지 보이는 정도
            val screenWidth = resources.displayMetrics.widthPixels // 스마트폰의 가로 길이
            val offsetPx = screenWidth - pageMarginPx - pagerWidth

        addHangoverBinding.rvImgPic.setPageTransformer { page, position ->
                page.translationX = position * -offsetPx
            }

        addHangoverBinding.rvImgPic.offscreenPageLimit = 1 // 1개 페이지 미리 로드
        addHangoverBinding.rvImgPic.adapter = ImgAdapter(imgList)
        addHangoverBinding.rvImgPic.orientation = ViewPager2.ORIENTATION_HORIZONTAL // 방향을 가로로

        // 카메라
        addHangoverBinding.btCamera.setOnClickListener{
            CallCamera()
        }
    }
    // 카메라 권한, 저장소 권한
    // 요청 권한
    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when(requestCode){
            CAMERA_CODE -> {
                for (grant in grantResults){
                    if(grant != PackageManager.PERMISSION_GRANTED){
                        Toast.makeText(this, "카메라 권한을 승인해 주세요", Toast.LENGTH_LONG).show()
                    }
                }
            }
            STORAGE_CODE -> {
                for(grant in grantResults){
                    if(grant != PackageManager.PERMISSION_GRANTED){
                        Toast.makeText(this, "저장소 권한을 승인해 주세요", Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
    }

    // 다른 권한등도 확인이 가능하도록
    fun checkPermission(permissions: Array<out String>, type:Int):Boolean{
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            for (permission in permissions){
                if(ContextCompat.checkSelfPermission(this, permission)
                    != PackageManager.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions(this, permissions, type)
                    return false
                }
            }
        }
        return true
    }

    // 카메라 촬영 - 권한 처리
    fun CallCamera(){
        // && checkPermission(STORAGE, STORAGE_CODE)
        if(checkPermission(CAMERA, CAMERA_CODE)){
            val itt = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            startActivityForResult(itt, CAMERA_CODE)
        }
    }

    // 사진 저장
    fun saveFile(fileName:String, mimeType:String, bitmap: Bitmap): Uri?{

        var CV = ContentValues()

        // MediaStore 에 파일명, mimeType 을 지정
        CV.put(MediaStore.Images.Media.DISPLAY_NAME, fileName)
        CV.put(MediaStore.Images.Media.MIME_TYPE, mimeType)

        // 안정성 검사
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q){
            CV.put(MediaStore.Images.Media.IS_PENDING, 1)
        }

        // MediaStore 에 파일을 저장
        val uri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, CV)
        if(uri != null){
            var scriptor = contentResolver.openFileDescriptor(uri, "w")

            val fos = FileOutputStream(scriptor?.fileDescriptor)

            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos)
            fos.close()

            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q){
                CV.clear()
                // IS_PENDING 을 초기화
                CV.put(MediaStore.Images.Media.IS_PENDING, 0)
                contentResolver.update(uri, CV, null, null)
            }
        }
        return uri
    }


    // 결과
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(resultCode == Activity.RESULT_OK){
            when(requestCode){
                CAMERA_CODE -> {
                    if(data?.extras?.get("data") != null){
                        val img = data?.extras?.get("data") as Bitmap
                        val uri = saveFile(RandomFileName(), "image/jpeg", img)
                        img_camera.setImageURI(uri)

                        // uri -> url로 변경
                        url_hangover = URL("file://"+ absolutelyPath(uri!!))
                        Log.d("tag", "title url:"+ "{$url_hangover}")
                    }
                }
                STORAGE_CODE -> {
                    val uri = data?.data
                    img_camera.setImageURI(uri)

                    // uri -> url로 변경
                    url_hangover = URL("file://"+ absolutelyPath(uri!!))
                    Log.d("tag", "title url:"+ "{$url_hangover}")
                }
            }
        }
    }

    // 파일명을 날짜 저장
    fun RandomFileName() : String{
        val fileName = SimpleDateFormat("yyyyMMddHHmmss").format(System.currentTimeMillis())
        return fileName
    }

    // 절대경로 변환
    fun absolutelyPath(path: Uri): String {
        var proj: Array<String> = arrayOf(MediaStore.Images.Media.DATA)
        var c: Cursor = contentResolver.query(path, proj, null, null, null)!!
        var index = c.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
        c.moveToFirst()

        var result = c.getString(index)

        return result
    }

    // 서버 연결 성공
    override fun onPostHangSuccess(response: HangResponse) {
        // 메인 화면으로 이동
        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
        startActivity(intent)

        Toast.makeText(this, "성공", Toast.LENGTH_SHORT).show()
    }

    // 서버 연결 실패
    override fun onPostHangFailure(message: String) {
        Log.d("error", "오류 : $message")
        Toast.makeText(this, "오류 : ${message}", Toast.LENGTH_SHORT).show()
    }
}
