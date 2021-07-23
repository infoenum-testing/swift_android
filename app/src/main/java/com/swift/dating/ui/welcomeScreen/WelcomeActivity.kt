package com.swift.dating.ui.welcomeScreen

import android.content.Intent
import android.os.Bundle
import com.swift.dating.R
import com.swift.dating.ui.base.BaseActivity
import com.swift.dating.ui.createAccountScreen.CreateAccountActivity
import kotlinx.android.synthetic.main.activity_welcome.*

class WelcomeActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)
        
        btn_continue.setOnClickListener {
            val i: Intent? = Intent(mActivity, CreateAccountActivity::class.java).putExtra("parseCount", 1)
            startActivity(i)
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        }
    }
}
