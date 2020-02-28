package com.maxpetroleum.soapp.Model;

import java.io.Serializable;

public class GradeInfo implements Serializable {
    private String grade_name,qnty,rate;

    public GradeInfo(String grade_name, String qnty, String rate) {
        this.grade_name = grade_name;
        this.qnty = qnty;
        this.rate = rate;
    }

    public GradeInfo() {
    }

    public String getGrade_name() {
        return grade_name;
    }

    public String getQnty() {
        return qnty;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }
}
