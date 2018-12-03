package me.duras.korman.dao;

import java.util.List;

import me.duras.korman.models.BicycleCategory;;

public interface BicycleCategoryDao {
    List<BicycleCategory> getAll();

    BicycleCategory getById(int id);
}
