package me.duras.korman.models;

import java.util.Date;

public class Bicycle {

    final static public String[] sizes = { "2XS", "XS", "S", "M", "L", "XL", "2XL" };

    private int id;
    private String externalId;
    private BicycleCategory category;
    private String series;
    private String size;
    private boolean wmn;

    /**
     * Price in cents
     */
    private int price;

    /**
     * Price difference in cents
     */
    private int diff;

    private int modelYear;
    private String url;
    private String photoUrl;

    private Date createdAt;
    private Date importedAt;

    public Bicycle() {}

    public Bicycle(String externalId, BicycleCategory category, String series, String size, boolean wmn, int price,
            int modelYear, String url, String photoUrl, Date createdAt) {
        this.externalId = externalId;
        this.category = category;
        this.series = series;
        this.size = size;
        this.wmn = wmn;
        this.price = price;
        this.modelYear = modelYear;
        this.url = url;
        this.createdAt = createdAt;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getExternalId() {
        return externalId;
    }

    public void setExternalId(String externalId) {
        this.externalId = externalId;
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

    public boolean isWmn() {
        return wmn;
    }

    public void setWmn(boolean wmn) {
        this.wmn = wmn;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getDiff() {
        return diff;
    }

    public void setDiff(int diff) {
        this.diff = diff;
    }

    public int getModelYear() {
        return modelYear;
    }

    public void setModelYear(int modelYear) {
        this.modelYear = modelYear;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getImportedAt() {
        return importedAt;
    }

    public void setImportedAt(Date importedAt) {
        this.importedAt = importedAt;
    }
}
