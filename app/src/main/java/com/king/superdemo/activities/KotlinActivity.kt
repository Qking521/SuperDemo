package com.king.superdemo.activities

import android.os.Bundle
import android.util.Log
import android.widget.TextView
import com.king.superdemo.R
import com.king.superdemo.kotlin.toast

open class KotlinActivity : BaseActivity() {

    val tips = "hello kotlin"

    private lateinit var textView : TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.i("wq", "onCreate: kotlin")
        setContentView(R.layout.kotlin_layout)
        textView = findViewById<TextView>(R.id.kotlin_text)
        textView.text = tips
        //调用扩展函数
        textView.toast("hello")
    }

   val holder = object {
       var x : Int = 1;
       var y : Int = 2;
   }


}