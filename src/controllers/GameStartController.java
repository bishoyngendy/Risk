package controllers;

import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.input.MouseEvent;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * CS 482 : Artificial Intelligence.
 * Assignment 2 : Risk
 * Main Application Class
 * @author Bishoy Nader Gendy
 * Thursday 22 November 2018
 */
public class GameStartController implements Initializable {
    private String[] agents = {"Human Agent", "Passive Agent", "Aggressive Agent",
                               "Pacifist Agent", "Greedy Agent", "AStar Agent",
                               "Real Time AStar Agent"};
    public ComboBox<String> firstAgentComboBox;
    public ComboBox<String> secondAgentComboBox;
    private MainController mainController;

    /**
     * Initializes the reference to the reference to the main controller pane.
     * @param mainController reference to the the parent.
     */
    public final void init(final MainController mainController) {
        this.mainController = mainController;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        firstAgentComboBox.getItems().addAll(agents);
        secondAgentComboBox.getItems().addAll(agents);
        firstAgentComboBox.getSelectionModel().select(0);
        secondAgentComboBox.getSelectionModel().select(0);
    }

    public void startGame(MouseEvent event) {
        String firstAgent = firstAgentComboBox.getSelectionModel().getSelectedItem();
        String secondAgent = secondAgentComboBox.getSelectionModel().getSelectedItem();
        this.mainController.startGame(firstAgent, secondAgent);
    }
}
