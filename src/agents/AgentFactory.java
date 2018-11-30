package agents;

import core.GameBoardCore;

/**
 * CS 482: Artificial Intelligence.
 * Assignment 2: RISK
 *
 * @author Marc Magdi
 * Friday, 23 November 2018
 */
public class AgentFactory {
    /**.
     *
     * @param agent_name the agent name
     * @return an agent instance match the given name
     */
    public static Agent getAgent(String agent_name, int id, GameBoardCore gameBoardCore) {
        if (agent_name.equals("Human Agent")) return new HumanAgent(id);
        else if (agent_name.equals("Passive Agent")) return new PassiveAgent(id, gameBoardCore);
        else if (agent_name.equals("Pacifist Agent")) return new PacifistAgent(id, gameBoardCore);
        else if (agent_name.equals("Aggressive Agent")) return new AggressiveAgent(id, gameBoardCore);
        else if (agent_name.equals("Greedy Agent")) return new GreedyAgent(id, gameBoardCore);
        else if (agent_name.equals("AStar Agent")) return new AStarAgent(id, gameBoardCore);
        else if (agent_name.equals("Real Time AStar Agent")) return new RealTimeAStarAgent(id, gameBoardCore);
        return null;
    }

    private AgentFactory(){}
}
