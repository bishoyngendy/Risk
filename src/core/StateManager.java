package core;

import agents.Agent;
import agents.evaluators.HeuristicEvaluator;
import models.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * CS 482 : Artificial Intelligence.
 * Assignment 2: RISK
 * Responsible for changing from one
 * state to the next one
 * @author Marc Magdi
 * Thursday, 22 November 2018
 */
public class StateManager {
    BoardState geNewtStateAfterAddingArmies(int vertexId, int armiesCount, BoardState boardState) {
        BoardState boardStateCopy = boardState.clone();
        return getStateAfterAddingArmies(vertexId, armiesCount, boardStateCopy);
    }

    BoardState getNewStateAfterAttack(int v1, int v2, int count, BoardState boardState) {
        BoardState boardStateCopy = boardState.clone();
        return getStateAfterAttack(v1, v2, count, boardStateCopy);
    }

    BoardState getStateAfterAddingArmies(int vertexId, int armiesCount, BoardState boardState) {
        VertexState vertexState = boardState.getVertices().get(vertexId);
        vertexState.increaseArmiesCount(armiesCount);
        return boardState;
    }

    BoardState getStateAfterAttack(int v1, int v2, int count, BoardState boardState) {
        VertexState vertexState1 = boardState.getVertices().get(v1);
        VertexState vertexState2 = boardState.getVertices().get(v2);

        // update vertices armies
        vertexState1.decreaseArmiesCount(vertexState2.getArmiesCount());
        vertexState1.decreaseArmiesCount(count);
        vertexState2.setArmiesCount(count);

        // move the vertex to opponent
        boardState.moveVertexToOpponent(vertexState2);

        return boardState;
    }

    public static List<RiskState> getNeighbouringStates(RiskState riskState, Agent agent, GameBoardCore gameBoardCore, int stepCost) {
        List<RiskState> riskStates = new ArrayList<>();
        StateManager manager = new StateManager();
        int bonus = gameBoardCore.getBonus(agent.getAgentId(), riskState.getBoardState());
        Set<VertexState> vertices = riskState.getBoardState().getPlayerCountries(agent);
        addNeighbours(riskState.getBoardState(), riskStates, manager, bonus, vertices, agent, gameBoardCore, stepCost + riskState.getActualCost());
        return riskStates;
    }

    private static void addNeighbours(BoardState boardState, List<RiskState> riskStates,
                                      StateManager manager, int bonus, Set<VertexState> vertices,
                                      Agent agent, GameBoardCore gameBoardCore, int actualCost) {
        for (VertexState bonusVertex : vertices) { // state is the city which take the bonus
            BoardState boardAfterBonus = manager.geNewtStateAfterAddingArmies(bonusVertex.getId(), bonus, boardState);
            addNoAttackStates(riskStates, bonus, bonusVertex, boardAfterBonus, actualCost, agent, gameBoardCore);
            BoardState cloned = boardAfterBonus.clone(); // added to it bonus
            Set<VertexState> states = cloned.getPlayerCountries(agent);
            for (VertexState attacker : states) {
                List<VertexState> attackedCities =
                        gameBoardCore.getValidNeighboursToAttack(attacker.getId(), cloned);
                int attackerArmies = attacker.getArmiesCount();
                for (VertexState attacked : attackedCities) {
                    int attackedArmies = attacked.getArmiesCount();
                    int maxTransferred = attackerArmies - attackedArmies - 1;
                    for (int transferred = 1; transferred <= maxTransferred; transferred++) {
                        BoardState finalState = manager
                                .getNewStateAfterAttack(attacker.getId(), attacked.getId(), transferred, cloned);
                        List<Action> actions = new ArrayList<>();
                        actions.add(new AddBonusAction(bonusVertex.getId(), bonus));
                        actions.add(new AttackAction(attacker.getId(), attacked.getId(), transferred));
                        int heuristic = HeuristicEvaluator.getHeuristicValueToGoal(agent, gameBoardCore.getOpponent(agent), finalState);
                        RiskState riskState = new RiskState(finalState, heuristic, actualCost);
                        riskState.setActions(actions);
                        riskStates.add(riskState);
                    }
                }
            }
        }
    }

    private static void addNoAttackStates(List<RiskState> riskStates, int bonus,
                                          VertexState bonusVertex, BoardState vertexBoardState,
                                          int actualCost, Agent agent, GameBoardCore gameBoardCore) {
        int heuristic = HeuristicEvaluator.getHeuristicValueToGoal(agent, gameBoardCore.getOpponent(agent), vertexBoardState);
        RiskState riskState = new RiskState(vertexBoardState, heuristic, actualCost);
        List<Action> actions = new ArrayList<>();
        actions.add(new AddBonusAction(bonusVertex.getId(), bonus));
        actions.add(new NoAttackAction());
        riskState.setActions(actions);
        riskStates.add(riskState);
    }

    public static boolean isGoalBoard(BoardState boardState, Agent agent) {
        return boardState.getPlayerCountries(agent).size() == boardState.getVertices().size();
    }
}
