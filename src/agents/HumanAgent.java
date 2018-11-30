package agents;

import models.Action;
import models.BoardState;

import java.util.List;

/**
 * Created by programajor on 11/20/18.
 */
public class HumanAgent implements Agent {
    private int agentId;

    public HumanAgent(int id) {
        this.agentId = id;
    }

    @Override
    public List<Action> play(BoardState boardState) {
        return null;
    }

    @Override
    public int getAgentId() {
        return agentId;
    }
}
