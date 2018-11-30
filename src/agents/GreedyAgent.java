package agents;

import agents.evaluators.HeuristicEvaluator;
import core.GameBoardCore;
import core.StateManager;
import models.*;

import java.util.*;

/**
 * Created by programajor on 11/20/18.
 */
public class GreedyAgent implements Agent {
    private int agentId;
    private GameBoardCore gameBoardCore;

    public GreedyAgent(int agentId, GameBoardCore gameBoardCore) {
        this.agentId = agentId;
        this.gameBoardCore = gameBoardCore;
    }

    @Override
    public List<Action> play(BoardState boardState) {
        PriorityQueue<RiskState> frontier = new PriorityQueue<>();
        Set<RiskState> frontierSet = new HashSet<>();
        Set<RiskState> explored = new HashSet<>();
        RiskState initial = new RiskState(boardState,
                HeuristicEvaluator.getHeuristicValueToGoal(this,
                        gameBoardCore.getOpponent(this), boardState), 0);
        frontier.add(initial);
        frontierSet.add(initial);
        while (!frontier.isEmpty()) {
            RiskState curr = frontier.poll();
            frontierSet.remove(curr);
            explored.add(curr);
            if (StateManager.isGoalBoard(curr.getBoardState(), this)) {
                while (curr.getParent() != null && curr.getParent().getParent() != null) {
                    curr = curr.getParent();
                }
                System.out.println("Number of Expanded States in Greedy: " + explored.size());
                if (curr.getParent() == null) {
                    return new ArrayList<>();
                } else {
                    List<Action> actions = curr.getActions();
                    applyActions(actions);
                    return actions;
                }
            }
            List<RiskState> neighbours = StateManager
                        .getNeighbouringStates(curr, this, gameBoardCore, 0);
            for (RiskState child : neighbours) {
                if (!frontierSet.contains(child) && !explored.contains(child)) {
                    child.setParent(curr);
                    curr.getChildren().add(child);
                    frontier.add(child);
                    frontierSet.add(child);
                }
            }
        }
        return new ArrayList<>();
    }

    private void applyActions(List<Action> actions) {
        for (Action action : actions) {
            if (action instanceof AddBonusAction) {
                this.gameBoardCore.addArmiesToCountry(((AddBonusAction) action).getVertexId(),
                        ((AddBonusAction) action).getBonusCount());
            } else if (action instanceof AttackAction) {
                this.gameBoardCore.transfer(((AttackAction) action).getAttackerVertex(),
                        ((AttackAction) action).getAttackedVertex(),
                        ((AttackAction) action).getTransferredArmies());
            }
        }
    }

    @Override
    public int getAgentId() {
        return agentId;
    }
}
