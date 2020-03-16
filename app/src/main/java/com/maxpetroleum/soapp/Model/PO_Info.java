package com.maxpetroleum.soapp.Model;

import java.io.Serializable;
import java.util.ArrayList;

public class PO_Info implements Serializable {
    private String po_no,amount,po_Date;
    private String delivery_date,bill_date,payment_date;
    private ArrayList<GradeInfo> grade;

    public PO_Info(String po_no, String amount, String po_Date, ArrayList<GradeInfo> grade) {
        this.po_no = po_no;
        this.amount = amount;
        this.po_Date = po_Date;
        this.grade = grade;
    }

    public PO_Info() {

    }

    public String getPo_no() {
        return po_no;
    }

    public String getAmount() {
        return amount;
    }

    public String getPo_Date() {
        return po_Date;
    }

    public String getDelivery_date() {
        return delivery_date;
    }

    public String getBill_date() {
        return bill_date;
    }

    public String getPayment_date() {
        return payment_date;
    }

    public ArrayList<GradeInfo> getGrade() {
        return grade;
    }

    public void setPo_no(String po_no) {
        this.po_no = po_no;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public void setPo_Date(String po_Date) {
        this.po_Date = po_Date;
    }

    public void setDelivery_date(String delivery_date) {
        this.delivery_date = delivery_date;
    }

    public void setBill_date(String bill_date) {
        this.bill_date = bill_date;
    }

    public void setPayment_date(String payment_date) {
        this.payment_date = payment_date;
    }

    public void setGrade(ArrayList<GradeInfo> grade) {
        this.grade = grade;
    }
}