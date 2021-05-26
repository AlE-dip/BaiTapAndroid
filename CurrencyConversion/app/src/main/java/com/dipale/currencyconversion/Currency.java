package com.dipale.currencyconversion;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.util.Objects;

public class Currency implements Comparable<Currency> {
    private String country;
    private String currencyCode;
    private String exchangeRate;

    public Currency(String country, String currencyCode, String exchangeRate) {
        this.country = country;
        this.currencyCode = currencyCode;
        this.exchangeRate = exchangeRate;
    }

    public Currency() {
        this.country = "";
        this.currencyCode = "";
        this.exchangeRate = "";
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public String getExchangeRate() {
        return exchangeRate;
    }

    public void setExchangeRate(String exchangeRate) {
        this.exchangeRate = exchangeRate;
    }

    @Override
    public int compareTo(Currency o) {
        return country.compareTo(o.getCountry());
    }
}
