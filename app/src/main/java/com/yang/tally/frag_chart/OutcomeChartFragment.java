package com.yang.tally.frag_chart;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.yang.tally.R;


public class OutcomeChartFragment extends BaseChartFragment {
    int kind = 0;
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