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
public class BoardState {
    private Map<Agent, Set<VertexState>> vertices;
    private List<PartitionState> partitionStates;
    private Map<Integer, VertexState> allVertices;
    private Agent firstAgent;
    private Agent secondAgent;

    public BoardState(Agent firstAgent, Agent secondAgent) {
        this.partitionStates = new ArrayList<>();
        this.allVertices = new HashMap<>();
        this.firstAgent = firstAgent;
        this.secondAgent = secondAgent;
        this.vertices = new HashMap<>();
        this.vertices.put(firstAgent, new HashSet<>());
        this.vertices.put(secondAgent, new HashSet<>());
    }

    public VertexState addVertex(Agent agent, Vertex vertex, int armiesCount) {
        VertexState vertexState = new VertexState(vertex, agent, armiesCount);
        this.allVertices.put(vertex.getId(), vertexState);
        this.vertices.get(agent).add(vertexState);
        return vertexState;
    }

    public Set<VertexState> getFirstPlayerCountries() {
        return this.vertices.get(firstAgent);
    }

    public Set<VertexState> getSecondPlayerCountries() {
        return this.vertices.get(secondAgent);
    }

    public Set<VertexState> getPlayerCountries(Agent agent) {return this.vertices.get(agent);}

    public Map<Integer, VertexState> getVertices() {
        return allVertices;
    }

    public List<PartitionState> getPartitionStates() {
        return partitionStates;
    }

    public void setPartitionStates(List<PartitionState> partitionStates) {
        this.partitionStates = partitionStates;
    }

    public void addPartition(int id, Partition partition) {
        partitionStates.add(new PartitionState(firstAgent, secondAgent, partition));
    }

    public void moveVertexToOpponent(VertexState vertexState) {
        if (vertexState.getAgent() == firstAgent) {
            this.vertices.get(firstAgent).remove(vertexState);
            this.vertices.get(secondAgent).add(vertexState);
            this.partitionStates.get(vertexState.getPartition().getId()).getVertices().get(firstAgent).remove(vertexState);
            this.partitionStates.get(vertexState.getPartition().getId()).getVertices().get(secondAgent).add(vertexState);
            vertexState.setAgent(secondAgent);
        } else {
            this.vertices.get(secondAgent).remove(vertexState);
            this.vertices.get(firstAgent).add(vertexState);
            this.partitionStates.get(vertexState.getPartition().getId()).getVertices().get(secondAgent).remove(vertexState);
            this.partitionStates.get(vertexState.getPartition().getId()).getVertices().get(firstAgent).add(vertexState);
            vertexState.setAgent(firstAgent);
        }
    }

    public BoardState clone() {
        BoardState boardState = new BoardState(firstAgent, secondAgent);
        boardState.vertices.put(firstAgent, cloneVerteciesList(this.vertices.get(firstAgent)));
        boardState.vertices.put(secondAgent, cloneVerteciesList(this.vertices.get(secondAgent)));
        boardState.partitionStates = clonePartitionsList(this.partitionStates);
        boardState.allVertices = new HashMap<>();
        for (VertexState vertexState : boardState.vertices.get(firstAgent)) {
            boardState.allVertices.put(vertexState.getId(), vertexState);
            int partitionID = vertexState.getPartition().getId();
            boardState.partitionStates.get(partitionID).addVertexState(vertexState.getAgent(), vertexState);
        }

        for (VertexState vertexState : boardState.vertices.get(secondAgent)) {
            boardState.allVertices.put(vertexState.getId(), vertexState);
            int partitionID = vertexState.getPartition().getId();
            boardState.partitionStates.get(partitionID).addVertexState(vertexState.getAgent(), vertexState);
        }
        return boardState;
    }

    private Set<VertexState> cloneVerteciesList(Set<VertexState> vertexStates) {
        Set<VertexState> returnVertexStates = new HashSet<>();
        for (VertexState vertexState : vertexStates) {
            returnVertexStates.add(vertexState.clone());
        }
        return returnVertexStates;
    }

    private List<PartitionState> clonePartitionsList(List<PartitionState> partitionStates) {
        List<PartitionState> returnPartitionStates = new ArrayList<>();
        for (PartitionState partitionState : partitionStates) {
            returnPartitionStates.add(partitionState.clone());
        }
        return returnPartitionStates;
    }
}
