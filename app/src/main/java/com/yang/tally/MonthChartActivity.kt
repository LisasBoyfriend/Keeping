package com.yang.tally;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.yang.tally.adapter.ChartVPAdapter;
import com.yang.tally.db.DBManager;
import com.yang.tally.frag_chart.IncomeChartFragment;
import com.yang.tally.frag_chart.OutcomeChartFragment;
import com.yang.tally.utils.CalendarDialog;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MonthChartActivity extends AppCompatActivity {

    Button inBtn,outBtn;
    TextView dateTv,inTv,outTv;
    ViewPager chartVp;
    private int year;
    private int month;

    int selectPos = -1,selectMonth = -1;
    List<Fragment>chartFragList;
    private IncomeChartFragment incomeChartFragment;
    private OutcomeChartFragment outcomeChartFragment;
    private ChartVPAdapter chartVPAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_month_chart);
        initView();
        initTime();
        initStatistics(year,month);
        initFrag();
        setVPSelectListener();
    }

    private void setVPSelectListener() {
        chartVp.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener(){
            @Override
            public void onPageSelected(int position) {
                setButtonStyle(position);
            }
        });
    }

    private void initFrag() {
        chartFragList = new ArrayList<>();
        //添加fragment对象
        incomeChartFragment = new IncomeChartFragment();
        outcomeChartFragment = new OutcomeChartFragment();
        //添加数据到fragment当中
        Bundle bundle = new Bundle();
        bundle.putInt("year",year);
        bundle.putInt("month",month);
        incomeChartFragment.setArguments(bundle);
        outcomeChartFragment.setArguments(bundle);
        //将fragment添加到数据源当中
        chartFragList.add(outcomeChartFragment);
        chartFragList.add(incomeChartFragment);
        //使用适配器

        chartVPAdapter = new ChartVPAdapter(getSupportFragmentManager(),chartFragList);
        chartVp.setAdapter(chartVPAdapter);
        //将fragment加载到activity 当中


    }

    /**统计某年某月的收支情况数据
     * @param year
     * @param month
     */
    private void initStatistics(int year, int month) {

        float inMoneyOneMonth = DBManager.getSumMoneyOneMonth(year, month, 1);//收入总钱数
        float outMoneyOneMonth = DBManager.getSumMoneyOneMonth(year, month, 0);//支出总钱数
        int incountItemOneMonth = DBManager.getCountItemOneMonth(year, month, 1);//收入多少笔
        int outcountItemOneMonth = DBManager.getCountItemOneMonth(year, month, 0);//支出多少笔
        dateTv.setText(year+"年"+month+"月账单");
        inTv.setText("共"+incountItemOneMonth+"笔收入, ￥ "+inMoneyOneMonth);
        outTv.setText("共"+outcountItemOneMonth+"笔支出, ￥ "+outMoneyOneMonth);

    }

    /**
     * 初始化时间的方法
     */
    private void initTime() {
        Calendar calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH)+1;
    }


    private void initView() {
        inBtn = findViewById(R.id.chart_btn_in);
        outBtn = findViewById(R.id.chart_btn_out);
        dateTv = findViewById(R.id.chart_tv_date);
        inTv = findViewById(R.id.chart_tv_in);
        outTv = findViewById(R.id.chart_tv_out);
        chartVp = findViewById(R.id.chart_vp);
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.chart_iv_back:
                finish();
                break;
            case R.id.chart_iv_rili:
                showCalendarDialog();
                break;
            case R.id.chart_btn_in:
                setButtonStyle(1);
                chartVp.setCurrentItem(1);
                break;
            case R.id.chart_btn_out:
                setButtonStyle(0);
                chartVp.setCurrentItem(0);

                break;

        }
    }

    /**
     * 显示日历的对话框
     */
    private void showCalendarDialog() {
        CalendarDialog dialog = new CalendarDialog(this, selectPos, selectMonth);
        dialog.show();
        dialog.setDialogSize();
        dialog.setOnRefreshListener(new CalendarDialog.OnRefreshListener() {
            @Override
            public void onRefresh(int selPos, int year, int month) {
                MonthChartActivity.this.selectPos = selPos;
                MonthChartActivity.this.selectMonth = month;
                initStatistics(year,month);
                incomeChartFragment.setDate(year,month);
                outcomeChartFragment.setDate(year,month);
            }
        });

    }

    /**
     * 设置按钮样式的改变 支出-0，收入-1
     */
    private void setButtonStyle(int kind){
        if (kind == 0) {
            outBtn.setBackgroundResource(R.drawable.main_recordbtn_bg);
            outBtn.setTextColor(Color.WHITE);
            inBtn.setBackgroundResource(R.drawable.dialog_btn_bg);
            inBtn.setTextColor(Color.BLACK);
        }else if(kind == 1){
            inBtn.setBackgroundResource(R.drawable.main_recordbtn_bg);
            inBtn.setTextColor(Color.WHITE);
            outBtn.setBackgroundResource(R.drawable.dialog_btn_bg);
            outBtn.setTextColor(Color.BLACK);
        }

    }
}