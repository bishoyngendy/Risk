package agents.evaluators;

import agents.Agent;
import models.BoardState;
import models.PartitionState;
import models.VertexState;

import java.util.List;

/**
 * CS 482: Artificial Intelligence.
 * Assignment 2: RISK
 *
 * @author Marc Magdi
 * Friday, 23 November 2018
 */
public class HeuristicEvaluator {

    /**.
     * Computer a heuristic value for expected remanining number of steps to the goal state.
     * @param player
     * @param opponnent
     * @param boardStates
     * @return
     */
    public static int getHeuristicValueToGoal(Agent player, Agent opponnent, BoardState boardStates) {
        float value = boardStates.getPlayerCountries(opponnent).size();
        List<PartitionState> partitionStates = boardStates.getPartitionStates();
        for (PartitionState partitionState : partitionStates) {
            int totalOpponnentArmies = 0;
            for (VertexState vertexState : partitionState.getAgentPartitionVertices(opponnent)) {
                totalOpponnentArmies += vertexState.getArmiesCount();
            }

            int totalArmies = totalOpponnentArmies;
            for (VertexState vertexState : partitionState.getAgentPartitionVertices(player)) {
                totalArmies += vertexState.getArmiesCount();
            }

            int partitionVerticesCount = partitionState.getPartition().getVertices().size();
            value += partitionVerticesCount * partitionState.getPartition().getNormalizedBonus() * totalOpponnentArmies/totalArmies;
        }

        return (int) value;
    }
}
