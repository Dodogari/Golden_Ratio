package com.example.goldenratio

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Gravity
import android.view.View
import android.view.Window
import android.widget.Button

class AddDialog(
    context: Context,
    val clickCocktail: View.OnClickListener,
    val clickHangover: View.OnClickListener
    ) : Dialog(context
){
    private val dialog = Dialog(context)   //부모 액티비티의 context 가 들어감

    fun showDialog() {
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)   //타이틀바 제거
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT)) //테두리 없애기
        dialog.setCanceledOnTouchOutside(true) //다이얼로그의 바깥 화면을 눌렀을 때 다이얼로그가 닫히게
        dialog.setContentView(R.layout.add_dialog)
        dialog.window!!.setGravity(Gravity.BOTTOM)   //다이얼로그 위치
        dialog.show()

        //버튼 동작
        var btCocktail = dialog.findViewById<Button>(R.id.bt_cocktail)
        var btHangover = dialog.findViewById<Button>(R.id.bt_hangover)

        btCocktail.setOnClickListener(clickCocktail)
        btHangover.setOnClickListener(clickHangover)
    }
}
