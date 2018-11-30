package models;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by programajor on 11/20/18.
 */
public class Partition {
    private int id;
    private int bonus;
    private Set<Vertex> vertices;
    private double normalizedBonus;

    public Partition(int id, Integer bonus) {
        this.id = id;
        this.bonus = bonus;
        this.vertices = new HashSet<>();
    }

    public double getNormalizedBonus() {
        return normalizedBonus;
    }

    public void setNormalizedBonus(double normalizedBonus) {
        this.normalizedBonus = normalizedBonus;
    }

    public int getId() {
        return id;
    }

    public Set<Vertex> getVertices() {
        return vertices;
    }

    public int getBonus() {
        return bonus;
    }
}
