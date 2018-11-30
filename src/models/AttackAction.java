package models;

/**
 * CS 482: Artificial Intelligence.
 * Assignment 2: RISK
 *
 * @author Marc Magdi
 * Friday, 23 November 2018
 */
public class AttackAction implements Action {
    int attackerVertex;
    int attackedVertex;
    int transferredArmies;

    public AttackAction(int attackerVertex, int attackedVertex, int transferredArmies) {
        this.attackerVertex = attackerVertex;
        this.attackedVertex = attackedVertex;
        this.transferredArmies = transferredArmies;
    }

    public int getAttackerVertex() {
        return attackerVertex;
    }

    public int getAttackedVertex() {
        return attackedVertex;
    }

    public int getTransferredArmies() {
        return transferredArmies;
    }
}
