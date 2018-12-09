package me.duras.korman.models;

public class BicycleCategory {

    private int id;
    private String name;
    private String externalUrl;

    public BicycleCategory() {}

    public BicycleCategory(int id, String name, String externalUrl) {
        this.id = id;
        this.name = name;
        this.externalUrl = externalUrl;
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
}
