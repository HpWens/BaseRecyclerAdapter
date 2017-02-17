package com.example.ecrbtb.mvp.category.bean;

import com.chad.library.adapter.base.entity.AbstractExpandableItem;
import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.example.ecrbtb.mvp.category.adapter.CategoryTypeAdapter;

/**
 * Created by boby on 2016/12/15.
 */

public class Column1 extends AbstractExpandableItem<Column2> implements MultiItemEntity {

    public String  name;
    public int id;


    @Override
    public int getLevel() {
        return 1;
    }

    @Override
    public int getItemType() {
        return CategoryTypeAdapter.TYPE_COLUMN_1;
    }
}
