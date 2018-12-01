/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package me.duras.korman;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author mmiskov
 */
public class AgentsDao {

    static List<Agent> zoznam = new ArrayList<Agent>();

    public void createAgent(String category, String series, String size, int idAgenta, String wmn, int minPrice, int maxPrice, int difference, int year) {
        //vytvori Agenta
        Agent agent = new Agent(category, series, size, idAgenta, wmn, minPrice, maxPrice, difference, year);
        //potom prida do DB
        zoznam.add(agent);
    }

    //vymaze v databaze, vola sa v Agents
    public void deleteAgent() {

    }

    //ziska z databazy, vypise v Agents
    public List<Agent> getAllAgents() {
        return zoznam;
    }

    public Agent getAgent() {
        return zoznam.get(0);
    }
}
