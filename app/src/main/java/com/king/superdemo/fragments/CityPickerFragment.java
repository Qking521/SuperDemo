package com.king.superdemo.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.king.superdemo.R;
import com.lljjcoder.Interface.OnCityItemClickListener;
import com.lljjcoder.bean.CityBean;
import com.lljjcoder.bean.DistrictBean;
import com.lljjcoder.bean.ProvinceBean;
import com.lljjcoder.citywheel.CityConfig;
import com.lljjcoder.style.cityjd.JDCityConfig;
import com.lljjcoder.style.cityjd.JDCityPicker;
import com.lljjcoder.style.citylist.CityListSelectActivity;
import com.lljjcoder.style.citylist.bean.CityInfoBean;
import com.lljjcoder.style.citylist.utils.CityListLoader;
import com.lljjcoder.style.citypickerview.CityPickerView;
import com.lljjcoder.style.citythreelist.ProvinceActivity;

import static android.app.Activity.RESULT_OK;


/**
 * 展示第三方库城市地址选择器
 * 参考链接:https://github.com/crazyandcoder/citypicker
 */
public class CityPickerFragment extends Fragment {

    private Context mContext;
    private TextView cityPickerTextView;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CityListLoader.getInstance().loadCityData(mContext);
        CityListLoader.getInstance().loadProData(mContext);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        LinearLayout linearLayout = (LinearLayout) inflater.inflate(R.layout.city_picker_fragment, container, false);
        cityPickerTextView = linearLayout.findViewById(R.id.city_picker_text_view);
        String[] styles = getResources().getStringArray(R.array.city_picker);
        int styleSize = styles.length;
        for (int i = 0; i < styleSize; i++) {
            Button button = new Button(mContext);
            button.setText(styles[i]);
            int finalI = i;
            button.setOnClickListener(v -> showCityPicker(finalI));
            linearLayout.addView(button);
        }
        return linearLayout;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CityListSelectActivity.CITY_SELECT_RESULT_FRAG) {
            if (resultCode == RESULT_OK) {
                if (data == null) {
                    return;
                }
                Bundle bundle = data.getExtras();

                CityInfoBean cityInfoBean = (CityInfoBean) bundle.getParcelable("cityinfo");

                if (null == cityInfoBean) {
                    return;
                }
                cityPickerTextView.setText("城市： " + cityInfoBean.toString());
            }
        }
        if (requestCode == ProvinceActivity.RESULT_DATA) {
            if (resultCode == RESULT_OK) {
                if (data == null) {
                    return;
                }
                //省份结果
                com.lljjcoder.style.citythreelist.CityBean province = data.getParcelableExtra("province");
                //城市结果
                com.lljjcoder.style.citythreelist.CityBean city = data.getParcelableExtra("city");
                //区域结果
                com.lljjcoder.style.citythreelist.CityBean area = data.getParcelableExtra("area");
                cityPickerTextView.setText(province.getName() + city.getName() + area.getName());
            }
        }
    }

    private void showCityPicker(int index) {
        switch (index) {
            case 0 : showIOSStyle(); break;
            case 1 : showJDStyle(); break;
            case 2 : showFirstLevelCity(); break;
            case 3 : showThirdLevelCity(); break;
        }
    }

    private void showThirdLevelCity() {
        Intent intent = new Intent(mContext, ProvinceActivity.class);
        startActivityForResult(intent, ProvinceActivity.RESULT_DATA);
    }

    private void showFirstLevelCity() {
        Intent intent = new Intent(mContext, CityListSelectActivity.class);
        startActivityForResult(intent, CityListSelectActivity.CITY_SELECT_RESULT_FRAG);
    }

    private void showJDStyle() {
        JDCityPicker cityPicker = new JDCityPicker();
        JDCityConfig jdCityConfig = new JDCityConfig.Builder().build();

        jdCityConfig.setShowType(JDCityConfig.ShowType.PRO_CITY_DIS);
        cityPicker.init(mContext);
        cityPicker.setConfig(jdCityConfig);
        cityPicker.setOnCityItemClickListener(new OnCityItemClickListener() {
            @Override
            public void onSelected(ProvinceBean province, CityBean city, DistrictBean district) {
                cityPickerTextView.setText("城市选择结果：\n" + province.getName() + "(" + province.getId() + ")\n"
                        + city.getName() + "(" + city.getId() + ")\n"
                        + district.getName() + "(" + district.getId() + ")");
            }

            @Override
            public void onCancel() {
            }
        });
        cityPicker.showCityPicker();
    }

    private void showIOSStyle() {
        CityPickerView mPicker = new CityPickerView();
        mPicker.init(mContext);
        CityConfig cityConfig = new CityConfig.Builder().build();
        mPicker.setConfig(cityConfig);
        mPicker.setOnCityItemClickListener(new OnCityItemClickListener() {
            @Override
            public void onSelected(ProvinceBean province, CityBean city, DistrictBean district) {
                Log.i("wq", "onSelected: province"+ province.toString());
                Log.i("wq", "onSelected: city"+ city.toString());
                Log.i("wq", "onSelected: district"+ district.toString());
                cityPickerTextView.setText(province.getName() +"\n" + city.getName() + "\n" + district.getName());
            }

            @Override
            public void onCancel() {

            }
        });
        mPicker.showCityPicker( );
    }
}
