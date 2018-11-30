package core;

import agents.Agent;
import models.BoardState;
import models.CoreGraph;
import models.Partition;
import models.PartitionState;

import java.util.List;

/**
 * CS 482: Artificial Intelligence.
 * Assignment 2: RISK
 *
 * @author Marc Magdi
 * Friday, 23 November 2018
 */
public class BoardCalculator {
    private CoreGraph coreGraph;
    private BoardState currentState;

    public BoardCalculator(CoreGraph coreGraph, BoardState currentState) {
        this.coreGraph = coreGraph;
        this.currentState = currentState;
    }

    public int getBonus(Agent agent) {
        return getBonus(agent, this.currentState);
    }

    public int getBonus(Agent agent, BoardState boardState) {
        int answer = 2;
        List<PartitionState> partitionStates = boardState.getPartitionStates();
        for (int i = 0; i < partitionStates.size(); i++) {
            Partition partition = this.coreGraph.getPartitions().get(i);
            int verticesCountInPartition = partition.getVertices().size();
            int agentPartitionVertexCount = boardState.getPartitionStates().get(i).getAgentPartitionVertices(agent).size();
            if (agentPartitionVertexCount == verticesCountInPartition) answer += partition.getBonus();
        }
        return answer;
    }
}