package core;

import agents.Agent;
import agents.AgentFactory;
import models.*;
import org.graphstream.graph.Graph;

import java.util.*;

/**
 * CS 482 : Artificial Intelligence.
 * Assignment 2: RISK
 *
 * @author Marc Magdi
 * Thursday, 22 November 2018
 */
public class GameBoardCoreImpl implements GameBoardCore {
    private Graph graphStreamGraph;
    private CoreGraph coreGraph;
    private BoardState currentState;
    private StateManager stateManager;
    private HashMap<String, Agent> map;
    private BoardCalculator boardCalculator;
    private Agent firstAgent;
    private Agent secondAgent;

    @Override
    public Graph startGame(String player1, String player2) {
        this.firstAgent = AgentFactory.getAgent(player1, 1, this);
        this.secondAgent = AgentFactory.getAgent(player2, 2, this);

        GameBuilder gameBuilder = new GameBuilder(firstAgent, secondAgent);
        gameBuilder.buildGraph();
        this.graphStreamGraph = gameBuilder.getGraphStreamGraph();
        this.coreGraph = gameBuilder.getCoreGraph();
        this.currentState = gameBuilder.getInitialState();
        this.stateManager = new StateManager();
        this.boardCalculator = new BoardCalculator(coreGraph, currentState);
        this.map = new HashMap<>();
        this.map.put(player1, firstAgent);
        this.map.put(player2, secondAgent);
        return this.graphStreamGraph;
    }

    @Override
    public int getBonus(int player) {
        return boardCalculator.getBonus(getAgent(player));
    }

    @Override
    public int getBonus(int player, BoardState boardState) {
        return boardCalculator.getBonus(getAgent(player), boardState);
    }

    @Override
    public List<VertexState> getPlayerVertices(int player) {
        Set<VertexState> vertexStates = player == 1 ?
                this.currentState.getFirstPlayerCountries() : this.currentState.getSecondPlayerCountries();
        List<VertexState> playerVertices = new ArrayList<>();
        playerVertices.addAll(vertexStates);
        return playerVertices;
    }

    @Override
    public List<VertexState> getPlayerVertices(Agent agent) {
        return getPlayerVertices(agent, this.currentState);
    }

    @Override
    public List<VertexState> getPlayerVertices(Agent agent, BoardState boardState) {
        Set<VertexState> vertexStates = boardState.getPlayerCountries(agent);
        List<VertexState> playerVertices = new ArrayList<>();
        playerVertices.addAll(vertexStates);
        return playerVertices;
    }

    @Override
    public List<VertexState> getPlayerReadyForAttackVertices(int player) {
        List<VertexState> vertices = getPlayerVertices(player);
        List<VertexState> validVertices = new LinkedList<>();
        for (VertexState vertex : vertices) {
            if (getValidNeighboursToAttack(vertex.getId()).size() > 0) validVertices.add(vertex);
        }
        return validVertices;
    }

    @Override
    public void addArmiesToCountry(int vertexId, int armiesCount) {
        this.stateManager.getStateAfterAddingArmies(vertexId, armiesCount, currentState);
    }

    @Override
    public List<VertexState> getValidNeighboursToAttack(int vertexId) {
        return this.getValidNeighboursToAttack(vertexId, this.currentState);
    }

    @Override
    public List<VertexState> getValidNeighboursToAttack(int vertexId, BoardState boardState) {
        List<VertexState> validNeighbours = new LinkedList<>();
        Set<Vertex> neighbours = this.coreGraph.getNeighbours(vertexId);
        VertexState currentVertex = boardState.getVertices().get(vertexId);
        for (Vertex vertex : neighbours) {
            VertexState vertexState = boardState.getVertices().get(vertex.getId());
            if (currentVertex.getArmiesCount() - 2  >= vertexState.getArmiesCount()
                    && vertexState.getAgent() != currentVertex.getAgent())
                validNeighbours.add(vertexState);
        }
        return validNeighbours;
    }

    @Override
    public List<VertexState> getVerticesThatCanAttack(int vertexId) {
        return this.getVerticesThatCanAttack(vertexId, this.currentState);
    }

    @Override
    public List<VertexState> getVerticesThatCanAttack(int vertexId, BoardState boardState) {
        List<VertexState> validNeighbours = new LinkedList<>();
        Set<Vertex> neighbours = this.coreGraph.getNeighbours(vertexId);
        VertexState currentVertex = boardState.getVertices().get(vertexId);
        for (Vertex vertex : neighbours) {
            VertexState vertexState = boardState.getVertices().get(vertex.getId());
            if (currentVertex.getArmiesCount() + 2 <= vertexState.getArmiesCount()
                    && vertexState.getAgent() != currentVertex.getAgent())
                validNeighbours.add(vertexState);
        }
        return validNeighbours;
    }

    @Override
    public int getArmiesOfVertex(int vertexId) {
        return this.currentState.getVertices().get(vertexId).getArmiesCount();
    }

    @Override
    public void transfer(int v1, int v2, int count) {
        this.stateManager.getStateAfterAttack(v1, v2, count, currentState);
    }

    @Override
    public List<PartitionState> getPartitionsStates() {
        return this.currentState.getPartitionStates();
    }

    @Override
    public VertexState getVertex(Vertex vertex) {
        return this.currentState.getVertices().get(vertex.getId());
    }

    @Override
    public List<Action> play_next(String agent) {
        return this.map.get(agent).play(this.currentState);
    }

    @Override
    public Agent getOpponent(Agent agent) {
        return agent == firstAgent ? secondAgent : firstAgent;
    }

    @Override
    public boolean winnerExist() {
        int size = this.currentState.getFirstPlayerCountries().size();
        return size == 0 || size == this.coreGraph.getVertices().size();
    }

    private Agent getAgent(int playerId) {
        return playerId == 1 ? firstAgent : secondAgent;
    }
}
