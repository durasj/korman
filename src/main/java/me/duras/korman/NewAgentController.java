/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package me.duras.korman;

/**
 *
 * @author mmiskov
 */
public class NewAgentController {

    private String category;
    private String series;
    private String size;
    private int wmn;
    private int minPrice;
    private int maxPrice;
    private int difference;
    private int year;

    //after click button Create Agent >> 
    AgentsDao agentsDao = new AgentsDao();

    public NewAgentController() {
        agentsDao.createAgent(category, series, size, wmn, minPrice, maxPrice, difference, year);
    }

}
