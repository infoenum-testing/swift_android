package com.swiftdating.app.common;

public class InAppPriceValue {
    private String priceTxt;
    private Double priceValue;

    public String getPriceTxt() {
        return priceTxt;
    }

    public void setPriceTxt(String priceTxt) {
        this.priceTxt = priceTxt;
    }

    public Double getPriceValue() {
        return priceValue;
    }

    public void setPriceValue(Double priceValue) {
        this.priceValue = priceValue;
    }

    public InAppPriceValue(String priceTxt, Double priceValue) {
        this.priceTxt = priceTxt;
        this.priceValue = priceValue;
    }

    @Override
    public String toString() {
        return "InAppPriceValue{" +
                "priceTxt='" + priceTxt + '\'' +
                ", priceValue=" + priceValue +
                '}';
    }
}
