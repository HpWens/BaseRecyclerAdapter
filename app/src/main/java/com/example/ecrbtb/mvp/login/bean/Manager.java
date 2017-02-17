package com.example.ecrbtb.mvp.login.bean;

import com.google.gson.annotations.Expose;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

/**
 * Created by boby on 2016/12/14.
 */
@Table(name = "Manager")
public class Manager {


    /**
     * ManagerId : 34
     * UserName : admin
     * Token : F882AE46711FCAE3F33574A68540FD65BFA17D128489A68C631FD0B874BDEBA5297FD80745EB7B8AA39F07AF726665E92581755AAB86D594B99C3C20B2B0D0C6298365092C3442D601EF250F7A82D5F78DC1C6063CF4D2495FD34AA2053615DFA336A4A46204276C7BF47D4A2F0BDA357C96B6AE65D303CB3E9A00969680BCF98CF27F1E49700DBA39085E4DB168CA2FF9F07ACCACBFD16A1ECDC0B3617DC28DB0913B8A6F4438485A4712962588B6924D48CAA8C336303D56C50072ACC2F32EF648FBF9DC9C62F270427666BF54E495DE30EF82C3A0ED896D271BA73096921FF5F1F90A19B15E06BCC6B1A45058021D
     * StoreId : 25
     * OpenId :
     */

    @Column(name = "id", isId = true)
    private int id;

    @Expose
    @Column(name = "ManagerId")
    public int ManagerId;

    @Expose
    @Column(name = "UserName")
    public String UserName;

    @Expose
    @Column(name = "Token")
    public String Token;

    @Expose
    @Column(name = "StoreId")
    public int StoreId;

    @Expose
    @Column(name = "OpenId")
    public String OpenId;
}
