package me.duras.korman.dao;

import java.util.List;

import me.duras.korman.models.Notification;

public interface NotificationDao {
    List<Notification> getAll();

    Notification getById(int id);

    Notification save(Notification notification);

    List<Notification> saveMany(List<Notification> notifications);

    int delete(Notification notification);

    int delete(int notificationId);
}
