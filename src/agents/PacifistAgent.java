package agents;

import core.GameBoardCore;
import models.*;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by programajor on 11/20/18.
 */
public class PacifistAgent implements Agent {
    private int agentId;
    private GameBoardCore gameBoardCore;

    public PacifistAgent(int agentId, GameBoardCore gameBoardCore) {
        this.agentId = agentId;
        this.gameBoardCore = gameBoardCore;
    }

    @Override
    public List<Action> play(BoardState boardState) {
        List<Action> actions = new LinkedList<>();
        actions.add(addBonus());
        actions.add(addAttack());
        return actions;
    }

    private Action addBonus() {
        int bonus = this.gameBoardCore.getBonus(this.agentId);
        List<VertexState> verticesState = this.gameBoardCore.getPlayerVertices(this.agentId);
        Collections.sort(verticesState);
        this.gameBoardCore.addArmiesToCountry(verticesState.get(0).getId(), bonus);
        return new AddBonusAction(verticesState.get(0).getId(), bonus);
    }

    private Action addAttack() {
        List<VertexState> opponnentVertices = gameBoardCore.getPlayerVertices(gameBoardCore.getOpponent(this));
        Collections.sort(opponnentVertices);
        for (VertexState oppenentVertex : opponnentVertices) {
            List<VertexState> attackers = gameBoardCore.getVerticesThatCanAttack(oppenentVertex.getId());
            if (attackers.size() != 0) {
                int attacker = attackers.get(0).getId();
                int transferred = gameBoardCore.getArmiesOfVertex(attacker) -
                    gameBoardCore.getArmiesOfVertex(oppenentVertex.getId());
                this.gameBoardCore.transfer(attacker, oppenentVertex.getId(), transferred / 2);
                return new AttackAction(attacker, oppenentVertex.getId(), transferred / 2);
            }
        }
        return new NoAttackAction();
    }

    @Override
    public int getAgentId() {
        return agentId;
    }
}
