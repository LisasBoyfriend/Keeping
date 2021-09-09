package com.yang.tally.frag_chart;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.fragment.app.Fragment;

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

        return view;
    }


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