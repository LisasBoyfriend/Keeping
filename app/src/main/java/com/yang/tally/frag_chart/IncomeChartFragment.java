package com.yang.tally.frag_chart;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.yang.tally.R;
import com.yang.tally.adapter.ChartItemAdapter;
import com.yang.tally.db.ChartItemBean;
import com.yang.tally.db.DBManager;

import java.util.ArrayList;
import java.util.List;


public class IncomeChartFragment extends BaseChartFragment {
    int kind = 1;
    @Override
    public void onResume() {
        super.onResume();
        loadData(year,month,kind);
    }

    @Override
    public void setDate(int year, int month) {
        super.setDate(year, month);
        loadData(year,month,kind);
    }
}