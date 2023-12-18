package com.lyc.cashbook.fragment;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.lyc.cashbook.R;
import com.lyc.cashbook.adapter.MxAdapter;
import com.lyc.cashbook.adapter.MyAdapter;
import com.lyc.cashbook.base.BaseFragment;
import com.lyc.cashbook.bean.Info;
import com.lyc.cashbook.bean.User;
import com.lyc.cashbook.ui.FbActivity;
import com.lyc.cashbook.ui.SearchActivity;

import org.litepal.LitePal;
import org.litepal.crud.DataSupport;
import org.litepal.crud.LitePalSupport;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */


//明细
public class MxFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener {

    private SwipeMenuListView listView;//侧滑删除
    private MyAdapter adapter;

    private List<Info> list_show = new ArrayList<Info>();;
    private boolean isChange;
    //接收到广播刷新
    private BroadcastReceiver receiver=new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {    //上下文内容连接跳转
            if(intent.getAction().equals("bg")){
                isChange=!isChange;
                if(isChange){
                    mBg.setBackgroundResource(R.mipmap.b);  //改变
                }else {
                    mBg.setBackgroundResource(R.mipmap.a);  //不变
                }
            }else {
                initData(); //初始化
            }
        }
    };
    //声明控件
    private TextView mTv_sr;
    private TextView mTv_zc;
    //收入
    private double sr;
    //支出
    private double zc;
    private TextView mTv_jy;
    private View mBg;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_mx;
    }

    @Override
    protected void initView() {
        getActivity().registerReceiver(receiver,new IntentFilter("refresh"));  //接收刷新
        getActivity().registerReceiver(receiver,new IntentFilter("bg"));  //显示刷新内容
        //设置监听事件
        $(R.id.fb).setOnClickListener(this);
        $(R.id.search).setOnClickListener(this);

        //找到控件
        mTv_sr = $(R.id.tv_sr);
        mTv_zc = $(R.id.tv_zc);
        mTv_jy = $(R.id.tv_jy);
        mBg = $(R.id.bg);

    }

    @Override
    protected void initData() {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("user", Context.MODE_PRIVATE);
        String userId = sharedPreferences.getString("userId","");
        sr=0;
        zc=0;
        //获取本地所有的明细
        list_show = LitePal.where("userId=?",userId).find(Info.class);
        //List<Info> list = LitePal.findAll(Info.class);

        for (int i = 0; i < list_show.size(); i++) {
            if(list_show.get(i).getType()==1){//遍历所有收入相加
                sr+=Double.valueOf(list_show.get(i).getMoney());
            }else {//遍历所有支出相加
                zc+=Double.valueOf(list_show.get(i).getMoney());
            }
        }
        //设置收入
        mTv_sr.setText(String.valueOf(sr));
        //设置支出
        mTv_zc.setText(String.valueOf(zc));
        //设置结余
        mTv_jy.setText(String.valueOf(sr-zc));

        initAdapter(list_show);
        initListView();
    }
    private void setData(){
        sr=0;
        zc=0;
        //获取本地所有的明细
        for (int i = 0; i < list_show.size(); i++) {
            if(list_show.get(i).getType()==1){//遍历所有收入相加
                sr+=Double.valueOf(list_show.get(i).getMoney());
            }else {//遍历所有支出相加
                zc+=Double.valueOf(list_show.get(i).getMoney());
            }
        }
        //设置收入
        mTv_sr.setText(String.valueOf(sr));
        //设置支出
        mTv_zc.setText(String.valueOf(zc));
        //设置结余
        mTv_jy.setText(String.valueOf(sr-zc));

        initAdapter(list_show);
        initListView();
    }


    //设置适配器
    private void initAdapter(List<Info> list) {
        listView = (SwipeMenuListView)$(R.id.collector_listview);
        adapter = new MyAdapter(getActivity(), R.layout.item_mx, list);
        listView.setAdapter(adapter);
    }

    //初始化ListView数据，在OnCreate方法中调用
    private void initListView()
    {

        SwipeMenuCreator creator = new SwipeMenuCreator() {
            @Override
            public void create(SwipeMenu menu) {

                // 创建“删除”项
                SwipeMenuItem deleteItem = new SwipeMenuItem(
                        getActivity());
                // 设置项目背景
                deleteItem.setBackground(new ColorDrawable(Color.rgb(0xF9,
                        0x3F, 0x25)));
                // 设置项目宽度
                deleteItem.setWidth(dp2px(90));
                // 设置图标
                deleteItem.setIcon(R.drawable.ic_delete);
                // 添加到菜单
                menu.addMenuItem(deleteItem);
            }
        };

        // 设置创建者
        listView.setMenuCreator(creator);

        listView.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {//设置菜单项目的监听事件
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {//打开菜单项单击
                switch (index) {
                    case 0:

                        Info infoBean = new Info();
                        infoBean = list_show.get(position);//获取列表位置
                        LitePal.delete(Info.class, infoBean.getId());//在数据库中找到
                        list_show.remove(position);//删除
                        adapter.notifyDataSetChanged();//通知数据集已更改
                        setData();
                        Log.d("delete", "onMenuItemClick: "+"删除成功");
                        break;
                }
                // false：关闭菜单；正确：不关闭菜单
                return false;
            }
        });

        // 设置滑动侦听器
        listView.setOnSwipeListener(new SwipeMenuListView.OnSwipeListener()
        {
            @Override
            public void onSwipeStart(int position)
            {
                // 滑动开始
            }

            @Override
            public void onSwipeEnd(int position)
            {
                // 滑动结束
            }
        });

    }

    private int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,//应用维度
                getResources().getDisplayMetrics());//获取显示指标
    }

    @Override
    public void onClick(View view) {   //设置点击view
        switch (view.getId()) {
            case R.id.fb://前往发布界面
                startActivity(FbActivity.class);
                break;
            case R.id.search://前往搜索界面
                startActivity(SearchActivity.class);
                break;
        }
    }

    //刷新数据
    @Override
    public void onRefresh() {
        initData();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().unregisterReceiver(receiver);//取消注册接收器
    }
}
