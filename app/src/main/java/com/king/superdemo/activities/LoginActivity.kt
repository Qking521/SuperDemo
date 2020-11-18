package com.king.superdemo.activities

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Toast
import com.king.superdemo.R
import com.king.superdemo.custom.PowerEditText
import org.w3c.dom.Text

class LoginActivity : BaseActivity() {

    lateinit var userNameEditor : PowerEditText;
    lateinit var userPasswdEditor : PowerEditText;


    val userNameTextWatcher : TextWatcher = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
            Log.i("wq", ": afterTextChanged s= ${s.toString()}")
            userNameEditor.checkLengthLimit(s.toString(), { operate() })
            userNameEditor.checkUppercase(s.toString())
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            Log.i("wq", ": beforeTextChanged");
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        }
    }



    val userPasswdWatcher : TextWatcher = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        initView()
        initEvent()
    }

    private fun initView() {
        userNameEditor = findViewById<PowerEditText>(R.id.user_name)
        userPasswdEditor = findViewById<PowerEditText>(R.id.user_passwd)
    }

    private fun initEvent() {
        userNameEditor.addTextChangedListener(userNameTextWatcher)
        userPasswdEditor.addTextChangedListener(userPasswdWatcher)
    }

    fun operate() {
        Toast.makeText(this, "operate", Toast.LENGTH_SHORT).show()
    }

}