package com.yang.tally.adapter;

import android.content.Context;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.yang.tally.R;
import com.yang.tally.db.ChartItemBean;
import com.yang.tally.utils.FloatUtils;

import java.util.List;

public class ChartItemAdapter extends BaseAdapter {
    LayoutInflater inflater;
    /**
     * 账单详情页面ListView的适配器
     * @return
     */
    Context context;
    List<ChartItemBean> mDatas;

    public ChartItemAdapter(Context context, List<ChartItemBean> mDatas) {
        this.context = context;
        this.mDatas = mDatas;
        inflater = LayoutInflater.from(context);
    }


    @Override
    public int getCount() {
        return mDatas.size();
    }

    @Override
    public Object getItem(int position) {
        return mDatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null){
            convertView = inflater.inflate(R.layout.item_chartfrag_lv,parent,false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder)convertView.getTag();
        }
        //获取显示内容
        ChartItemBean bean = mDatas.get(position);
        holder.iv.setImageResource(bean.getsImageId());
        holder.typeTv.setText(bean.getType());
        holder.totalTv.setText("￥ "+bean.getTotalMoney());

        float radio = bean.getRadio();
        String pert = FloatUtils.ratioToPercent(radio);
        holder.ratioTv.setText(pert);

        return convertView;

    }
    class ViewHolder{
        TextView typeTv,ratioTv,totalTv;
        ImageView iv;
        public ViewHolder(View view){
            typeTv = view.findViewById(R.id.item_chartfrag_tv_type);
            ratioTv = view.findViewById(R.id.item_chartfrag_tv_pert);
            totalTv = view.findViewById(R.id.item_chartfrag_tv_sum);
            iv = view.findViewById(R.id.item_chartfrag_iv);
        }
    }
}
