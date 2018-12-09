package me.duras.korman.models;

import java.util.Date;

/**
 * Agent model
 */
public class Agent {

    private int id;
    private String name;
    private String email;
    private BicycleCategory category;
    private String series;
    private String size;
    private boolean wmn;

    /**
     * Min price in cents
     */
    private int minPrice;
    /**
     * Max price in cents
     */
    private int maxPrice;

    /**
     * Minimum price difference in cents
     */
    private int minDiff;

    private int modelYear;

    private Date lastCheck;
    private boolean active;

    public Agent() {}

    public Agent(String name, String email, BicycleCategory category, String series, String size, boolean wmn, int minPrice, int maxPrice, int minDiff, int modelYear) {
        this.name = name;
        this.email = email;
        this.category = category;
        this.series = series;
        this.size = size;
        this.wmn = wmn;
        this.minPrice = minPrice;
        this.maxPrice = maxPrice;
        this.minDiff = minDiff;
        this.modelYear = modelYear;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public BicycleCategory getCategory() {
        return category;
    }

    public void setCategory(BicycleCategory category) {
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

    public boolean getWmn() {
        return wmn;
    }

    public void setWmn(boolean wmn) {
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

    public int getMinDiff() {
        return minDiff;
    }

    public void setMinDiff(int minDiff) {
        this.minDiff = minDiff;
    }

    public int getModelYear() {
        return modelYear;
    }

    public void setModelYear(int modelYear) {
        this.modelYear = modelYear;
    }

    public Date getLastCheck() {
        return lastCheck;
    }

    public void setLastCheck(Date lastCheck) {
        this.lastCheck = lastCheck;
    }

    public boolean getActive() {
        return this.active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
