package me.duras.korman.models;

import java.util.Date;

public class ArchivedBicycle extends Bicycle {
    private Date archivedAt;

    public ArchivedBicycle(String externalId, BicycleCategory category, String series, String size, boolean wmn, int price,
            int modelYear, String url, String photoUrl, Date createdAt) {
        super(externalId, category, series, size, wmn, price, modelYear, url, photoUrl, createdAt);
    }

    public Date getArchivedAt() {
        return archivedAt;
    }

    public void setArchivedAt(Date archivedAt) {
        this.archivedAt = archivedAt;
    }
}
