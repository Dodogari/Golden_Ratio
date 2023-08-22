package com.example.goldenratio.login

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
import com.example.goldenratio.R
import com.example.goldenratio.databinding.ActivityProfileBinding
import com.example.goldenratio.login.models.register.PostRegisterRequest
import com.example.goldenratio.login.models.register.RegisterInterface
import com.example.goldenratio.login.models.register.RegisterResponse
import com.example.goldenratio.login.models.register.RegisterService
import java.io.FileOutputStream
import java.net.URL
import java.text.SimpleDateFormat

var url_profile: URL?= null

class ProfileActivity : AppCompatActivity(), RegisterInterface {
    private lateinit var profileBinding: ActivityProfileBinding

    // storage 권한 처리에 필요한 변수
    val CAMERA = arrayOf(Manifest.permission.CAMERA)
    val STORAGE = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.WRITE_EXTERNAL_STORAGE)
    val CAMERA_CODE = 98
    val STORAGE_CODE = 99

    private lateinit var img_profile : ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        profileBinding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(profileBinding.root)

        img_profile = findViewById(R.id.img_profile)

        profileBinding.btNext.isEnabled = true

        // 로그인 화면으로 이동
        profileBinding.btNext.setOnClickListener{
            // 서버에 값 보냄
            val postRequest = PostRegisterRequest(id = id, password = pw, nickname = nick, url = url_profile)
            RegisterService(this).tryPostRegister(postRequest)
        }

        // 카메라
        profileBinding.btCamera.setOnClickListener{
            CallCamera()
        }

        // 갤러리
        profileBinding.btGallery.setOnClickListener{
            GetAlbum()
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
    fun saveFile(fileName:String, mimeType:String, bitmap: Bitmap):Uri?{

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
                        img_profile.setImageURI(uri)

                        // uri -> url로 변경
                        url_profile = URL("file://"+ absolutelyPath(uri!!))
                        Log.d("tag", "profile url:"+ "{$url_profile}")
                    }
                }
                STORAGE_CODE -> {
                    val uri = data?.data
                    img_profile.setImageURI(uri)

                    // uri -> url로 변경
                    url_profile = URL("file://"+ absolutelyPath(uri!!))
                    Log.d("tag", "profile url:"+ "{$url_profile}")
                }
            }
        }
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

    // 파일명을 날짜 저장
    fun RandomFileName() : String{
        val fileName = SimpleDateFormat("yyyyMMddHHmmss").format(System.currentTimeMillis())
        return fileName
    }

    // 갤러리 취득
    fun GetAlbum(){
        if(checkPermission(STORAGE, STORAGE_CODE)){
            val itt = Intent(Intent.ACTION_PICK)
            itt.type = MediaStore.Images.Media.CONTENT_TYPE
            startActivityForResult(itt, STORAGE_CODE)
        }
    }

    // 서버 연결 성공
    override fun onPostRegisterSuccess(response: RegisterResponse) {
        // 로그인 화면으로 이동
        val intent = Intent(this, LoginActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
        startActivity(intent)

        Toast.makeText(this, "${response.result}", Toast.LENGTH_SHORT).show()
    }

    // 서버 연결 실패
    override fun onPostRegisterFailure(message: String) {
        Log.d("error", "오류 : $message")
    }
}