package me.duras.korman.models;

import java.util.ArrayList;
import java.util.List;

/**
 * DAO for Agents model
 */
public class AgentsDao {

    static List<Agent> list = new ArrayList<Agent>();

    // Create a new Agent in the DB
    public void createAgent(String category, String series, String size, int idAgenta, boolean wmn, int minPrice,
            int maxPrice, int difference, int year, String name, String timeStamp) {

        Agent agent = new Agent(category, series, size, idAgenta, wmn, minPrice, maxPrice, difference, year, name, timeStamp);
        list.add(agent);
    }

    // Edit an Agent in the DB
    public void editAgent(String category, String series, String size, int idAgenta, boolean wmn, int minPrice,
            int maxPrice, int difference, int year, String name, String timeStamp) {
        for (Agent agent : list) {
            if (agent.getIdAgenta() == idAgenta) {
                agent.setCategory(category);
                agent.setDifference(difference);
                agent.setMaxPrice(maxPrice);
                agent.setMinPrice(minPrice);
                agent.setSeries(series);
                agent.setSize(size);
                agent.setWmn(wmn);
                agent.setName(name);
                agent.setYear(year);
                agent.setTimeStamp(timeStamp);
            }
        }
    }

    // Delete Agent from the DB
    public void deleteAgent() {

    }

    // Get all Agents from the DB
    public List<Agent> getAllAgents() {
        return list;
    }

    // Get one Agent from the DB
    public Agent getAgent() {
        return list.get(0);
    }
}
