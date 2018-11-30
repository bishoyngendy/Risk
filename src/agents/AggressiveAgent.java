package agents;

import core.GameBoardCore;
import models.*;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * CS 482: Artificial Intelligence.
 * Assignment 2: RISK
 *
 * @author Marc Magdi
 * Friday, 23 November 2018
 */
public class AggressiveAgent implements Agent {
    private int agentId;
    private GameBoardCore gameBoardCore;

    public AggressiveAgent(int agentId, GameBoardCore gameBoardCore) {
        this.agentId = agentId;
        this.gameBoardCore = gameBoardCore;
    }

    @Override
    public List<Action> play(BoardState boardState) {
        List<Action> actions = new LinkedList<>();
        actions.add(addBonus());
        actions.add(attack());
        return actions;
    }

    @Override
    public int getAgentId() {
        return agentId;
    }

    private Action addBonus() {
        int bonus = this.gameBoardCore.getBonus(this.agentId);
        List<VertexState> vertecesState = this.gameBoardCore.getPlayerVertices(this.agentId);
        Collections.sort(vertecesState);
        this.gameBoardCore.addArmiesToCountry(vertecesState.get(vertecesState.size()-1).getId(), bonus);
        return new AddBonusAction(vertecesState.get(vertecesState.size()-1).getId(), bonus);
    }

    private Action attack() {
        // prevent opponent to get a continent if possible
        List<PartitionState> partitionStates = this.gameBoardCore.getPartitionsStates();
        Agent opponent = this.gameBoardCore.getOpponent(this);
        for (PartitionState partitionState : partitionStates) {
            Set<VertexState> opponentVertices = partitionState.getVertices().get(opponent);
            if (opponentVertices.size() + 1 == partitionState.getPartition().getVertices().size()) {
                // need to attack any vertex in this partiton if possible
                Set<VertexState> vertices = partitionState.getVertices().get(this);
                for (VertexState vertex : vertices) {
                    List<VertexState> verticesState = this.gameBoardCore.getValidNeighboursToAttack(vertex.getId());
                    Collections.sort(verticesState);
                    for (VertexState opponentState : verticesState) {
                        if (partitionState.getVertices().get(opponent).contains(opponentState)) {
                            // attack
                            int remaining = vertex.getArmiesCount() - opponentState.getArmiesCount();
                            this.gameBoardCore.transfer(vertex.getId(), opponentState.getId(), remaining/2);
                            return new AttackAction(vertex.getId(), opponentState.getId(), remaining/2);
                        }
                    }
                }
            }
        }

        // cause most damage if no continent can be taken
        List<VertexState> opponentVertices = this.gameBoardCore.getPlayerVertices(opponent);
        Collections.sort(opponentVertices);
        for (int i = opponentVertices.size()-1; i >= 0; i--) {
            VertexState opponentVertex = opponentVertices.get(i);
            List<VertexState> verticesState = this.gameBoardCore.getVerticesThatCanAttack(opponentVertex.getId());
            for (VertexState myVertex : verticesState) {
                int remaining = myVertex.getArmiesCount() - opponentVertex.getArmiesCount();
                this.gameBoardCore.transfer(myVertex.getId(), opponentVertex.getId(), remaining/2);
                return new AttackAction(myVertex.getId(), opponentVertex.getId(), remaining/2);
            }
        }

        return new NoAttackAction();
    }
}
