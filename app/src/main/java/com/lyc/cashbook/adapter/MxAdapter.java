package com.lyc.cashbook.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.lyc.cashbook.R;
import com.lyc.cashbook.bean.Info;

import java.util.List;


//明细适配器
public class MxAdapter extends BaseQuickAdapter<Info, BaseViewHolder> {

    private Context context;

    public MxAdapter(Context context) {   //联系前后文
        super(R.layout.item_mx);
        this.context = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, Info item) {   //设置转换
        helper.setText(R.id.tv_name,item.getName());//设置类别
        helper.setText(R.id.time,item.getTime());//设置时间
        if(item.getType()==1){//如果是收入+
            helper.setText(R.id.money,"+ "+item.getMoney());
        }else {//如果是支出-
            helper.setText(R.id.money,"- "+item.getMoney());
        }

        helper.setImageResource(R.id.iv_name,item.getRes());//设置图标
    }
}
