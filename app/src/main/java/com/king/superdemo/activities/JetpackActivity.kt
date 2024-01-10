package com.king.superdemo.activities

import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.activity.viewModels
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.king.superdemo.R
import com.king.superdemo.utils.PackageUtil
import com.king.superdemo.utils.ShellUtil
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class JetpackActivity : BaseActivity() {

//    private lateinit var binding: JetpackActivityBinding

    lateinit var viewModelButton:Button
    val myViewModel : MyViewModel by viewModels()
    val name: String by lazy { "000" }
    var mainScope = MainScope()





    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.v("wq", "logThread1: nam")

        PackageUtil.install(this,"storage/emulated/0/QuickpicGallery.apk")


        Log.v("wq", "onCreate: isRoot =${ShellUtil.checkRootPermission()}")
        val job = Job()
        val coroutineExceptionHandler = CoroutineExceptionHandler{_, exception ->
            Log.v(
                "wq",
                "onCreate: "
            )}
       GlobalScope.launch(job + Dispatchers.Main + CoroutineName("111") + coroutineExceptionHandler, CoroutineStart.UNDISPATCHED) {
           for (i in 1..10) {
               Log.v("wq", "onCreate: Globale")
               delay(1000)
           }
       }

//        MainScope().launch {  }

        mainScope.launch {
            for (i in 1..10) {
                Log.v("wq", "onCreate: lifecycleScope")
                delay(1000)
            }
        }

//        System.setProperty("kotlinx.coroutines.debug", "on" )
        setContentView(R.layout.activity_jetpack)
        viewModelButton = findViewById(R.id.view_model)
        viewModelButton.setOnClickListener{
            myViewModel.nameLiveData.value = name
        }
        myViewModel.getUserName().observe(this){
            Log.v("wq", "onCreate: $it")
        }
    }

    class MyViewModel : ViewModel() {

        val nameLiveData: MutableLiveData<String> = MutableLiveData<String>()

        fun getUserName(): MutableLiveData<String>  {
            Log.v("wq", "getUserName: ")
            nameLiveData.value = "123"
            return nameLiveData
        }

        override fun onCleared() {
            super.onCleared()
            Log.v("wq", "onCleared: ")
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mainScope.cancel()

    }

    
}

