package me.duras.korman.dao;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import me.duras.korman.DaoFactory;
import me.duras.korman.models.Agent;
import me.duras.korman.models.BicycleCategory;

@DisplayName("SqliteAgentDao")
public class SqliteAgentDaoTest extends DaoTestCaseBase {
    private AgentDao dao;

    private BicycleCategory category = new BicycleCategory(1, "MTB", "", "");
    private BicycleCategory categoryRoad = new BicycleCategory(2, "Road", "", "");
    private Agent agent = new Agent("Unnamed", "email@email.com", category, "Exceed", "M", false, 1000, 3000, 0, 2018);

    @BeforeEach
    public void setupDao() {
        dao = DaoFactory.INSTANCE.getAgentDao();
        agent.setId(0);
        agent.setMinDiff(100);
    }

    @Test
    public void testGetAllSaveNew() {
        int oldSize = dao.getAll().size();

        dao.save(agent);
        List<Agent> list = dao.getAll();
        Optional<Agent> sameAgent = list.stream()
            .filter((savedAgent) -> agentsSame(agent, savedAgent))
            .findFirst();

        assertTrue(oldSize + 1 == list.size());
        assertTrue(sameAgent.isPresent());
    }

    @Test
    public void testGetByIdSaveNew() {
        Agent saved = dao.save(agent);
        Agent retrieved = dao.getById(saved.getId());

        assertTrue(agentsSame(saved, retrieved));
    }

    @Test
    public void testSaveExisting() {
        Agent original = dao.save(agent);
        Agent cloned = dao.getById(original.getId());
        cloned.setName("Something Else");
        cloned.setEmail("different@email.com");
        cloned.setCategory(categoryRoad);
        cloned.setSeries("Neuron");
        cloned.setSize("L");
        cloned.setWmn(true);
        cloned.setMinPrice(5000);
        cloned.setMaxPrice(10000);
        cloned.setMinDiff(300);
        cloned.setModelYear(2019);

        Agent saved = dao.save(cloned);

        assertTrue(agentsSame(cloned, saved));
    }

    @Test
    public void testDeleting() {
        int listSize = dao.getAll().size();

        dao.save(agent);
        Agent cloned = dao.getById(agent.getId());
        dao.save(cloned);

        dao.delete(agent);
        dao.delete(cloned.getId());

        int newListSize = dao.getAll().size();
        assertTrue(listSize == newListSize);
    }

    private boolean agentsSame(Agent first, Agent second) {
        if (!first.getName().equals(second.getName())) return false;
        if (!first.getEmail().equals(second.getEmail())) return false;
        if (first.getCategory().getId() != second.getCategory().getId()) return false;
        if (!first.getSeries().equals(second.getSeries())) return false;
        if (!first.getSize().equals(second.getSize())) return false;
        if (first.getWmn() != second.getWmn()) return false;
        if (first.getMinPrice() != second.getMinPrice()) return false;
        if (first.getMaxPrice() != second.getMaxPrice()) return false;
        if (first.getModelYear() != second.getModelYear()) return false;
        if (first.getMinDiff() != second.getMinDiff()) return false;

        return true;
    }
}
