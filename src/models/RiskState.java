package models;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by programajor on 11/23/18.
 */
public class RiskState implements Comparable<RiskState> {
    private BoardState boardState;
    private int heuristicValue;
    private int actualCost;
    private RiskState parent;
    private List<RiskState> children;
    private List<Action> actions;

    public RiskState(BoardState boardState, int heuristicValue, int actualCost) {
        this.boardState = boardState;
        this.heuristicValue = heuristicValue;
        this.actualCost = actualCost;
        this.children = new ArrayList<>();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RiskState riskState = (RiskState) o;

        if (heuristicValue != riskState.heuristicValue) return false;
        if (actualCost != riskState.actualCost) return false;
        return boardState != null ? boardState.equals(riskState.boardState) : riskState.boardState == null;
    }

    @Override
    public int hashCode() {
        return this.heuristicValue;
    }

    public float getHeuristicValue() {
        return heuristicValue;
    }

    public void setHeuristicValue(int heuristicValue) {
        this.heuristicValue = heuristicValue;
    }

    public int getActualCost() {
        return actualCost;
    }

    public void setActualCost(int actualCost) {
        this.actualCost = actualCost;
    }

    public BoardState getBoardState() {
        return boardState;
    }

    public void setBoardState(BoardState boardState) {
        this.boardState = boardState;
    }

    private Integer getComparingValue() {
        return actualCost + heuristicValue;
    }

    @Override
    public int compareTo(RiskState state) {
        return getComparingValue().compareTo(state.getComparingValue());
    }

    public void setParent(RiskState parent) {
        this.parent = parent;
    }

    public List<RiskState> getChildren() {
        return children;
    }

    public RiskState getParent() {
        return parent;
    }

    public List<Action> getActions() {
        return actions;
    }

    public void setActions(List<Action> actions) {
        this.actions = actions;
    }

    public void setChildren(List<RiskState> children) {
        this.children = children;
    }
}
