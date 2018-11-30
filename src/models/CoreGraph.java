package models;

import java.util.*;

/**
 * Created by programajor on 11/20/18.
 */
public class CoreGraph {
    private Map<Integer, Vertex> vertices;
    private List<Partition> partitions;

    public CoreGraph(int numberOfPlayers, int numberOfVertices) {
        vertices = new HashMap<>();
        partitions = new ArrayList<>();
        for (int i = 0; i < numberOfVertices; i++) {
            vertices.put(i + 1 , new Vertex(i + 1));
        }
    }

    public void addEdge(int first, int second) {
        Vertex v1 = vertices.get(first);
        Vertex v2 = vertices.get(second);
        v1.getNeighbours().add(v2);
        v2.getNeighbours().add(v1);
    }

    public Partition createPartition(int id, Integer bonus) {
        Partition partition = new Partition(id, bonus);
        partitions.add(partition);
        return partition;
    }

    public Set<Vertex> getNeighbours(int vertexId) {
        Vertex vertex = vertices.get(vertexId);
        return vertex.getNeighbours();
    }

    public Map<Integer, Vertex> getVertices() {
        return vertices;
    }

    public void setVertices(Map<Integer, Vertex> vertices) {
        this.vertices = vertices;
    }

    public List<Partition> getPartitions() {
        return partitions;
    }

    public void setPartitions(List<Partition> partitions) {
        this.partitions = partitions;
    }
}
