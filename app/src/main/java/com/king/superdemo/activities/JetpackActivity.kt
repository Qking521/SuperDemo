package com.king.superdemo.activities

import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.activity.viewModels
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.king.superdemo.R
import kotlinx.coroutines.launch

class JetpackActivity : BaseActivity() {

//    private lateinit var binding: JetpackActivityBinding

    lateinit var viewModelButton:Button
    val myViewModel : MyViewModel by viewModels()
    val name: String by lazy { "000" }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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

    
}

