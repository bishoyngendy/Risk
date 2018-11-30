package models;

import agents.Agent;

import java.util.Comparator;
import java.util.Objects;

/**
 * CS 482 : Artificial Intelligence.
 * Assignment 2: RISK
 *
 * @author Marc Magdi
 * Thursday, 22 November 2018
 */
public class VertexState implements Cloneable, Comparator<VertexState>, Comparable<VertexState> {
    private int armiesCount;
    private Agent agent;
    private Vertex vertex;

    public VertexState(Vertex vertex, Agent agent, int armiesCount) {
        this.armiesCount = armiesCount;
        this.vertex = vertex;
        this.agent = agent;
    }

    public Agent getAgent() {
        return agent;
    }

    public int getArmiesCount() {
        return armiesCount;
    }

    public void setArmiesCount(int armiesCount) {
        this.armiesCount = armiesCount;
    }

    public void increaseArmiesCount(int armiesCount) {
        this.armiesCount += armiesCount;
    }

    public void decreaseArmiesCount(int armiesCount) {
        this.armiesCount -= armiesCount;
    }

    public int getId() {
        return this.vertex.getId();
    }

    public Partition getPartition() {
        return this.vertex.getPartition();
    }

    public void setAgent(Agent agent) {
        this.agent = agent;
    }

    public Vertex getVertex() {
        return vertex;
    }

    @Override
    public int compare(VertexState o1, VertexState o2) {
        if (o1.armiesCount < o2.armiesCount) return -1;
        else if (o1.armiesCount > o2.armiesCount) return 1;
        return o1.getId() > o2.getId() ? 1 : -1;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        VertexState vertexState = (VertexState) o;
        return Objects.equals(this.vertex.getId(), vertexState.vertex.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.vertex.getId());
    }

    public VertexState clone() {
        VertexState vertexState = new VertexState(vertex, agent, armiesCount);
        return vertexState;
    }

    @Override
    public int compareTo(VertexState o) {
        return compare(this, o);
    }
}
