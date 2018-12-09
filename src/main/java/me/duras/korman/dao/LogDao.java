package me.duras.korman.dao;

import java.util.Date;
import java.util.List;

import me.duras.korman.models.Log;

public interface LogDao {
    List<Log> getAll();

    List<Log> getNew(Date from);

    Log save(Log log);

    int clear();
}
