package com.yang.tally.frag_chart;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.yang.tally.R;
import com.yang.tally.adapter.ChartItemAdapter;
import com.yang.tally.db.ChartItemBean;
import com.yang.tally.db.DBManager;

import java.util.ArrayList;
import java.util.List;


public abstract class BaseChartFragment extends Fragment {
    ListView chartLv;
    int year;
    int month;
    List<ChartItemBean>mDatas;
    private ChartItemAdapter itemAdapter;
    BarChart barChart;//柱状图控件
    TextView chartTv;//如果没有收支情况，显示的文本控件


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_income_chart, container, false);

        chartLv = view.findViewById(R.id.frag_chart_lv);
        //获取activity传递的数据
        Bundle bundle = getArguments();
        year = bundle.getInt("year");
        month = bundle.getInt("month");
        //设置数据源
        mDatas = new ArrayList<>();
        //设置适配器
        itemAdapter = new ChartItemAdapter(getContext(), mDatas);
        chartLv.setAdapter(itemAdapter);
//添加头布局
        addLVHeaderView();
        return view;
    }

    protected  void addLVHeaderView(){
        //将布局转化成View对象
        View headerView = getLayoutInflater().inflate(R.layout.item_chartfrag_top,null);
        //将View添加到ListView的头布局上
        chartLv.addHeaderView(headerView);
        //查找头布局当中包含的控件
        barChart = headerView.findViewById(R.id.item_chartfrag_chart);
        chartTv = headerView.findViewById(R.id.item_chartfrag_top_tv);
        //设定柱状图不显示描述
        barChart.getDescription().setEnabled(false);
        //设置柱状图的内边距
        barChart.setExtraOffsets(20,20,20,20);
        setAxis(year,month);//设置坐标轴
        //设置坐标轴显示的数据
        setAxisData(year,month);

    }

    /**
     * 设置坐标轴显示的数据
     * @param year
     * @param month
     */
    protected abstract void setAxisData(int year, int month);


    /**
     * 用来设置柱状图坐标轴的显示：方法必须重写
     * @param year
     * @param month
     */
    protected  void setAxis(int year, final int month){

        //设置X轴
        XAxis xAxis = barChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);//设置X轴显示在下方
        xAxis.setDrawGridLines(true);//设置绘制该轴的网格线
        //设置X轴标签的个数
        xAxis.setLabelCount(31);
        xAxis.setTextSize(12f);//X轴标签大小
        //设置X轴显示的值的格式
        xAxis.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                int val = (int)value;
                if (val == 0) {
                    return month+"-1";
                }

                if (val == 14){
                    return month+"-15";
                }

                //根据不同的月份显示最后一天的位置
                if (month == 2){
                    if (val == 27){
                        return month+"-28";
                    }
                }else if(month == 1 || month == 3||month==5||month==7||month==8||month==10||month==12){
                    if (val == 30){
                        return month+"-31";
                    }
                }else if(month == 4||month==6||month==9||month==11){
                    if (val==29){
                        return month+"-30";
                    }
                }
                return "";

            }
        });
        xAxis.setYOffset(10);//设置标签对X轴的偏移量，垂直方向
        //Y轴在子类的设置
        setYAxis(year,month);
    }

    /**
     * 设置Y轴，因为最高的坐标不确定，所以在子类中设置
     */
    protected abstract void setYAxis(int year,int month);

    public void setDate(int year,int month){
        this.year = year;
        this.month = month;
    }

    public void loadData(int year, int month,int kind) {

        List<ChartItemBean> list = DBManager.getChastListFromAccounttb(year, month, kind);
        mDatas.clear();
        mDatas.addAll(list);
        itemAdapter.notifyDataSetChanged();
    }
}