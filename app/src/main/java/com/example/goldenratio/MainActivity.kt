package com.example.goldenratio

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.fragment.app.Fragment
import com.example.goldenratio.databinding.ActivityMainBinding
import com.example.goldenratio.hangover.NewHangoverActivity

class MainActivity : AppCompatActivity() {
    private lateinit var mainBinding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //Show Splash Screen
        installSplashScreen()

        mainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mainBinding.root)

        loadFragment(HomeFragment())

        //바텀 네비게이션 설정
        //백그라운드 및 floating button 및 메뉴 클릭 안되게
        mainBinding.navigationMenu.background = null
        mainBinding.navigationMenu.menu.getItem(1).isEnabled = false
        
        //네비게이션 메뉴 클릭 시
        mainBinding.navigationMenu.setOnItemReselectedListener {
            when(it.itemId) {
                R.id.fragment_home -> loadFragment(HomeFragment())
                R.id.fragment_setting -> loadFragment(SettingFragment())
            }
        }

        // 레시피 추가로 이동
        mainBinding.floatingActionButton.setOnClickListener {
            val intent = Intent(this, NewHangoverActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
            startActivity(intent)
        }
    }

    // 프래그먼트 불러오는 함수
    private fun loadFragment(fragment: Fragment){
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.main_frame, fragment)
        transaction.commit()
    }
}