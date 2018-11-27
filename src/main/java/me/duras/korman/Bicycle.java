package me.duras.korman;

import java.util.Date;
import javafx.scene.image.Image;

public class Bicycle {

    private String url;
    private String category;
    private String series;
    private String size;
    private int id;
    private int wmn;
    private int price;
    private int diff;
    private int year;
    private Date date;
    private Image photo;

    public Bicycle(String url, String category, String series, String Size, int id, int wmn, int price, int diff, int year, Date date, Image photo) {
        this.url = url;
        this.category = category;
        this.series = series;
        this.size = Size;
        this.id = id;
        this.wmn = wmn;
        this.price = price;
        this.diff = diff;
        this.year = year;
        this.date = date;
        this.photo = photo;
    }
}
