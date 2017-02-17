package com.example.ecrbtb.event;

import com.chad.library.adapter.base.entity.MultiItemEntity;

import java.util.List;

/**
 * Created by boby on 2016/12/16.
 */

public class RequestDataEvent {

    public String message;
    //0表示失败   1表示成功
    public int code;

    public List<MultiItemEntity> lists;

}
