package com.ej.ejdian.ui;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.ej.ejdian.annotation.LayoutSetting;
import com.ej.ejdian.view.BaseLayout;
import com.readystatesoftware.systembartint.SystemBarTintManager;

import java.lang.reflect.Method;

import butterknife.BindView;
import butterknife.ButterKnife;

public abstract class BaseActivity extends AppCompatActivity implements View.OnClickListener {
    protected LayoutInflater mLayoutInflater;
    private BaseLayout mBaseLayout;

    /**
     * title资源id
     */
    private int mLayoutTitleId;
    /**
     * 是否设置主题色沉浸头
     */
    private boolean isThemeBar;

    @BindView(R.id.tvBack)
    public TextView tvBack;
    @BindView(R.id.tvTitle)
    public TextView tvTitle;
    @BindView(R.id.tvRight)
    public TextView tvRight;
    @BindView(R.id.ivRight)
    public ImageView ivRight;
    @BindView(R.id.llLeft)
    public LinearLayout llLeft;

    protected Intent mIntent;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mLayoutInflater = LayoutInflater.from(this);
        try {
            Class<?>[] parameterTypes = {Bundle.class};
            Method method = getClass().getDeclaredMethod("onCreate", parameterTypes);
            LayoutSetting setting = method.getAnnotation(LayoutSetting.class);
            if (setting == null) {
                isThemeBar = true;
                mLayoutTitleId = R.layout.title_bar;
            } else {
                isThemeBar = setting.isThemeBar();
                mLayoutTitleId = setting.titleId();
            }
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
            isThemeBar = true;
        }
    }

    @Override
    public void setContentView(int layoutResID) {
        mBaseLayout = new BaseLayout(this, mLayoutTitleId, layoutResID, mLayoutInflater);
        super.setContentView(mBaseLayout);
        ButterKnife.bind(this, mBaseLayout);
        initSystemBar();
        initData();
        listener();
    }

    @Override
    public void setContentView(View view) {
        mBaseLayout = new BaseLayout(this, mLayoutTitleId, view, mLayoutInflater);
        super.setContentView(mBaseLayout);
        ButterKnife.bind(this, mBaseLayout);
        initSystemBar();
        initData();
        listener();
    }

    protected void invoke(Activity from, Class<BaseActivity> clazz, String backText, String titleText) {
        Intent baseIntent = new Intent(from, clazz);
        baseIntent.putExtra("backText", backText);
        baseIntent.putExtra("titleText", titleText);
        from.startActivity(baseIntent);
    }

    protected void invoke(Activity from, Class<BaseActivity> clazz, String titleText) {
        Intent baseIntent = new Intent(from, clazz);
        baseIntent.putExtra("titleText", titleText);
        from.startActivity(baseIntent);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.llLeft) {
            finish();
        }
    }

    protected void setLeftVisible(int isVisible) {
        llLeft.setVisibility(isVisible);
    }

    protected void setBackText(String text) {
        if (!TextUtils.isEmpty(text) || "".equals(text)) {
            if (llLeft.getVisibility() == View.GONE)
                llLeft.setVisibility(View.VISIBLE);
            tvBack.setText(text);
        } else
            setLeftVisible(View.GONE);
    }

    protected void setTitle(String text) {
        if (!TextUtils.isEmpty(text))
            tvTitle.setText(text);
    }

    protected void setRightText(String text) {
        if (!TextUtils.isEmpty(text)) {
            tvRight.setVisibility(View.VISIBLE);
            tvRight.setText(text);
        }
    }

    protected void setRightIcon(int iconID) {
        ivRight.setVisibility(View.VISIBLE);
        ivRight.setImageResource(iconID);
    }


    protected void initData() {
        mIntent = getIntent();
        setBackText(mIntent.getStringExtra("backText"));
        setTitle(mIntent.getStringExtra("titleText"));
    }


    protected void listener() {
        llLeft.setOnClickListener(this);
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }


    /**
     * 初始化沉浸头
     */
    private void initSystemBar() {
        if (Build.VERSION.SDK_INT >= 19) {
            setTranslucentStatus(true);
            if (isThemeBar) {
                SystemBarTintManager tintManager = new SystemBarTintManager(this);
                tintManager.setStatusBarTintEnabled(true);
                tintManager.setStatusBarTintColor(getResources().getColor(R.color.colorPrimaryDark));
                SystemBarTintManager.SystemBarConfig config = tintManager.getConfig();
                mBaseLayout.setPadding(0,
                        config.getPixelInsetTop(false),
                        config.getPixelInsetRight(),
                        config.getPixelInsetBottom());
            }
        }
    }

    @TargetApi(19)
    private void setTranslucentStatus(boolean on) {
        Window win = getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }


}
