package com.example.ecrbtb.mvp.goods.bean;

import com.google.gson.annotations.Expose;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

/**
 * Created by boby on 2016/12/19.
 */
@Table(name = "Specs")
public class Specs {


    /**
     * Id : 99
     * SpecId : 24
     * SpecName : 容量
     * ShowType : 文字
     * SpecValue_Text : 50mL
     */

    @Column(name = "id", isId = true)
    private int id;

    @Column(name = "ProductId")
    public int ProductId;

    @Column(name = "GoodsId")
    public int GoodsId;

    @Expose
    @Column(name = "ValueId")
    public String Id;

    @Expose
    @Column(name = "SpecId")
    public String SpecId;

    @Expose
    @Column(name = "SpecName")
    public String SpecName;

    @Expose
    @Column(name = "ShowType")
    public String ShowType;

    @Expose
    @Column(name = "SpecValue_Text")
    public String SpecValue_Text;


}
