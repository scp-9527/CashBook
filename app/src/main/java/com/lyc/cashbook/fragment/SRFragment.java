package com.lyc.cashbook.fragment;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.lyc.cashbook.R;
import com.lyc.cashbook.base.BaseFragment;
import com.lyc.cashbook.bean.Info;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 */
public class SRFragment extends BaseFragment {

    private ImageView mIv_gz;
    private ImageView mIv_jt;
    private ImageView mIv_hb;
    private ImageView mIv_qt;
    private TextView mTv_gz;
    private TextView mTv_jt;
    private TextView mTv_hb;
    private TextView mTv_qt;
    private TextView mTv_value;
    private String name = "工资";
    private int res=R.mipmap.gz;
    private EditText mEt_remark;
    private TextView mTv_time;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_sr;
    }

    @Override
    protected void initView() {
        $(R.id.ll_gz).setOnClickListener(this);
        $(R.id.ll_jt).setOnClickListener(this);
        $(R.id.ll_hb).setOnClickListener(this);
        $(R.id.ll_qt).setOnClickListener(this);
        mIv_gz = $(R.id.iv_gz);
        mIv_jt = $(R.id.iv_jt);
        mIv_hb = $(R.id.iv_hb);
        mIv_qt = $(R.id.iv_qt);
        mTv_gz = $(R.id.tv_gz);
        mTv_jt = $(R.id.tv_jt);
        mTv_hb = $(R.id.tv_hb);
        mTv_qt = $(R.id.tv_qt);
        mTv_value = $(R.id.tv_value);
        mEt_remark = $(R.id.et_remark);
        mTv_time = $(R.id.tv_time);

        $(R.id.btn_1).setOnClickListener(this);
        $(R.id.btn_2).setOnClickListener(this);
        $(R.id.btn_3).setOnClickListener(this);
        $(R.id.btn_4).setOnClickListener(this);
        $(R.id.btn_5).setOnClickListener(this);
        $(R.id.btn_6).setOnClickListener(this);
        $(R.id.btn_7).setOnClickListener(this);
        $(R.id.btn_8).setOnClickListener(this);
        $(R.id.btn_9).setOnClickListener(this);
        $(R.id.btn_0).setOnClickListener(this);
        $(R.id.btn_dot).setOnClickListener(this);
        $(R.id.btn_delete).setOnClickListener(this);
        $(R.id.btn_sure).setOnClickListener(this);
        $(R.id.btn_reset).setOnClickListener(this);


        mTv_time.setText(new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Date()));
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_gz://点击工资
                name = "工资";
                res=R.mipmap.gz;
                initType(1);
                break;
            case R.id.ll_jt://点击津贴
                name = "津贴";
                res=R.mipmap.jt;
                initType(2);
                break;
            case R.id.ll_hb://点击红包
                name = "红包";
                res=R.mipmap.hb;
                initType(3);
                break;
            case R.id.ll_qt://点击其他
                name = "其他";
                res=R.mipmap.qt;
                initType(4);
                break;
            case R.id.btn_0:
                setValue("0");
                break;
            case R.id.btn_1:
                setValue("1");
                break;
            case R.id.btn_2:
                setValue("2");
                break;
            case R.id.btn_3:
                setValue("3");
                break;
            case R.id.btn_4:
                setValue("4");
                break;
            case R.id.btn_5:
                setValue("5");
                break;
            case R.id.btn_6:
                setValue("6");
                break;
            case R.id.btn_7:
                setValue("7");
                break;
            case R.id.btn_8:
                setValue("8");
                break;
            case R.id.btn_9:
                setValue("9");
                break;
            case R.id.btn_dot:
                setValue(".");
                break;
            case R.id.btn_delete://按了删除键，如果为0，没反应
                if (!mTv_value.getText().toString().equals("0")) {
                    //如果剩下一位数，变成0
                    if (mTv_value.getText().toString().length() == 1) {
                        mTv_value.setText("0");
                    } else {
                        //否则去掉最后一位
                        mTv_value.setText(mTv_value.getText().toString().subSequence(0, mTv_value.getText().toString().length() - 1));
                    }
                }
                break;
            case R.id.btn_reset:
                mTv_value.setText("0");//重置归0
                break;
            case R.id.btn_sure:
                if (mTv_value.getText().equals("0")) {
                    toast("请输入金额");
                } else {
                    SharedPreferences sharedPreferences = getActivity().getSharedPreferences("user", Context.MODE_PRIVATE);
                    String userId = sharedPreferences.getString("userId","");
                    Info info = new Info();
                    info.setId(System.currentTimeMillis());//设置id
                    info.setType(1);//设置类型
                    info.setName(name);//设置类别
                    info.setRemark(mEt_remark.getText().toString());//设置备注信息
                    info.setTime(mTv_time.getText().toString());//设置时间
                    info.setMoney(mTv_value.getText().toString());//设置金额
                    info.setRes(res);//设置图片资源
                    info.setUserId(userId);
                    boolean save = info.save();//保存到数据库
                    if(save){
                        toast("记录成功");
                        //发送广播
                        getActivity().sendBroadcast(new Intent("refresh"));
                        getActivity().finish();
                    }else {
                        toast("记录失败");
                    }
                }
                break;

        }
    }

    public void setValue(String a) {
        //如果是0，点击了. 变成0.
        if (mTv_value.getText().toString().equals("0")) {
            if (a.equals(".")) {
                mTv_value.setText("0.");
            } else {//否则直接变成相应的数字
                mTv_value.setText(a + "");
            }
        } else {//如果不为0，则拼接数字
            mTv_value.setText(mTv_value.getText().toString() + a + "");
        }
    }


    private void initType(int type) {
        switch (type) {
            case 1://设置工资为高亮
                mIv_gz.setImageResource(R.mipmap.gz_click);
                mTv_gz.setTextColor(getResources().getColor(R.color.colorClick));
                mIv_jt.setImageResource(R.mipmap.jt);
                mTv_jt.setTextColor(getResources().getColor(R.color.colorNormal));
                mIv_hb.setImageResource(R.mipmap.hb);
                mTv_hb.setTextColor(getResources().getColor(R.color.colorNormal));
                mIv_qt.setImageResource(R.mipmap.qt);
                mTv_qt.setTextColor(getResources().getColor(R.color.colorNormal));
                break;
            case 2://设置津贴为高亮
                mIv_gz.setImageResource(R.mipmap.gz);
                mTv_gz.setTextColor(getResources().getColor(R.color.colorNormal));
                mIv_jt.setImageResource(R.mipmap.jt_click);
                mTv_jt.setTextColor(getResources().getColor(R.color.colorClick));
                mIv_hb.setImageResource(R.mipmap.hb);
                mTv_hb.setTextColor(getResources().getColor(R.color.colorNormal));
                mIv_qt.setImageResource(R.mipmap.qt);
                mTv_qt.setTextColor(getResources().getColor(R.color.colorNormal));
                break;
            case 3://设置红包为高亮
                mIv_gz.setImageResource(R.mipmap.gz);
                mTv_gz.setTextColor(getResources().getColor(R.color.colorNormal));
                mIv_jt.setImageResource(R.mipmap.jt);
                mTv_jt.setTextColor(getResources().getColor(R.color.colorNormal));
                mIv_hb.setImageResource(R.mipmap.hb_click);
                mTv_hb.setTextColor(getResources().getColor(R.color.colorClick));
                mIv_qt.setImageResource(R.mipmap.qt);
                mTv_qt.setTextColor(getResources().getColor(R.color.colorNormal));
                break;
            case 4://设置其他为高亮
                mIv_gz.setImageResource(R.mipmap.gz);
                mTv_gz.setTextColor(getResources().getColor(R.color.colorNormal));
                mIv_jt.setImageResource(R.mipmap.jt);
                mTv_jt.setTextColor(getResources().getColor(R.color.colorNormal));
                mIv_hb.setImageResource(R.mipmap.hb);
                mTv_hb.setTextColor(getResources().getColor(R.color.colorNormal));
                mIv_qt.setImageResource(R.mipmap.qt_click);
                mTv_qt.setTextColor(getResources().getColor(R.color.colorClick));
                break;
        }
    }

}
