package agents;

import core.GameBoardCore;
import models.*;

import java.util.*;

/**
 * CS 482: Artificial Intelligence.
 * Assignment 2: RISK
 *
 * @author Marc Magdi
 * Friday, 23 November 2018
 */
public class PassiveAgent implements Agent {
    private int agentId;
    private GameBoardCore gameBoardCore;

    public PassiveAgent(int agentId, GameBoardCore gameBoardCore) {
        this.agentId = agentId;
        this.gameBoardCore = gameBoardCore;
    }

    @Override
    public List<Action> play(BoardState boardState) {
        List<Action> actions = new LinkedList<>();
        actions.add(addBonus());
        actions.add(new NoAttackAction());
        return actions;
    }

    private Action addBonus() {
        int bonus = this.gameBoardCore.getBonus(this.agentId);
        List<VertexState> verticesState = this.gameBoardCore.getPlayerVertices(this.agentId);
        Collections.sort(verticesState);
        this.gameBoardCore.addArmiesToCountry(verticesState.get(0).getId(), bonus);
        return new AddBonusAction(verticesState.get(0).getId(), bonus);
    }

    @Override
    public int getAgentId() {
        return agentId;
    }
}
