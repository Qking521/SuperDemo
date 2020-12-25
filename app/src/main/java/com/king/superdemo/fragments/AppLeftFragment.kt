package com.king.superdemo.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.AdapterView.OnItemClickListener
import android.widget.ArrayAdapter
import android.widget.ListView
import com.king.superdemo.R

class AppLeftFragment : BaseFragment() {

    //和AppManagerActivity通信
    interface CallBack {
        fun onItemClick(position: Int)
    }

    private val datas: Array<String>
        get() {
            val  array = context?.resources?.getStringArray(R.array.app_left_fragment) as Array<String>
            return array
        }

    private var mListView: ListView? = null
    private lateinit var callBack: (Int) -> Unit;
    override fun onAttach(context: Context) {
        super.onAttach(context)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.app_left_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mListView = view.findViewById(R.id.app_left_fragment_lv)
        mListView!!.setAdapter(ArrayAdapter(view.context, android.R.layout.simple_list_item_1, datas))
        mListView!!.setOnItemClickListener{ parent: AdapterView<*>?, view1: View?, position: Int, id: Long -> callBack!!.invoke(position) }
    }

    fun setOnItemClickListener(callBack: (Int) -> Unit) {
        this.callBack = callBack
    }

}