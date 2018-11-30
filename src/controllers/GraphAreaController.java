package controllers;

import core.GameBoardCore;
import core.GameBoardCoreImpl;
import javafx.embed.swing.SwingNode;
import javafx.fxml.Initializable;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import models.*;
import org.graphstream.graph.Graph;
import org.graphstream.ui.swingViewer.ViewPanel;
import org.graphstream.ui.view.Viewer;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

/**
 * CS 482 : Artificial Intelligence.
 * Assignment 2 : Risk
 * Main Application Class
 * @author Bishoy Nader Gendy
 * Thursday 22 November 2018
 */
public class GraphAreaController implements Initializable {
    public BorderPane borderPane;
    private MainController mainController;
    private GameBoardCore gameBoardCore;
    private Graph graph;

    private int player;

    private String firstAgent;
    private String secondAgent;
    private int numberOfTurns;

    /**
     * Initializes the reference to the reference to the main controller pane.
     * @param mainController reference to the the parent.
     */
    final void init(final MainController mainController) {
        this.mainController = mainController;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.gameBoardCore = new GameBoardCoreImpl();
    }

    void startGame(String firstAgent, String secondAgent) {
        this.firstAgent = firstAgent;
        this.secondAgent = secondAgent;
        this.graph = this.gameBoardCore.startGame(firstAgent, secondAgent);
        this.player = 1;
        this.numberOfTurns = 1;
        showGraph();
        startPlaying();
    }

    private void startPlaying() {
        if (!gameBoardCore.winnerExist()) {
            this.numberOfTurns++;
            if (isCurrentHuman()) {
                int bonus = gameBoardCore.getBonus(player);
                List<VertexState> vertices = gameBoardCore.getPlayerVertices(player);
                this.mainController.showBonusDialogue(bonus, getListOfIntegers(vertices));
            } else {
                this.mainController.showComputer();
                String agent = player == 1 ? firstAgent : secondAgent;
                List<Action> actions = gameBoardCore.play_next(agent);
                modifyUIAccordingToActions(actions);
                player = player == 1 ? 2 : 1;
                startPlaying();
            }
        } else {
            player = player == 1 ? 2 : 1;
            this.mainController.addLoggingMessage("Game Ended", 0);
            this.mainController.addLoggingMessage("Player " + player + " Wins", 0);
            this.mainController.hideAllDialogues();
            System.out.println("Number of turns to win: " + numberOfTurns/2);
        }
    }

    private void modifyUIAccordingToActions(List<Action> actions) {
        for (Action action : actions) {
            if (action instanceof AddBonusAction) {
                int bonus = ((AddBonusAction) action).getBonusCount();
                int vertex = ((AddBonusAction) action).getVertexId();
                String loggingMessage = "Player " + player + " added " + bonus + " armies to " + vertex + "\n";
                this.mainController.addLoggingMessage(loggingMessage, player);
                int newArmies = gameBoardCore.getArmiesOfVertex(vertex);
                this.graph.getNode(vertex + "")
                        .setAttribute("label", vertex + " (" + newArmies + ")");
            } else if (action instanceof NoAttackAction) {
                String loggingMessage = "Player " + player + " will not attack\n";
                this.mainController.addLoggingMessage(loggingMessage, player);
            } else if (action instanceof AttackAction) {
                int attacker = ((AttackAction) action).getAttackerVertex();
                int attacked = ((AttackAction) action).getAttackedVertex();
                int transferred = ((AttackAction) action).getTransferredArmies();
                logAttackAndTransfer(attacker, attacked, transferred);
                if (player == 1) {
                    this.graph.getNode(attacked + "").setAttribute("ui.class", "one");
                } else {
                    this.graph.getNode(attacked + "").setAttribute("ui.class", "two");
                }
                this.graph.getNode(attacker + "").setAttribute("label",
                        attacker + " (" + gameBoardCore.getArmiesOfVertex(attacker) + ")");
                this.graph.getNode(attacked + "").setAttribute("label",
                        attacked + " (" + gameBoardCore.getArmiesOfVertex(attacked) + ")");
            }
        }
    }

