package core;

import agents.Agent;
import models.*;
import org.graphstream.graph.Graph;

import java.util.List;

/**
 * CS 482 : Artificial Intelligence.
 * Assignment 2: RISK
 * Game Main Core
 * @author Marc Magdi
 * Tuesday, 20 November 2018
 */
public interface GameBoardCore {

    Graph startGame(String player1, String player2);

    /**.
     * Get bonus of player if available.
     * By default it returns a value (2), additional elements
     * are added to the value if player is eligible for additional bonus, only
     * if he occupies one ore more continent.
     * @param player the player to get its bonus
     * @return the total value of bonuses
     */
    int getBonus(int player);
    int getBonus(int player, BoardState boardState);

    /**.
     *
     * @param player
     * @return return the vertices that the player occupies
     */
    List<VertexState> getPlayerVertices(int player);
    List<VertexState> getPlayerVertices(Agent player);
    List<VertexState> getPlayerVertices(Agent player, BoardState boardState);


    /**.
     *
     * @param player
     * @return return list of vertices of the given player that
     * can intiate an attack.
     */
    List<VertexState> getPlayerReadyForAttackVertices(int player);

    /**.
     * Add the given army count to the country specified
     * @param vertexId the id of the country to add the armies to
     * @param armiesCount the count of armies
     */
    void addArmiesToCountry(int vertexId, int armiesCount);

    /**.
     * Get valid neigbours of given country, s.t. armies of the country is > neighbour's army - 1
     * @param vertexId the vertex to get its neigbours
     * @return list of valid vertices identifiers.
     */
    List<VertexState> getValidNeighboursToAttack(int vertexId);
    List<VertexState> getValidNeighboursToAttack(int vertexId, BoardState boardState);

    List<VertexState> getVerticesThatCanAttack(int vertexId);
    List<VertexState> getVerticesThatCanAttack(int vertexId, BoardState boardState);

    /**.
     *
     * @param vertexId
     * @return the count of armies in this vertex
     */
    int getArmiesOfVertex(int vertexId);

    /**.
     * Attack the specified vertex.
     * @param v1 the attacking vertex
     * @param v2 the attacked vertex
     */
    void transfer(int v1, int v2, int count);

    /**.
     *
     * @return a list of current partition states
     */
    List<PartitionState> getPartitionsStates();

    /**.
     * @param vertex
     * @return the current state of the given vertex
     */
    VertexState getVertex(Vertex vertex);

    /**.
     * Let the agent play
     * @param agent
     */
    List<Action> play_next(String agent);

    Agent getOpponent(Agent agent);

    boolean winnerExist();
}
