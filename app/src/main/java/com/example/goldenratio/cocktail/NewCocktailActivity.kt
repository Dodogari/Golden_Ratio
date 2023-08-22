package com.example.goldenratio.cocktail

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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.example.goldenratio.MainActivity
import com.example.goldenratio.R
import com.example.goldenratio.cocktail.models.*
import com.example.goldenratio.databinding.ActivityNewCocktailBinding
import com.example.goldenratio.hangover.Img
import com.example.goldenratio.img.ImgInterface
import com.example.goldenratio.img.ImgResponse
import com.example.goldenratio.img.ImgService
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat

class NewCocktailActivity : AppCompatActivity(), CocktailInterface, ImgInterface {
    private lateinit var newCocktailBinding: ActivityNewCocktailBinding

    val tvList: ArrayList<TvRatio> = arrayListOf()
    val intList: ArrayList<TvRatio> = arrayListOf()


    // storage 권한 처리에 필요한 변수
    val CAMERA = arrayOf(Manifest.permission.CAMERA)
    val CAMERA_CODE = 98
    val STORAGE_CODE = 99

    private lateinit var img_cocktail : ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        newCocktailBinding = ActivityNewCocktailBinding.inflate(layoutInflater)
        setContentView(newCocktailBinding.root)

        img_cocktail = findViewById(R.id.img_cocktail)

        val boardId = intent.getIntExtra("boardId", -1)

        // 이미지
        val imgList: ArrayList<Img> = arrayListOf()
        val size = ingredientList2.size

        for (i in 0 ..size -1) {
            if (size != 0) {
                imgList.apply {
                    add(Img(url_ingredient))
                }
            }
        }

        if(url_cocktail != null){
            Glide.with(this)
                .load(url_cocktail.toString()) // 불러올 이미지 url
                .into(img_cocktail) // 이미지를 넣을 뷰
            Log.d("tag", "url_cocktail:"+"{$url_cocktail}")
        }

        // 뒤로가기
        newCocktailBinding.btBack.setOnClickListener {
            finish()
        }

        // 비율 이름
        for (i in 0 ..ratioNameList.size -1) {
            if (ratioNameList.size != 0) {
                tvList.apply {
                    add(
                        TvRatio(
                            name = ratioNameList[i], color = ratioColorList[i]
                        )
                    )
                }
            }
        }
        newCocktailBinding.rvRatioTv.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        newCocktailBinding.rvRatioTv.setHasFixedSize(true)
        newCocktailBinding.rvRatioTv.adapter = TvAdapter(tvList = tvList)


        // 비율 숫자
        for (i in 0 ..ratioNameList.size -1) {
            if (ratioNameList.size != 0) {
                intList.apply {
                    add(
                        TvRatio(
                            name = ratioIntList[i].toString(), color = ratioColorList[i]
                        )
                    )
                }
            }
        }
        newCocktailBinding.rvRatioInt.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        newCocktailBinding.rvRatioInt.setHasFixedSize(true)
        newCocktailBinding.rvRatioInt.adapter = IntAdapter(intList = intList)

        // 메인 화면으로 이동
        newCocktailBinding.btNext.setOnClickListener {
            // 서버에 값 보냄
            val title = title_cocktail
            val cocktailMainImageUrl = url_cocktail
            val content = newCocktailBinding.etContent.text.toString()
            val category = "칵테일"
            val gradientList: ArrayList<CocktailRequest> = arrayListOf()
            val balanceList: ArrayList<BalanceListRequest> = arrayListOf()

            for (i in 0 ..size -1) {
                if (size != 0) {
                    gradientList.apply{
                        add(
                            CocktailRequest(
                                name = ingredientList2[i].name,
                                url = ingredientList2[i].img
                            )
                        )
                    }
                }
            }

            for (i in 0 ..size -1) {
                if (size != 0) {
                    balanceList.apply{
                        add(
                            BalanceListRequest(
                                balanceName = ratioNameList[i],
                                balanceNum = ratioIntList[i]
                            )
                        )
                    }
                }
            }

            val PostCocktailRequest = PostCocktailRequest(
                title = title, cocktailMainImageUrl = cocktailMainImageUrl,
                content = content, category = category, sweet = sweet, alcohol = alcohol, gradientList = gradientList, balanceList = balanceList)

            if(boardId != -1)
                CocktailService(this).tryEditCocktail(boardId.toString(), PostCocktailRequest)
            else
                CocktailService(this).tryPostCocktail(PostCocktailRequest)

            Toast.makeText(this, "서버 요청", Toast.LENGTH_SHORT).show()
        }

        // ViewPager 여백, 너비 정의
        val pageMarginPx = resources.getDimensionPixelOffset(R.dimen.pageMargin) // 페이지끼리 간격
        val pagerWidth = resources.getDimensionPixelOffset(R.dimen.pageWidth) // 페이지 보이는 정도
        val screenWidth = resources.displayMetrics.widthPixels // 스마트폰의 가로 길이
        val offsetPx = screenWidth - pageMarginPx - pagerWidth

        newCocktailBinding.rvImgPic.setPageTransformer { page, position ->
            page.translationX = position * -offsetPx
        }

        newCocktailBinding.rvImgPic.offscreenPageLimit = 1 // 1개 페이지 미리 로드
        newCocktailBinding.rvImgPic.adapter = ImgAdapter2(imgList)
        newCocktailBinding.rvImgPic.orientation = ViewPager2.ORIENTATION_HORIZONTAL // 방향을 가로로

        // 카메라
        newCocktailBinding.btCamera.setOnClickListener{
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
                        val img = data.extras?.get("data") as Bitmap
                        val uri = saveFile(RandomFileName(), "image/jpeg", img)
                        img_cocktail.setImageURI(uri)

                        // 아미지 url로 변경
                        val file = File(absolutelyPath(uri!!))
                        ImgService(this).tryPostImg(file)
                    }
                }
                STORAGE_CODE -> {
                    val uri = data?.data
                    img_cocktail.setImageURI(uri)

                    // 아미지 url로 변경
                    val file = File(absolutelyPath(uri!!))
                    ImgService(this).tryPostImg(file)
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
    override fun onPostImgSuccess(response: ImgResponse) {
        url_cocktail = response.result
    }

    // 서버 연결 실패
    override fun onPostImgFailure(message: String) {
        Log.d("error", "오류 : $message")
    }

    // 서버 연결 성공
    override fun onPostCocktailSuccess(response: CocktailResponse) {
        // 메인 화면으로 이동
        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
        startActivity(intent)

        Toast.makeText(this, response.result, Toast.LENGTH_SHORT).show()
    }

    // 서버 연결 실패
    override fun onPostCocktailFailure(message: String) {
        Log.d("error", "오류 : $message")
        Toast.makeText(this, "오류 : ${message}", Toast.LENGTH_SHORT).show()
    }
}
