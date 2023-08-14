package com.example.goldenratio.hangover

import android.Manifest
import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.goldenratio.MainActivity
import com.example.goldenratio.cocktail.IngredientFragment
import com.example.goldenratio.R
import com.example.goldenratio.databinding.ActivityNewHangoverBinding
import com.example.goldenratio.hangover.models.HangInterface
import com.example.goldenratio.hangover.models.HangResponse
import com.example.goldenratio.hangover.models.PostHangRequest
import com.example.goldenratio.login.id
import com.example.goldenratio.login.models.LoginResponse
import com.example.goldenratio.login.models.LoginService
import com.example.goldenratio.login.models.PostLoginRequest
import com.example.goldenratio.search.SearchFragment
import java.io.FileOutputStream
import java.text.SimpleDateFormat

var img_hangover: Uri? = null

class  NewHangoverActivity : AppCompatActivity(), HangInterface {

    private lateinit var NewHangoverBinding : ActivityNewHangoverBinding

    // storage 권한 처리에 필요한 변수
    val CAMERA = arrayOf(Manifest.permission.CAMERA)
    val CAMERA_CODE = 98
    val STORAGE_CODE = 99


    private lateinit var img_camera : ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        NewHangoverBinding = ActivityNewHangoverBinding.inflate(layoutInflater)
        setContentView(NewHangoverBinding.root)

        img_camera = findViewById(R.id.img_camera)

        NewHangoverBinding.btNext.isEnabled = true

        // 이전 화면으로 이동
        NewHangoverBinding.btBack.setOnClickListener{
            finish()
        }

        // 다음 화면으로 이동
        NewHangoverBinding.btNext.setOnClickListener{
            // 서버에 값 보냄
//            val title = ""
//            val hangoverMainImageUrl = ""
//            val content = ""
//            val category = ""
//
//            val postRequest = PostHangRequest(title = title, hangoverMainImageUrl = hangoverMainImageUrl, content = content, category = category, gradientList = gradientList)
//
//            LoginService(this).tryPostLogin(postRequest)
            Toast.makeText(this, "서버 요청", Toast.LENGTH_SHORT).show()
        }

        // 카메라
        NewHangoverBinding.btCamera.setOnClickListener{
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
                        img_hangover = uri
                    }
                }
                STORAGE_CODE -> {
                    val uri = data?.data
                    img_camera.setImageURI(uri)
                    img_hangover = uri
                }
            }
        }
    }

    // 파일명을 날짜 저장
    fun RandomFileName() : String{
        val fileName = SimpleDateFormat("yyyyMMddHHmmss").format(System.currentTimeMillis())
        return fileName
    }

    // 화면 이동
    fun changeFragment(index: Int){
        when(index) {
            // 재료 추가 페이지
            1 -> {
                supportFragmentManager
                    .beginTransaction()
                    .replace(NewHangoverBinding.hangoverFrame.id, IngredientFragment())
                    .commitAllowingStateLoss()
            }

            // 재료 검색 페이지
            2 -> {
                supportFragmentManager
                    .beginTransaction()
                    .replace(NewHangoverBinding.hangoverFrame.id, SearchFragment())
                    .commitAllowingStateLoss()
            }
        }
    }

    // 서버 연결 성공
    override fun onPostHangSuccess(response: HangResponse) {
        // 메인 화면으로 이동
        val intent = Intent(this, IngredientActivity::class.java)
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