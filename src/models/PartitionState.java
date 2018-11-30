package models;

import agents.Agent;

import java.util.*;

/**
 * CS 482 : Artificial Intelligence.
 * Assignment 2: RISK
 *
 * @author Marc Magdi
 * Thursday, 22 November 2018
 */
public class PartitionState implements Cloneable {

    private Partition partition;
    private Map<Agent, Set<VertexState>> vertices;
    private Agent firstAgent;
    private Agent secondAgent;

    public PartitionState(Agent firstAgent, Agent secondAgent, Partition partition) {
        this.vertices = new HashMap<>();
        this.vertices.put(firstAgent, new HashSet<>());
        this.vertices.put(secondAgent, new HashSet<>());
        this.partition = partition;
        this.firstAgent = firstAgent;
        this.secondAgent = secondAgent;
    }

    public int getId() {
        return partition.getId();
    }

    public Partition getPartition() {
        return partition;
    }

    public void addVertexState(Agent agent, VertexState vertexState) {
        this.vertices.get(agent).add(vertexState);
    }

    public Set<VertexState> getAgentPartitionVertices(Agent agent) {
        return this.vertices.get(agent);
    }

    public void moveVertexToOpponent(VertexState vertexState) {
        if (this.vertices.get(firstAgent).remove(vertexState)) {
            this.vertices.get(secondAgent).add(vertexState);
        } else {
            this.vertices.get(secondAgent).remove(vertexState);
            this.vertices.get(firstAgent).add(vertexState);
        }
    }

    public Map<Agent, Set<VertexState>> getVertices() {
        return vertices;
    }

    public Set<VertexState> getAgentVerticesCount(Agent agent) {
        return this.vertices.get(agent);
    }

    public PartitionState clone() {
        PartitionState partitionState = new PartitionState(firstAgent, secondAgent, partition);

        return partitionState;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PartitionState that = (PartitionState) o;
        return this.partition.getId() == that.partition.getId();
    }

    @Override
    public int hashCode() {
        return Objects.hash(partition.getId());
    }
}
