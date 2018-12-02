package me.duras.korman.dao;

import java.util.List;

import me.duras.korman.models.Bicycle;

public interface BicycleDao {
    List<Bicycle> getAll();

    Bicycle getById(int id);

    Bicycle save(Bicycle bicycle);

    int delete(Bicycle bicycle);
}
