package me.duras.korman.models;

import java.util.Date;

public class Notification {
    private int id;
    private Agent agent;
    private Bicycle bicycle;
    private Date createdAt;
    private boolean emailSent;

    public Notification() {}

    public Notification(Agent agent, Bicycle bicycle, Date createdAt, boolean emailSent) {
        this.agent = agent;
        this.bicycle = bicycle;
        this.createdAt = createdAt;
        this.emailSent = emailSent;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Agent getAgent() {
        return this.agent;
    }

    public void setAgent(Agent agent) {
        this.agent = agent;
    }

    public Bicycle getBicycle() {
        return this.bicycle;
    }

    public void setBicycle(Bicycle bicycle) {
        this.bicycle = bicycle;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public boolean getEmailSent() {
        return emailSent;
    }

    public void setEmailSent(boolean emailSent) {
        this.emailSent = emailSent;
    }
}
