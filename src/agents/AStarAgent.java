package agents;

import agents.evaluators.HeuristicEvaluator;
import core.GameBoardCore;
import core.StateManager;
import models.*;

import java.util.*;

/**
 * Created by programajor on 11/20/18.
 */
public class AStarAgent implements Agent {
    private int agentId;
    private GameBoardCore gameBoardCore;

    public AStarAgent(int agentId, GameBoardCore gameBoardCore) {
        this.agentId = agentId;
        this.gameBoardCore = gameBoardCore;
    }

    @Override
    public List<Action> play(BoardState boardState) {
        PriorityQueue<RiskState> frontier = new PriorityQueue<>();
        Map<RiskState, RiskState> frontierMap = new HashMap<>();
        Set<RiskState> explored = new HashSet<>();
        RiskState initial = new RiskState(boardState,
                HeuristicEvaluator.getHeuristicValueToGoal(this,
                        gameBoardCore.getOpponent(this), boardState), 0);
        frontier.add(initial);
        frontierMap.put(initial, initial);
        while (!frontier.isEmpty()) {
            RiskState curr = frontier.poll();
            frontierMap.remove(curr);
            explored.add(curr);
            if (StateManager.isGoalBoard(curr.getBoardState(), this)) {
                while (curr.getParent() != null && curr.getParent().getParent() != null) {
                    curr = curr.getParent();
                }
                System.out.println("Number of Expanded States in AStar: " + explored.size());
                if (curr.getParent() == null) {
                    return new ArrayList<>();
                } else {
                    List<Action> actions = curr.getActions();
                    applyActions(actions);
                    return actions;
                }
            }
            List<RiskState> neighbours = StateManager
                                .getNeighbouringStates(curr, this, gameBoardCore, 1);
            for (RiskState child : neighbours) {
                if (!frontierMap.containsKey(child) && !explored.contains(child)) {
                    child.setParent(curr);
                    curr.getChildren().add(child);
                    frontier.add(child);
                    frontierMap.put(child, child);
                } else {
                    RiskState childInFrontier = frontierMap.get(child);
                    if (childInFrontier != null && childInFrontier.getActualCost() > child.getActualCost()) {
                        childInFrontier.getParent().getChildren().remove(childInFrontier);
                        childInFrontier.setParent(curr);
                        childInFrontier.setActualCost(child.getActualCost());
                        curr.getChildren().add(childInFrontier);
                    }
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
