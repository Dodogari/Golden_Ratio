package com.example.goldenratio.cocktail

import android.Manifest
import android.app.Activity
import android.content.ContentValues
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.goldenratio.*
import com.example.goldenratio.databinding.ActivityAddCocktailBinding
import com.example.goldenratio.hangover.Ingredient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.FileOutputStream
import java.net.URL
import java.text.SimpleDateFormat


val ingredientList: ArrayList<Ingredient> = arrayListOf()
val ingredientNameList: ArrayList<String> = arrayListOf()   // 재료 이름

class AddCocktailActivity : AppCompatActivity() {
    private lateinit var addCocktailBinding: ActivityAddCocktailBinding

    // storage 권한 처리에 필요한 변수
    val CAMERA = arrayOf(Manifest.permission.CAMERA)
    val CAMERA_CODE = 98
    val STORAGE_CODE = 99

    private lateinit var img_camera : ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        addCocktailBinding = ActivityAddCocktailBinding.inflate(layoutInflater)
        setContentView(addCocktailBinding.root)

        img_camera = findViewById(R.id.img_camera)

        // 초기화
        ingredientList.clear()
        ingredientNameList.clear()
        ingredient_name = null
        ratioItemList.clear()

        val boardId = intent.getIntExtra("boardId", -1)

        if(boardId != -1) {
            val editCocktail = RegisterClient.cocktailService.getCocktailItem(boardId.toString())
            editCocktail.enqueue(object : Callback<CocktailData> {
                override fun onResponse(
                    call: Call<CocktailData>,
                    response: Response<CocktailData>
                ) {
                    val cocktailData = response.body()!!
                    
                    //화면 초기화
                    //제목 작성란에 데이터 넣기
                    addCocktailBinding.editText.setText(cocktailData.title)
                    
                    //알콜 라디오 버튼
                    when(cocktailData.alcohol) {
                        0 -> addCocktailBinding.rbt1.isChecked = true
                        1 -> addCocktailBinding.rbt2.isChecked = true
                        2 -> addCocktailBinding.rbt3.isChecked = true
                    }

                    //단맛 라디오 버튼
                    when(cocktailData.sweet) {
                        0 -> addCocktailBinding.rbtTop.isChecked = true
                        1 -> addCocktailBinding.rbtMid.isChecked = true
                        2 -> addCocktailBinding.rbtBottom.isChecked = true
                    }

                    for(i in 0 until cocktailData.gradientList.size) {
                        ingredientList.add(Ingredient(URL(cocktailData.gradientList[i].gradientImageUrl),
                            cocktailData.gradientList[i].gradientName, R.drawable.ic_delete))

                        ingredientNameList.add(cocktailData.gradientList[i].gradientName)
                    }
                }

                override fun onFailure(call: Call<CocktailData>, t: Throwable) {
                    Toast.makeText(this@AddCocktailActivity, "기존 데이터 불러오기를 실패하였습니다.", Toast.LENGTH_LONG).show()
                }

            })
        }

        // 카메라
        addCocktailBinding.btCamera.setOnClickListener{
            CallCamera()
        }

        addCocktailBinding.btNext.setOnClickListener {
            val intent = Intent(this, IngredientActivity2::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
            intent.putExtra("board_id", boardId)

            startActivity(intent)
        }

        //뒤로가기 버튼
        addCocktailBinding.btBack.setOnClickListener {
            //다이얼로그 띄우기
            val oDialog: AlertDialog.Builder = AlertDialog.Builder(this, android.R.style.Theme_DeviceDefault_Light_Dialog)
            oDialog.setMessage("지금까지 작성한 내용은 저장되지 않습니다.")
                .setTitle("뒤로가기")
                .setPositiveButton("아니오", DialogInterface.OnClickListener { _, _ ->
                })
                .setNeutralButton("예",
                    DialogInterface.OnClickListener { _, _ ->
                        //수정 중이면 기존 칵테일 아이템으로
                        if(boardId != -1) {
                            val itemIntent = Intent(this@AddCocktailActivity, CocktailItemActivity::class.java)
                            itemIntent.putExtra("boardId", boardId)
                            startActivity(itemIntent)

                            if(!isFinishing) finish()
                        }
                        else {
                            val homeIntent = Intent(this@AddCocktailActivity, MainActivity::class.java)
                            startActivity(homeIntent)

                            if(!isFinishing) finish()
                        }
                    })
                .show()
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
                    }
                }
                STORAGE_CODE -> {
                    val uri = data?.data
                    img_camera.setImageURI(uri)
                }
            }
        }
    }

    // 파일명을 날짜 저장
    fun RandomFileName() : String{
        val fileName = SimpleDateFormat("yyyyMMddHHmmss").format(System.currentTimeMillis())
        return fileName
    }
}
