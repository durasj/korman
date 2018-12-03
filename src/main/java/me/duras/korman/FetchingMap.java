package me.duras.korman;

public interface FetchingMap<Element, Item> {
    Item map(Element input, String url);
}
