package controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import models.VertexState;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

/**
 * CS 482 : Artificial Intelligence.
 * Assignment 2 : Risk
 * Main Application Class
 * @author Bishoy Nader Gendy
 * Thursday 22 November 2018
 */
public class MainController implements Initializable {
    @FXML private GraphAreaController graphAreaController;
    @FXML private GameStartController gameStartController;
    @FXML private GameActionsController gameActionsController;
    @FXML private LoggingController loggingController;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        graphAreaController.init(this);
        gameStartController.init(this);
        gameActionsController.init(this);
        loggingController.init(this);
    }

    void startGame(String firstAgent, String secondAgent) {
        this.loggingController.addLoggingMessage("Game Started", 0);
        this.graphAreaController.startGame(firstAgent, secondAgent);
    }

    void showBonusDialogue(int bonus, List<Integer> vertices) {
        this.gameActionsController.showBonusDialogue(bonus, vertices);
    }

    void showWillAttachDialogue() {
        this.gameActionsController.showWillAttachDialogue();
    }

    void addBonusToVertex(int selectedCity) {
        this.graphAreaController.addBonusToVertex(selectedCity);
    }

    void willNotAttack() {
        this.graphAreaController.willNotAttack();
    }

    List<VertexState> getValidNeighboursToAttack(Integer vertex) {
        return this.graphAreaController.getValidNeighboursToAttack(vertex);
    }

    int getArmiesOfVertex(Integer vertex) {
        return this.graphAreaController.getArmiesOfVertex(vertex);
    }

    void transfer(int attacker, int attacked, int transferred) {
        this.graphAreaController.transfer(attacker, attacked, transferred);
    }

    List<Integer> getPlayerReadyForAttackVertices() {
        return this.graphAreaController.getPlayerReadyForAttackVertices();
    }

    int getCurrentPlayer() {
        return this.graphAreaController.getCurrentPlayer();
    }

    void addLoggingMessage(String loggingMessage, int player) {
        this.loggingController.addLoggingMessage(loggingMessage, player);
    }

    public void showComputer() {
        this.gameActionsController.showComputer();
    }

    public void hideAllDialogues() {
        this.gameActionsController.hideAllDialogues();
    }
}
