package com.lyc.cashbook.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.lyc.cashbook.R;
import com.lyc.cashbook.adapter.MxAdapter;
import com.lyc.cashbook.base.BaseActivity;
import com.lyc.cashbook.bean.Info;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends BaseActivity {
    //声明控件
    private String keyword;//关键字
    private List<Info> searchList=new ArrayList<>();
    private MxAdapter mAdapter;
    private RecyclerView mList;//回收视图
    private EditText mEt_search;

    @Override
    protected int setLayoutResourceID() {
        return R.layout.activity_search;
    }//获取前端页面设计的地址
    //找到控件
    @Override
    protected void initView() {
        setTitle("搜索");
        mList = findViewById(R.id.list);
        mEt_search = findViewById(R.id.et_search);
        findViewById(R.id.btn_search).setOnClickListener(this);//按键触发
        mList.setLayoutManager(new LinearLayoutManager(this));
    }


    private void getData() {  //获取数据
        searchList.clear();//清除搜索列表
        //获取所有数据
        List<Info> list = LitePal.findAll(Info.class);
        if(TextUtils.isEmpty(keyword)){
            searchList=list;
        }else {
            //如果搜索收入，显示所有收入数据
            if("收入".equals(keyword)){
                for (int i = 0; i <list.size() ; i++) {
                   if(list.get(i).getType()==1){
                       searchList.add(list.get(i));
                   }
                }
                //如果搜索支出，显示所有支出数据
            }else if("支出".equals(keyword)){
                for (int i = 0; i <list.size() ; i++) {
                    if(list.get(i).getType()==2){
                        searchList.add(list.get(i));
                    }
                }
            }else {
                //如果明细信息匹配搜索结果，则添加到列表
                for (int i = 0; i < list.size(); i++) {
                    if(!list.get(i).getName().contains(keyword)){
                        if(!list.get(i).getMoney().contains(keyword)){
                            if(!list.get(i).getRemark().contains(keyword)){
                                if(!list.get(i).getTime().contains(keyword)){
                                }else {
                                    searchList.add(list.get(i));
                                }
                            }else {
                                searchList.add(list.get(i));
                            }
                        }else {
                            searchList.add(list.get(i));
                        }
                    }else {
                        searchList.add(list.get(i));
                    }
                }
            }
        }

        initAdapter();//初始化适配器
    }


    private void initAdapter() {//显示列表
        mAdapter=new MxAdapter(SearchActivity.this);
        mAdapter.setNewData(searchList);
        mList.setAdapter(mAdapter);
    }

    @Override
    public void onClick(View v) {    //设置点击按钮
        switch (v.getId()){
            case R.id.btn_search:
                keyword = mEt_search.getText().toString();
                if(TextUtils.isEmpty(keyword)){
                    toast("请输入内容");
                }else {
                    getData();
                }
                break;
        }
    }
}
