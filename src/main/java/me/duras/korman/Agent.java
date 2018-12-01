/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package me.duras.korman;

/**
 *
 * @author mmiskov
 */
public class Agent {

    private String category;
    private String series;
    private String size;
    private int idAgenta;
    private String wmn;
    private int minPrice;
    private int maxPrice;
    private int difference;
    private int year;

    public Agent(String category, String series, String size, int idAgenta, String wmn, int minPrice, int maxPrice, int difference, int year) {
        this.category = category;
        this.series = series;
        this.size = size;
        this.idAgenta = idAgenta;
        this.wmn = wmn;
        this.minPrice = minPrice;
        this.maxPrice = maxPrice;
        this.difference = difference;
        this.year = year;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getSeries() {
        return series;
    }

    public void setSeries(String series) {
        this.series = series;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public int getIdAgenta() {
        return idAgenta;
    }

    public void setIdAgenta(int idAgenta) {
        this.idAgenta = idAgenta;
    }

    public String getWmn() {
        return wmn;
    }

    public void setWmn(String wmn) {
        this.wmn = wmn;
    }

    public int getMinPrice() {
        return minPrice;
    }

    public void setMinPrice(int minPrice) {
        this.minPrice = minPrice;
    }

    public int getMaxPrice() {
        return maxPrice;
    }

    public void setMaxPrice(int maxPrice) {
        this.maxPrice = maxPrice;
    }

    public int getDifference() {
        return difference;
    }

    public void setDifference(int difference) {
        this.difference = difference;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

}
