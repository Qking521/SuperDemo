package com.king.superdemo.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.bigkoo.pickerview.builder.OptionsPickerBuilder
import com.bigkoo.pickerview.builder.TimePickerBuilder
import com.king.superdemo.R


/**
 * 展示第三方库时间/选项/日历选择器
 * 参考链接:https://github.com/Bigkoo/Android-PickerView
 */
class TimePickerFragment : Fragment() {
    private var mContext: Context? = null
    private var cityPickerResultText: TextView? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val linearLayout = inflater.inflate(R.layout.time_picker_fragment, container, false) as LinearLayout
        cityPickerResultText = linearLayout.findViewById(R.id.time_picker_text_view)
        val styles = resources.getStringArray(R.array.time_picker)
        val styleSize = styles.size
        for (i in 0 until styleSize) {
            val button = Button(mContext)
            button.text = styles[i]
            button.setOnClickListener { showTimePicker(i) }
            linearLayout.addView(button)
        }
        return linearLayout
    }

    private fun showTimePicker(index: Int) {
        when (index) {
            0 -> showTimePicker()
            1 -> showOptionsPicker()
        }
    }

    private fun showOptionsPicker() {
        val options1Items = listOf<Int>(1, 2, 3, 4)

        val pvOptions = OptionsPickerBuilder(mContext) { options1, options2, options3, v -> cityPickerResultText?.setText(options1.toString()) }.build<Any>()

        pvOptions.setPicker(options1Items)
        pvOptions.show()
    }

    private fun showTimePicker() {

        val pvTime = TimePickerBuilder(context){ date, v -> Toast.makeText(context, "", Toast.LENGTH_SHORT).show() }.build()
        pvTime.show();
    }
}