package com.example.goldenratio

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Gravity
import android.view.View
import android.view.Window
import android.widget.Button

class CameraDialog(
    context: Context,
    val clickCamera: View.OnClickListener,
    val clickGallery: View.OnClickListener
    ) : Dialog(context
){
    private val dialog = Dialog(context)   //부모 액티비티의 context 가 들어감

    fun showDialog() {
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)   //타이틀바 제거
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT)) //테두리 없애기
        dialog.setCanceledOnTouchOutside(true) //다이얼로그의 바깥 화면을 눌렀을 때 다이얼로그가 닫히게
        dialog.setContentView(R.layout.camera_dialog)
        dialog.window!!.setGravity(Gravity.CENTER)   //다이얼로그 위치
        dialog.show()

        //버튼 동작
        var btCamera = dialog.findViewById<Button>(R.id.bt_camera)
        var btGallery = dialog.findViewById<Button>(R.id.bt_gallery)

        btCamera.setOnClickListener(clickCamera)
        btGallery.setOnClickListener(clickGallery)
    }
}
