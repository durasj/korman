package me.duras.korman.models;

import java.util.Date;

public class Bicycle {

    private int id;
    private int externalId;
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

    public Bicycle() {

    }
}
