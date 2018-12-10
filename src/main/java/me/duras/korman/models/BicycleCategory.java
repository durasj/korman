package me.duras.korman.models;

public class BicycleCategory {

    private int id;
    private String name;
    private String externalUrl;
    private String externalDetailUrl;

    public BicycleCategory() {}

    public BicycleCategory(int id, String name, String externalUrl, String externalDetailUrl) {
        this.id = id;
        this.name = name;
        this.externalUrl = externalUrl;
        this.externalDetailUrl = externalDetailUrl;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getExternalUrl() {
        return externalUrl;
    }

    public void setExternalUrl(String externalUrl) {
        this.externalUrl = externalUrl;
    }

    public String getExternalDetailUrl() {
        return externalDetailUrl;
    }

    public void setExternalDetailUrl(String externalDetailUrl) {
        this.externalDetailUrl = externalDetailUrl;
    }
}