    private List<Integer> getListOfIntegers(List<VertexState> vertices) {
        List<Integer> ret = new ArrayList<>();
        for (VertexState vertex : vertices) {
            ret.add(vertex.getId());
        }
        return ret;
    }

    private boolean isCurrentHuman() {
        String agent = player == 1 ? firstAgent : secondAgent;
        return agent.equals("Human Agent");
    }

    private void showGraph() {
        Viewer viewer = new Viewer(graph, Viewer.ThreadingModel.GRAPH_IN_ANOTHER_THREAD);
        ViewPanel view = viewer.addDefaultView(false);
        viewer.enableAutoLayout();
        viewer.setCloseFramePolicy(Viewer.CloseFramePolicy.EXIT);
        SwingNode swingNode = new SwingNode();
        swingNode.setContent(view);

        StackPane paneWithSwing = new StackPane(swingNode);
        Pane invisiblePane = new Pane(paneWithSwing);
        invisiblePane.setPrefWidth(0);
        invisiblePane.setPrefHeight(0);
        invisiblePane.setOpacity(0);

        borderPane.setCenter(paneWithSwing);
    }

    void addBonusToVertex(int vertex) {
        int bonus = gameBoardCore.getBonus(player);
        String loggingMessage = "Player " + player + " added " + bonus + " armies to " + vertex + "\n";
        this.mainController.addLoggingMessage(loggingMessage, player);
        int newArmies = gameBoardCore.getArmiesOfVertex(vertex) + bonus;
        this.graph.getNode(vertex + "")
                .setAttribute("label", vertex + " (" + newArmies + ")");
        gameBoardCore.addArmiesToCountry(vertex, bonus);
        this.mainController.showWillAttachDialogue();
    }

    void willNotAttack() {
        String loggingMessage = "Player " + player + " will not attack\n";
        this.mainController.addLoggingMessage(loggingMessage, player);
        player = player == 1 ? 2 : 1;
        startPlaying();
    }

    List<VertexState> getValidNeighboursToAttack(Integer vertex) {
        return gameBoardCore.getValidNeighboursToAttack(vertex);
    }

    int getArmiesOfVertex(Integer vertex) {
        return gameBoardCore.getArmiesOfVertex(vertex);
    }

    void transfer(int attacker, int attacked, int transferred) {
        logAttackAndTransfer(attacker, attacked, transferred);
        gameBoardCore.transfer(attacker, attacked, transferred);
        int newAttackerArmies = gameBoardCore.getArmiesOfVertex(attacker);
        int newAttackedArmies = gameBoardCore.getArmiesOfVertex(attacked);
        updateGraphNodes(attacker, attacked, newAttackerArmies, newAttackedArmies);
        player = player == 1 ? 2 : 1;
        startPlaying();
    }

    private void updateGraphNodes(int attacker, int attacked, int newAttackerArmies, int newAttackedArmies) {
        if (player == 1) {
            this.graph.getNode(attacked + "").setAttribute("ui.class", "one");
        } else {
            this.graph.getNode(attacked + "").setAttribute("ui.class", "two");
        }
        this.graph.getNode(attacker + "").setAttribute("label",
                        attacker + " (" + newAttackerArmies + ")");
        this.graph.getNode(attacked + "").setAttribute("label",
                attacked + " (" + newAttackedArmies + ")");
    }

    private void logAttackAndTransfer(int attacker, int attacked, int transferred) {
        String loggingMessage = "Player " + player + " will attack " + attacked +
                                " with " + attacker + "\n";
        this.mainController.addLoggingMessage(loggingMessage, player);
        String OtherLoggingMessage = "Player " + player + " will transfer " + transferred +
                                " armies to " + attacked + "\n" ;
        this.mainController.addLoggingMessage(OtherLoggingMessage, player);
    }

    List<Integer> getPlayerReadyForAttackVertices() {
        return getListOfIntegers(gameBoardCore.getPlayerReadyForAttackVertices(player));
    }

    int getCurrentPlayer() {
        return player;
    }
}
