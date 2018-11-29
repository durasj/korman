package me.duras.korman;

public class Agents {

    public static void showAgent() {
        AgentsDao dao = new AgentsDao();

        for (Agent agent : dao.getAllAgents()) {
            System.out.println("Agent maxPrice: " + agent.getMaxPrice() + ", Size: " + agent.getSize());
        }
    }
}
