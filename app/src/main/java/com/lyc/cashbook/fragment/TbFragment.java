package com.lyc.cashbook.fragment;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.MPPointF;
import com.lyc.cashbook.R;
import com.lyc.cashbook.base.BaseFragment;
import com.lyc.cashbook.bean.Info;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class TbFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener {


    private PieChart chart;
    private boolean isPay=true;
    private List<Info> srList=new ArrayList<>();//收入列表
    private List<Info> zcList=new ArrayList<>();//支出列表
    private double type1;//第一个类别钱
    private double type2;//第二个
    private double type3;//第三个
    private double type4;//第四个
    private boolean isChange;
    private View mBg;
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
                initChart(); //初始化
            }
        }
    };


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_tb;
    }

    @Override
    protected void initView() {
        getActivity().registerReceiver(receiver,new IntentFilter("refresh"));
        getActivity().registerReceiver(receiver,new IntentFilter("bg"));  //显示刷新内容
        chart = $(R.id.chart);
        $(R.id.btn_zc).setOnClickListener(this);
        $(R.id.btn_sr).setOnClickListener(this);
        mBg = $(R.id.bg);

        initChart();
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_zc://点击支出
                isPay=true;
                initChart();
                break;
            case R.id.btn_sr://点击收入
                isPay=false;
                initChart();
                break;
        }
    }

    private void initChart() {
        srList.clear();
        zcList.clear();
        type1=0;
        type2=0;
        type3=0;
        type4=0;
        List<Info> list = LitePal.findAll(Info.class);
        for (int i = 0; i < list.size(); i++) {
            if(list.get(i).getType()==1){
                srList.add(list.get(i));//添加收入数据
            }else {
                zcList.add(list.get(i));//添加支出数据
            }
        }
        List<PieEntry> strings = new ArrayList<>();
        if(isPay){//支出
            for (int i = 0; i < zcList.size(); i++) {
                if(zcList.get(i).getName().equals("话费")){//话费总支出
                    type1+=Double.parseDouble(zcList.get(i).getMoney());
                }else if(zcList.get(i).getName().equals("购物")){//购物总支出
                    type2+=Double.parseDouble(zcList.get(i).getMoney());
                }else if(zcList.get(i).getName().equals("吃饭")){//吃饭总支出
                    type3+=Double.parseDouble(zcList.get(i).getMoney());
                }else if(zcList.get(i).getName().equals("其他")){//其他总支出
                    type4+=Double.parseDouble(zcList.get(i).getMoney());
                }
            }
        }else {//收入
            for (int i = 0; i < srList.size(); i++) {
                if(srList.get(i).getName().equals("工资")){//工资总收入
                    type1+=Double.parseDouble(srList.get(i).getMoney());
                }else if(srList.get(i).getName().equals("津贴")){//津贴总收入
                    type2+=Double.parseDouble(srList.get(i).getMoney());
                }else if(srList.get(i).getName().equals("红包")){//红包总收入
                    type3+=Double.parseDouble(srList.get(i).getMoney());
                }else if(srList.get(i).getName().equals("其他")){//其他总收入
                    type4+=Double.parseDouble(srList.get(i).getMoney());
                }
            }
        }

        if(isPay){

            //总价为0则不添加

            if(type1!=0){
                strings.add(new PieEntry(new Double(type1).longValue(),"话费"));
            }
            if(type2!=0){
                strings.add(new PieEntry(new Double(type2).longValue(),"购物"));
            }
            if(type3!=0){
                strings.add(new PieEntry(new Double(type3).longValue(),"吃饭"));
            }
            if(type4!=0){
                strings.add(new PieEntry(new Double(type4).longValue(),"其他"));
            }
        }else {
            if(type1!=0){
                strings.add(new PieEntry(new Double(type1).longValue(),"工资"));
            }
            if(type2!=0){
                strings.add(new PieEntry(new Double(type2).longValue(),"津贴"));
            }
            if(type3!=0){
                strings.add(new PieEntry(new Double(type3).longValue(),"红包"));
            }
            if(type4!=0){
                strings.add(new PieEntry(new Double(type4).longValue(),"其他"));
            }

        }

        PieDataSet dataSet = new PieDataSet(strings,"");

        //设置四种颜色
        ArrayList<Integer> colors = new ArrayList<Integer>();
        colors.add(Color.parseColor("#1296db"));
        colors.add(Color.parseColor("#FFC006"));
        colors.add(Color.parseColor("#d4237a"));
        colors.add(Color.parseColor("#7d670e"));

        dataSet.setColors(colors);
        dataSet.setSliceSpace(3f);//设置饼块之间的间隔


        PieData pieData = new PieData(dataSet);//设置数据
        pieData.setDrawValues(true);
        pieData.setValueFormatter(new PercentFormatter(chart));//显示百分比
        pieData.setValueTextSize(14f);//文字大小

        if(isPay){
            chart.setCenterText("支出");//设置环中文字
        }else {
            chart.setCenterText("收入");//设置环中文字
        }
        chart.setCenterTextSize(22f);//设置环中文字的大小

        Legend legend = chart.getLegend();
        legend.setEnabled(false);

        chart.setUsePercentValues(true);//允许使用百分比
        chart.setData(pieData);
        chart.invalidate();//图表重绘

        Description description = new Description();
        description.setText("");
        chart.setDescription(description);
    }
    @Override
    public void onRefresh() {
        initChart();
    }
}
