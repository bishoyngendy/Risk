package models;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by programajor on 11/20/18.
 */
public class Vertex {
    private int id;
    private Set<Vertex> neighbours;
    private Partition partition;

    public Vertex(int id) {
        this.id = id;
        this.neighbours = new HashSet<>();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Set<Vertex> getNeighbours() {
        return neighbours;
    }

    public void setNeighbours(Set<Vertex> neighbours) {
        this.neighbours = neighbours;
    }

    public Partition getPartition() {
        return partition;
    }

    public void setPartition(Partition partition) {
        this.partition = partition;
    }
}
