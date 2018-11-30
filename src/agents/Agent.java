package agents;

import models.Action;
import models.BoardState;

import java.util.List;

/**
 * CS 482: Artificial Intelligence.
 * Assignment 2: RISK
 * Agent's Interface introducing main agent actions
 * @author Marc Magdi
 * Thursday, 22 November 2018
 */
public interface Agent {
    List<Action> play(BoardState boardState);
    int getAgentId();
}
