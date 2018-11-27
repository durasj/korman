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
public class AgentsDao {

    public void createAgent(String category, String series, String size, int wmn, int minPrice, int maxPrice, int difference, int year) {
        Agent agent = new Agent(category, series, size, wmn, minPrice, maxPrice, difference, year);
    }
}
