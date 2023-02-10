package com.yang.tally.frag_record;

import com.yang.tally.R;
import com.yang.tally.db.DBManager;
import com.yang.tally.db.TypeBean;

import java.util.List;

public class IncomeFragment extends BaseRecordFragment{
    @Override
    public void loadDataToGv() {
        super.loadDataToGv();
        //获取数据库当中的数据源
        List<TypeBean> inList = DBManager.getTypeList(1);
        typeList.addAll(inList);
        adapter.notifyDataSetChanged();
        typeTv.setText("其他");
        typeIv.setImageResource(R.mipmap.in_qt_fs);
    }

    @Override
    public void saveAccountToDB() {

        accountBean.setKind(1);
        DBManager.insertItemToAccounttb(accountBean);
    }
}
