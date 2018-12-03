package me.duras.korman.models;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.jsoup.nodes.Element;

import me.duras.korman.FetchingMap;

public class Bicycle {

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

    public static final FetchingMap<Element, Bicycle> fetchMap = (Element el, String detailUrl) -> {
        String externalId = el.attr("data-id");
        String series = el.attr("data-series");
        String size = el.attr("data-size").replaceAll("\\|", "");
        boolean wmn = el.attr("data-wmn") == "1";
        int price = Integer.parseInt(el.attr("data-price"));
        int year = Integer.parseInt(el.attr("data-year"));
        int diff = Integer.parseInt(el.attr("data-diff"));
        String url = detailUrl + externalId;
        String photoUrl = null;
        try {
            photoUrl = el.selectFirst("img").attr("data-srcset").split(" ")[0];
        } catch (Exception e) {
            // Silently ignore, photoUrl is not neccessary
        }

        // Will be filled after the fetching
        BicycleCategory category = null;

        Date created = null;
        try {
            created = (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).parse(el.attr("data-date"));
        } catch (ParseException e) {
            System.err.println("Incorrect bicycle date");
        }

        Bicycle bike = new Bicycle(externalId, category, series, size, wmn, price, year, url, photoUrl, created);
        bike.setDiff(diff);

        return bike;
    };

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
