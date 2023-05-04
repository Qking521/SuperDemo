package com.king.superdemo.fragments

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.king.superdemo.R
import com.lljjcoder.Interface.OnCityItemClickListener
import com.lljjcoder.bean.DistrictBean
import com.lljjcoder.bean.ProvinceBean
import com.lljjcoder.citywheel.CityConfig
import com.lljjcoder.style.cityjd.JDCityConfig
import com.lljjcoder.style.cityjd.JDCityPicker
import com.lljjcoder.style.citylist.CityListSelectActivity
import com.lljjcoder.style.citylist.bean.CityInfoBean
import com.lljjcoder.style.citylist.utils.CityListLoader
import com.lljjcoder.style.citypickerview.CityPickerView
import com.lljjcoder.style.citythreelist.CityBean
import com.lljjcoder.style.citythreelist.ProvinceActivity

/**
 * 展示第三方库城市地址选择器
 * 参考链接:https://github.com/crazyandcoder/citypicker
 */
class CityPickerFragment : Fragment() {
    private var mContext: Context? = null
    private var cityPickerResultText: TextView? = null
    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        CityListLoader.getInstance().loadCityData(mContext)
        CityListLoader.getInstance().loadProData(mContext)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val linearLayout = inflater.inflate(R.layout.city_picker_fragment, container, false) as LinearLayout
        cityPickerResultText = linearLayout.findViewById(R.id.city_picker_text_view)
        val styles = resources.getStringArray(R.array.city_picker)
        val styleSize = styles.size
        for (i in 0 until styleSize) {
            val button = Button(mContext)
            button.text = styles[i]
            button.setOnClickListener { v: View? -> showCityPicker(i) }
            linearLayout.addView(button)
        }
        return linearLayout
    }

    @SuppressLint("SetTextI18n")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CityListSelectActivity.CITY_SELECT_RESULT_FRAG) {
            if (resultCode == Activity.RESULT_OK) {
                if (data == null) {
                    return
                }
                val bundle = data.extras
                val cityInfoBean = bundle!!.getParcelable<Parcelable>("cityinfo") as CityInfoBean?
                        ?: return
                cityPickerResultText!!.text = "城市： $cityInfoBean"
            }
        }
        if (requestCode == ProvinceActivity.RESULT_DATA) {
            if (resultCode == Activity.RESULT_OK) {
                if (data == null) {
                    return
                }
                //省份结果
                val province: CityBean? = data.getParcelableExtra("province")
                //城市结果
                val city: CityBean? = data.getParcelableExtra("city")
                //区域结果
                val area: CityBean? = data.getParcelableExtra("area")
                cityPickerResultText!!.text = province!!.name + city!!.name + area!!.name
            }
        }
    }

    private fun showCityPicker(index: Int) {
        when (index) {
            0 -> showIOSStyle()
            1 -> showJDStyle()
            2 -> showFirstLevelCity()
            3 -> showThirdLevelCity()
        }
    }

    private fun showThirdLevelCity() {
        val intent = Intent(mContext, ProvinceActivity::class.java)
        startActivityForResult(intent, ProvinceActivity.RESULT_DATA)
    }

    private fun showFirstLevelCity() {
        val intent = Intent(mContext, CityListSelectActivity::class.java)
        startActivityForResult(intent, CityListSelectActivity.CITY_SELECT_RESULT_FRAG)
    }

    private fun showJDStyle() {
        val cityPicker = JDCityPicker()
        val jdCityConfig = JDCityConfig.Builder().build()
        jdCityConfig.showType = JDCityConfig.ShowType.PRO_CITY_DIS
        cityPicker.init(mContext)
        cityPicker.setConfig(jdCityConfig)
        cityPicker.setOnCityItemClickListener(object : OnCityItemClickListener() {
            override fun onSelected(province: ProvinceBean, city: com.lljjcoder.bean.CityBean, district: DistrictBean) {
                cityPickerResultText!!.text = """
                    城市选择结果：
                    ${province.name}(${province.id})
                    ${city.name}(${city.id})
                    ${district.name}(${district.id})
                    """.trimIndent()
            }

            override fun onCancel() {}
        })
        cityPicker.showCityPicker()
    }

    private fun showIOSStyle() {
        val mPicker = CityPickerView()
        mPicker.init(mContext)
        val cityConfig = CityConfig.Builder().build()
        mPicker.setConfig(cityConfig)
        mPicker.setOnCityItemClickListener(object : OnCityItemClickListener() {
            override fun onSelected(province: ProvinceBean, city: com.lljjcoder.bean.CityBean, district: DistrictBean) {
                Log.i("wq", "onSelected: province$province")
                Log.i("wq", "onSelected: city$city")
                Log.i("wq", "onSelected: district$district")
                cityPickerResultText!!.text = """
                    ${province.name}
                    ${city.name}
                    ${district.name}
                    """.trimIndent()
            }

            override fun onCancel() {}
        })
        mPicker.showCityPicker()
    }
}