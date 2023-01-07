package com.example.trelloclone.activity


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import com.example.trelloclone.R
import kotlinx.android.synthetic.main.activity_intro.*

class IntroActivity : BaseActivity() {
//    lateinit var binding: ActivityIntroBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        binding = ActivityIntroBinding.inflate(layoutInflater)
        setContentView(R.layout.activity_intro)

        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        btn_sign_in_intro.setOnClickListener {
            startActivity(Intent(this, SignInActivity::class.java))
        }
        btn_sign_up_intro.setOnClickListener {
            startActivity(Intent(this, SignUpActivity::class.java))
        }
    }
}