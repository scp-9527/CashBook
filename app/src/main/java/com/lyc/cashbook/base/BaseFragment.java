package com.lyc.cashbook.base;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.ColorRes;
import androidx.annotation.DrawableRes;
import androidx.annotation.IdRes;
import androidx.fragment.app.Fragment;

import com.lyc.cashbook.R;
import com.lyc.cashbook.utils.ToastUtils;


public abstract class BaseFragment extends Fragment implements View.OnClickListener {

    private View mInflate;
    private ProgressDialog progressDialog;
    //获取布局文件ID
    protected abstract int getLayoutId();

    /* 用于普通View 进行快速牵引 */
    private SparseArray<View> mViews = new SparseArray<View>();

    protected abstract void initView();


    protected void initData() {
    }

    /***
     * 用于在初始化View之前做一些事
     */
    protected void initBefore() {
    }

    /**
     * 初始化监听
     */
    public void initListener() {
    }

    protected <T extends View> T $(int id) {
        return (T) mInflate.findViewById(id);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        initBefore();
        mInflate = inflater.inflate(getLayoutId(), container, false);
        initView();
        initProgressDialog();
        initData();
        initListener();
        return mInflate;
    }

    protected void startActivity(Class<?> clazz) {
        Intent intent = new Intent(getActivity(), clazz);
        startActivity(intent);
    }

    protected void startActivity(Class<?> clazz, Bundle extras) {
        Intent intent = new Intent(getActivity(), clazz);
        intent.putExtras(extras);
        startActivity(intent);
    }

    public <T extends View> T getView(int viewId) {
        View view = mViews.get(viewId);
        if (view == null) {
            view = mInflate.findViewById(viewId);
            /* 放入牵引Map，增强效率 */
            mViews.put(viewId, view);
        }
        return (T) view;
    }

    public TextView getViewTv(int viewId) {
        return getView(viewId);
    }

    public EditText getViewEt(int viewId) {
        return getView(viewId);
    }

    public ImageView getViewIv(int viewId) {
        return getView(viewId);
    }

    public boolean getVisible(int viewId) {
        if (getView(viewId).getVisibility() == View.VISIBLE) {
            return true;
        }
        return false;
    }

    public void setVisible(int viewId, boolean visible) {
        View view = getView(viewId);
        view.setVisibility(visible ? View.VISIBLE : View.GONE);
    }

    public void setBackgroundResource(int viewId, @DrawableRes int resId) {
        getView(viewId).setBackgroundResource(resId);
    }

    public void setImageView(int viewId, @DrawableRes int resId) {
        ImageView imageView = getView(viewId);
        imageView.setImageResource(resId);
    }

    public void setImageView(int viewId, Bitmap drawable) {
        ImageView imageView = getView(viewId);
        imageView.setImageBitmap(drawable);
    }

    public void setImageView(int viewId, String url) {
        ImageView iv = getView(viewId);
//        Glide.with(getActivity()).load(url).into(iv);
    }

    public void setImageView(View parent, int viewId, String url) {
        ImageView iv = (ImageView) parent.findViewById(viewId);
//        Glide.with(getActivity()).load(url).into(iv);
    }

    public void setTextView(int viewId, String text) {
        TextView textView = getView(viewId);
        textView.setText(text);
    }

    public void setTextView(View parent, int viewId, String text) {
        TextView textView = (TextView) parent.findViewById(viewId);
        textView.setText(text);
    }

    public void setTextView(int viewId, @ColorRes int resId) {
        TextView textView = getView(viewId);
        textView.setTextColor(getResources().getColor(resId));
    }

    public void setEditText(int viewId, String text) {
        EditText editText = getView(viewId);
        editText.setText(text);
    }

    public void setEditText(int viewId, @ColorRes int resId) {
        EditText editText = getView(viewId);
        editText.setTextColor(getResources().getColor(resId));
    }

    public void setHint(int viewId, String text) {
        EditText editText = getView(viewId);
        editText.setHint(text);
    }

    public void setHint(int viewId, @ColorRes int resId) {
        EditText editText = getView(viewId);
        editText.setHintTextColor(getResources().getColor(resId));
    }


    public void setButton(int viewId, String text) {
        Button button = getView(viewId);
        button.setText(text);
    }

    public void setButton(int viewId, @DrawableRes int resId) {
        Button button = getView(viewId);
        button.setBackgroundResource(resId);
    }

    public void setButton(int viewId, boolean enabled) {
        Button button = getView(viewId);
        button.setEnabled(enabled);
    }

    public void setCheckBox(int viewId, boolean isCheck) {
        CheckBox checkBox = getView(viewId);
        checkBox.setChecked(isCheck);
    }

    public void setRadioGroup(int viewId, @IdRes int childId) {
        RadioGroup rb = getView(viewId);
        rb.check(childId);
    }



    protected void setTitle(String title) {
        TextView titleView = (TextView) getView(R.id.titleView);
        if (titleView != null) {
            titleView.setText(title);
        }
    }
    /**
     * Toast
     */
    public void toast(String message) {
        ToastUtils.getInstance().showToast(message);
    }

    public void setOnClickListener(int... ids) {
        for (int i = 0; i < ids.length; i++) {
            getView(ids[i]).setOnClickListener(this);
        }
    }

    @Override
    public void onClick(View view) {

    }
    //初始化等待框
    private void initProgressDialog() {
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setIndeterminate(false);//循环滚动
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setMessage("加载中...");
        progressDialog.setCancelable(true);//false不能取消显示，true可以取消显示
    }

    public void show(){
        progressDialog.show();
    }
    public void  dismiss(){
        progressDialog.dismiss();
    }
}
