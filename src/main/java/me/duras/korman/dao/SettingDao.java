package me.duras.korman.dao;

import java.util.List;

import me.duras.korman.models.Setting;

public interface SettingDao {
    List<Setting> getAll();

    Setting getByKey(String key);

    int save(Setting setting);
}
