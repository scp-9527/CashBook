package com.lyc.cashbook.bean;

import org.litepal.crud.LitePalSupport;


//用户表
public class User  extends LitePalSupport {
    long id;//id
    String username;//账号
    String password; //密码
    String monthBudget;//月预算
    String yearBudget;//年预算

    public String getMonthBudget() {
        return monthBudget;
    }

    public void setMonthBudget(String monthBudget) {
        this.monthBudget = monthBudget;
    }

    public String getYearBudget() {
        return yearBudget;
    }

    public void setYearBudget(String yearBudget) {
        this.yearBudget = yearBudget;
    }


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
