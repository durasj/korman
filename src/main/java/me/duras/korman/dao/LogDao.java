package me.duras.korman.dao;

import java.util.List;

import me.duras.korman.models.Log;

public interface LogDao {
    List<Log> getAll();

    Log save(Log log);

    int clear();
}
