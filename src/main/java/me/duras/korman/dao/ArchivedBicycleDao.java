package me.duras.korman.dao;

import java.util.List;

import me.duras.korman.models.ArchivedBicycle;

public interface ArchivedBicycleDao {
    List<ArchivedBicycle> getAll();

    ArchivedBicycle getById(int id);

    void saveMany(List<ArchivedBicycle> bicycles);
}
