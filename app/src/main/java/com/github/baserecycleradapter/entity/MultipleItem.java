package com.github.baserecycleradapter.entity;

import com.github.library.entity.MultiItemEntity;

/**
 * Created by Administrator on 8/3 0003.
 */
public class MultipleItem implements MultiItemEntity {

    public static final int HEADER = 1;
    public static final int CONTENT = 2;

    public String name;

    public int itemType;

    @Override
    public int getItemType() {
        return itemType;
    }
}
