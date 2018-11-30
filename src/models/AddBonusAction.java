package models;

/**
 * CS 482: Artificial Intelligence.
 * Assignment 2: RISK
 *
 * @author Marc Magdi
 * Friday, 23 November 2018
 */
public class AddBonusAction implements Action {
    private int vertexId;
    private int bonusCount;

    public AddBonusAction(int vertexId, int bonusCount) {
        this.vertexId = vertexId;
        this.bonusCount = bonusCount;
    }

    public int getVertexId() {
        return vertexId;
    }

    public int getBonusCount() {
        return bonusCount;
    }
}
