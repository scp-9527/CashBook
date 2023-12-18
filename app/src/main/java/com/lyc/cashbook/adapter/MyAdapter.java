package com.lyc.cashbook.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.lyc.cashbook.R;
import com.lyc.cashbook.bean.Info;

import java.util.List;

public class MyAdapter extends ArrayAdapter<Info> {
    private int resourceId;

    public MyAdapter(Context context, int resource, List<Info> objects) {
        super(context, resource, objects);
        resourceId = resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {  //获取视图
        View view;
        //自定义的内部类
        ViewHolder viewHolder;
        //要绑定的数据
        Info infoBean = getItem(position);

        if(convertView == null) {
            //布局文件加载器，获取加载器对象
            view = LayoutInflater.from(getContext()).inflate(resourceId, parent, false);
            viewHolder = new ViewHolder();
            //获取布局文件中的各种控件
            viewHolder.tv_name=view.findViewById(R.id.tv_name);
            viewHolder.time=view.findViewById(R.id.time);
            viewHolder.money =view.findViewById(R.id.money);
            view.setTag(viewHolder);//设置标签
        } else {
            view = convertView;
            viewHolder = (ViewHolder) view.getTag();
        }

        if(infoBean.getType()==1){//如果是收入+
            viewHolder.money.setText("+ "+infoBean.getMoney());
        }else {//如果是支出-
            viewHolder.money.setText("- "+infoBean.getMoney());
        }
        //给控件绑定数据
        viewHolder.tv_name.setText(infoBean.getName());
        viewHolder.time.setText(infoBean.getTime());
        return view;
    }
    /*
     * 自定义内部类，类似于缓存的作用
     * */
    private class ViewHolder {//设置取景器
        ImageView iv_name;
        TextView tv_name;
        TextView time;
        TextView money;
    }
}