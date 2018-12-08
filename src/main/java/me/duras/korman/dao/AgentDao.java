package me.duras.korman.dao;

import java.util.List;

import me.duras.korman.models.Agent;

public interface AgentDao {
    List<Agent> getAll();

    Agent getById(int id);

    Agent save(Agent agent);

    int delete(Agent agent);
}
