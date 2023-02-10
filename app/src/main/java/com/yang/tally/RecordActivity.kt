package com.yang.tally;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.view.View;

import com.google.android.material.tabs.TabLayout;
import com.yang.tally.adapter.RecordPaperAdapter;
import com.yang.tally.frag_record.IncomeFragment;
import com.yang.tally.frag_record.OutcomeFragment;
import com.yang.tally.frag_record.BaseRecordFragment;

import java.util.ArrayList;
import java.util.List;

public class RecordActivity extends AppCompatActivity {

    TabLayout tabLayout;
    ViewPager viewPager;
    private RecordPaperAdapter paperAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);
        //1.查找控件
        tabLayout = findViewById(R.id.record_tabs);
        viewPager = findViewById(R.id.record_vp);

        //设置viewpaper加载页面
        initPaper();
    }

    private void initPaper() {
        //初始化ViewPaper页面的集合
        List<Fragment>fragmentList = new ArrayList<>();
        //创建收入和支出页面，放置在fragment当中
        OutcomeFragment outFrag = new OutcomeFragment();
        IncomeFragment inFrag = new IncomeFragment();
        fragmentList.add(outFrag);
        fragmentList.add(inFrag);
        //创建适配器
        RecordPaperAdapter paperAdapter = new RecordPaperAdapter(getSupportFragmentManager(), fragmentList);
    //设置适配器
        viewPager.setAdapter(paperAdapter);
        //将tablayout和viewpager关联
        tabLayout.setupWithViewPager(viewPager);

    }

    /*点击事件*/
    public void onClick(View view){
        switch (view.getId()) {
            case R.id.record_iv_back:
                finish();
                break;
        }
    }
}