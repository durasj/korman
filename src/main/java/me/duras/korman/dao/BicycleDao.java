package me.duras.korman.dao;

import java.util.List;

import me.duras.korman.models.Bicycle;

public interface BicycleDao {
    List<Bicycle> getAll();

    Bicycle getById(int id);

    Bicycle getByExternalId(String id);

    Bicycle save(Bicycle bicycle);

    void saveMany(List<Bicycle> bicycles);

    int delete(Bicycle bicycle);

    int deleteManyByExternalId(List<String> externalIds);
}
