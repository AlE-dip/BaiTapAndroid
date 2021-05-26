package com.example.nationinfo;

import androidx.annotation.Nullable;

public class Country implements Comparable<Country>{
    int Index;
    String IDcountry;
    String DienTich;
    String Ten;
    String DanSo;

    public Country(int Index, String IDcountry, String DienTich, String Ten, String DanSo) {
        this.Index = Index;
        this.IDcountry = IDcountry;
        this.DienTich = DienTich;
        this.Ten = Ten;
        this.DanSo = DanSo;
    }


    public String getIDcountry() {
        return IDcountry;
    }

    public String getDienTich() {
        return DienTich;
    }

    public String getTen() {
        return Ten;
    }

    public String getDanSo() {
        return DanSo;
    }


    public void setIDcountry(String IDcountry) {
        this.IDcountry = IDcountry;
    }

    public void setDienTich(String DienTich) {
        this.DienTich = DienTich;
    }

    public void setTen(String Ten) {
        this.Ten = Ten;
    }

    public void setDanSo(String DanSo) {
        this.DanSo = DanSo;
    }

    public int getIndex() {
        return Index;
    }

    public void setIndex(int index) {
        Index = index;
    }

    public int compareTo(Country obj) {
        return this.Ten.compareTo(obj.Ten);
    }
}

