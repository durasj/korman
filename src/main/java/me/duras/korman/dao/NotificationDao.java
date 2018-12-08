package me.duras.korman.dao;

import java.util.List;

import me.duras.korman.models.Notification;

public interface NotificationDao {
    List<Notification> getAll();

    Notification getById(int id);

    Notification save(Notification notification);

    int delete(Notification notification);
}
