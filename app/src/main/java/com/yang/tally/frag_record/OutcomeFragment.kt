package com.yang.tally.frag_record;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.yang.tally.R;
import com.yang.tally.db.DBManager;
import com.yang.tally.db.TypeBean;

import java.util.List;

/**
 *
 */
public class OutcomeFragment extends BaseRecordFragment {

    //重写
    @Override
    public void loadDataToGv() {
        super.loadDataToGv();
        //获取数据库当中的数据源
        List<TypeBean> outList = DBManager.getTypeList(0);
        typeList.addAll(outList);
        adapter.notifyDataSetChanged();
        typeTv.setText("其他");
        typeIv.setImageResource(R.mipmap.ic_qita_fs);
    }

    @Override
    public void saveAccountToDB() {

        accountBean.setKind(0);
        DBManager.insertItemToAccounttb(accountBean);
    }
}