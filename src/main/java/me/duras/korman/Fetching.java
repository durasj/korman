package me.duras.korman;

import java.io.IOException;
import java.util.*;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * Fetches items from the websites
 */
public class Fetching {
    private String url;

    public Fetching (String url) {
        this.url = url;
    }

    public <Item> List<Item> fetchItems(String selectCssQuery, FetchingMap<Element, Item> mapping, String detailUrl) throws IOException {
        Document doc = Jsoup.connect(this.url).get();

        Elements elements = doc.select(selectCssQuery);
        List<Item> list = new ArrayList<Item>();
        for (Element element : elements) {
            list.add(mapping.map(element, detailUrl));
        }

        return list;
    }

    
}
