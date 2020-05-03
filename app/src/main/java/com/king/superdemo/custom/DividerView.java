package com.king.superdemo.custom;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;

import androidx.annotation.Nullable;

import com.king.superdemo.R;

import java.util.ArrayList;
import java.util.List;

/**
 * 分割线View
 * 跟随目标view进行动态的显示，目标view可以是多个
 * 只要有一个目标view显示，那么分割线就显示，只有目标view都隐藏，分割线才隐藏
 */
public class DividerView extends View {

    private Context mContext;
    private View mDecorView;
    /**
     * 跟随动态显示view的集合,通过mFollowedViewName获得
     * @see #mFollowedViewName
     */
    private List<View> mFollowedViewList = new ArrayList<>();
    /**
     * 跟随动态显示View的ID集合
     */
    private String[] mFollowedViewName = new String[]{};

    private ViewTreeObserver.OnGlobalLayoutListener globalLayoutListener = () -> checkVisible();

    public DividerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.divider_view, 0, 0);
        String followName = ta.getString(R.styleable.divider_view_follow);
        if (!TextUtils.isEmpty(followName)) {
            mFollowedViewName = followName.split("\\|");
        }
        ta.recycle();
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        mDecorView = ((Activity) mContext).getWindow().getDecorView();
        mDecorView.getViewTreeObserver().addOnGlobalLayoutListener(globalLayoutListener);
        int length = mFollowedViewName.length;
        for (int i = 0; i < length; i++) {
            int id = mContext.getResources().getIdentifier(mFollowedViewName[i], "id", mContext.getPackageName());
            View view = mDecorView.findViewById(id);
            mFollowedViewList.add(view);
            Log.v("wq", "DividerView: id=" + id);
        }
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        mDecorView.getViewTreeObserver().removeOnGlobalLayoutListener(globalLayoutListener);
    }

    private void checkVisible() {
        boolean visible = false;
        for (View view : mFollowedViewList) {
            if (view != null && view.getVisibility() == VISIBLE) {
                visible = true;
                break;
            }
        }
        if ((visible && getVisibility() == VISIBLE) || (!visible && getVisibility() == GONE)) {
            return;
        }
        setVisibility(visible ? VISIBLE : GONE);
    }
}
