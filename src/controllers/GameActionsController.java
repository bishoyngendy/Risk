package controllers;

import javafx.beans.value.ChangeListener;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import models.VertexState;

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
public class GameActionsController implements Initializable {
    public GridPane bonusDialogue;
    public Text bonusTitle;
    public ComboBox<Integer> bonusVerticesOptions;
    public GridPane willAttackDialogue;
    public GridPane attackingDialogue;
    public ComboBox<Integer> attackingVertexComboBox;
    public ComboBox<Integer> attackedVertexComboBox;
    public TextField transferredArmiesTextField;
    public Text playerTurnTextInBonus;
    public Text playerTurnTextInWillAttack;
    public Text playerTurnTextInAttacking;
    public GridPane computerPlayingDialogue;
    public Text playerTurnTextInComputer;
    public Button willAttackButton;

    private MainController mainController;
    private ChangeListener<? super String> attackedListener;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        showDialogue(false, false, false);
    }

    void init(MainController mainController) {
        this.mainController = mainController;
    }

    void showBonusDialogue(int bonus, List<Integer> vertices) {
        this.bonusTitle.setText("You have " + bonus + " new armies as a bonus");
        if (vertices.size() > 0) {
            this.bonusVerticesOptions.getItems().setAll(vertices);
            this.bonusVerticesOptions.getSelectionModel().select(0);
        }
        showDialogue(true, false, false);
    }

    public void addBonusArmies(MouseEvent event) {
        int vertex = this.bonusVerticesOptions.getSelectionModel().getSelectedItem();
        this.mainController.addBonusToVertex(vertex);
    }

    void showWillAttachDialogue() {
        List<Integer> attackers = this.mainController.getPlayerReadyForAttackVertices();
        if (attackers.size() == 0) {
            willAttackButton.setDisable(true);
        } else {
            willAttackButton.setDisable(false);
        }
        showDialogue(false, true, false);
    }

    public void willNotAttack(MouseEvent event) {
        this.mainController.willNotAttack();
    }

    public void willAttack(MouseEvent event) {
        List<Integer> attackers = this.mainController.getPlayerReadyForAttackVertices();
        this.attackingVertexComboBox.getItems().setAll(attackers);
        this.attackingVertexComboBox.getSelectionModel().select(0);

        List<Integer> attacked = this.getIntegers(mainController.getValidNeighboursToAttack(attackers.get(0)));
        this.attackedVertexComboBox.getItems().setAll(attacked);
        this.attackedVertexComboBox.getSelectionModel().select(0);

        int attackerArmies = this.mainController.getArmiesOfVertex(attackers.get(0));
        int attackedArmies = this.mainController.getArmiesOfVertex(attacked.get(0));

        int maxTransferred = attackerArmies - attackedArmies - 1;
        transferredArmiesTextField.setText("1");
        if (this.attackedListener != null) {
            transferredArmiesTextField.textProperty().removeListener(this.attackedListener);
        }
        this.attackedListener = getAttackedListener(maxTransferred);
        transferredArmiesTextField.textProperty().addListener(this.attackedListener);
        showDialogue(false, false, true);
    }

    private List<Integer> getIntegers(List<VertexState> vertices) {
        List<Integer> ret = new ArrayList<>();
        for (VertexState state : vertices) {
            ret.add(state.getId());
        }
        return ret;
    }

    private void showDialogue(boolean bonus, boolean willAttack, boolean attacking) {
        this.bonusDialogue.setVisible(bonus);
        this.willAttackDialogue.setVisible(willAttack);
        this.attackingDialogue.setVisible(attacking);
        if (this.mainController != null) {
            playerTurnTextInBonus.setText("Player " + mainController.getCurrentPlayer() + " to play:");
            playerTurnTextInWillAttack.setText("Player " + mainController.getCurrentPlayer() + " to play:");
            playerTurnTextInAttacking.setText("Player " + mainController.getCurrentPlayer() + " to play:");
            playerTurnTextInComputer.setText("Player " + mainController.getCurrentPlayer() + " to play:");
        }
        if (!bonus && !willAttack && !attacking) {
            this.computerPlayingDialogue.setVisible(true);
        } else {
            this.computerPlayingDialogue.setVisible(false);
        }
    }

    public void attack(MouseEvent event) {
        int attacker = this.attackingVertexComboBox.getSelectionModel().getSelectedItem();
        int attacked = this.attackedVertexComboBox.getSelectionModel().getSelectedItem();
        int transferred = Integer.valueOf(transferredArmiesTextField.getText());
        showDialogue(false, false, false);
        this.mainController.transfer(attacker, attacked, transferred);
    }

    public void changeAttackingVertex(ActionEvent event) {
        int attacker = this.attackingVertexComboBox.getSelectionModel().getSelectedItem();
        List<Integer> attacked = getIntegers(mainController.getValidNeighboursToAttack(attacker));
        this.attackedVertexComboBox.getItems().setAll(attacked);
        this.attackedVertexComboBox.getSelectionModel().select(0);
    }

    public void changeAttackedVertex(ActionEvent event) {
        int attacker = this.attackingVertexComboBox.getSelectionModel().getSelectedItem();
        int attacked = this.attackedVertexComboBox.getSelectionModel().getSelectedItem();
        int attackerArmies = this.mainController.getArmiesOfVertex(attacker);
        int attackedArmies = this.mainController.getArmiesOfVertex(attacked);
        int maxTransferred = attackerArmies - attackedArmies - 1;
        transferredArmiesTextField.setText("1");
        if (this.attackedListener != null) {
            transferredArmiesTextField.textProperty().removeListener(this.attackedListener);
        }
        this.attackedListener = getAttackedListener(maxTransferred);
        transferredArmiesTextField.textProperty().addListener(this.attackedListener);
    }

    private ChangeListener<? super String> getAttackedListener(int maxTransferred) {
        return (observable, oldValue, newValue) -> {
            boolean first = newValue.matches("^\\d+$");
            if (first) {
                boolean second = Integer.valueOf(newValue) < 1;
                boolean third = Integer.valueOf(newValue) > maxTransferred;
                if (second || third) {
                    transferredArmiesTextField.setText(oldValue);
                }
            } else {
                transferredArmiesTextField.setText(oldValue);
            }
        };
    }

    void showComputer() {
        showDialogue(false, false, false);
//        try {
//            Thread.sleep(3000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
    }

    public void hideAllDialogues() {
        this.bonusDialogue.setVisible(false);
        this.willAttackDialogue.setVisible(false);
        this.attackingDialogue.setVisible(false);
        this.computerPlayingDialogue.setVisible(false);
    }
}
