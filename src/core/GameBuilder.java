package core;

import agents.Agent;
import models.*;
import org.graphstream.graph.Graph;
import org.graphstream.graph.implementations.SingleGraph;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.Scanner;

/**
 * Created by programajor on 11/20/18.
 */
public class GameBuilder {
    private static GameBuilder instance;
    private CoreGraph coreGraph;
    private Graph graphStreamGraph;
    private static final double MAX_NUMBER_OF_PARTITIONS = 8.0;
    private BoardState initialState;
    private Agent firstAgent;
    private Agent secondAgent;

    public GameBuilder(Agent firstAgent, Agent secondAgent) {
        this.firstAgent = firstAgent;
        this.secondAgent = secondAgent;
        this.initialState = new BoardState(firstAgent, secondAgent);
    }

    CoreGraph getCoreGraph() {
        return coreGraph;
    }

    Graph getGraphStreamGraph() {
        return graphStreamGraph;
    }

    void buildGraph() {
        try {
            Scanner sc = new Scanner(new File("input.txt"));
            this.graphStreamGraph = initializeGraph();
            this.coreGraph = initializeGame(sc);
            addPartitionsToGraph(sc);
            addArmiesToGraph(sc);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private CoreGraph initializeGame(Scanner sc) {
        int numberOfVertices = Integer.valueOf(sc.nextLine().split("\\s+")[1]);
        CoreGraph coreGraph = new CoreGraph(2, numberOfVertices);
        for (int i = 0; i < numberOfVertices; i++) {
            graphStreamGraph.addNode(i + 1 + "");
        }
        int numberOfEdges = Integer.valueOf(sc.nextLine().split("\\s+")[1]);
        for (int i = 0; i < numberOfEdges; i++) {
            String[] numbers = getNumbers(sc);
            coreGraph.addEdge(Integer.valueOf(numbers[0]), Integer.valueOf(numbers[1]));
            graphStreamGraph.addEdge(numbers[0] + '-' +  numbers[1], numbers[0], numbers[1]);
        }
        return coreGraph;
    }

    private void addPartitionsToGraph(Scanner sc) {
        int numberOfPartitions = Integer.valueOf(sc.nextLine().split("\\s+")[1]);
        for (int i = 0; i < numberOfPartitions; i++) {
            String line = sc.nextLine();
            String[] numbers = line.split("\\s+");
            Partition partition = this.coreGraph.createPartition(i, Integer.valueOf(numbers[0]));
            for (int j = 1; j < numbers.length; j++) {
                Vertex v = this.coreGraph.getVertices().get(Integer.valueOf(numbers[j]));
                graphStreamGraph.getNode(numbers[j])
                        .setAttribute("ui.color", i/MAX_NUMBER_OF_PARTITIONS);
                partition.getVertices().add(v);
                v.setPartition(partition);
            }

            this.initialState.addPartition(i, partition);
        }

        this.normalizeBonuses(this.coreGraph.getPartitions());
    }

    private void normalizeBonuses(List<Partition> partitionList) {
        int max = Integer.MIN_VALUE;
        int min = Integer.MAX_VALUE;

        for (Partition partition : partitionList) {
            max = Math.max(max, partition.getBonus());
            min = Math.min(min, partition.getBonus());
        }

        for (Partition partition : partitionList) {
            partition.setNormalizedBonus((double)partition.getBonus()-min/(max-min));
        }
    }

    private void addArmiesToGraph(Scanner sc) {
        int numberOfVertices = this.coreGraph.getVertices().size();
        for (int i = 0; i < numberOfVertices; i++) {
            String line = sc.nextLine();
            String[] numbers = line.split("\\s+");
            Vertex vertex = this.coreGraph.getVertices().get(i + 1);
            int playerId = Integer.valueOf(numbers[0]);
            Agent agent = getAgent(playerId);
            int initialArmies = Integer.valueOf(numbers[1]);
            graphStreamGraph.getNode(i + 1 + "").setAttribute("label",
                    vertex.getId() + " (" + initialArmies + ")");
            if (playerId == 1) {
                graphStreamGraph.getNode(i + 1 + "").addAttribute("ui.class", "one");
            } else {
                graphStreamGraph.getNode(i + 1 + "").addAttribute("ui.class", "two");
            }
            // create a new vertex state and add it to the agent vertices
            VertexState vertexState = this.initialState.addVertex(agent, vertex, initialArmies);
            // updating partition state
            PartitionState partitionState = this.initialState.getPartitionStates()
                    .get(vertexState.getPartition().getId());

            partitionState.addVertexState(agent, vertexState);
        }
    }

    private Agent getAgent(int id) {
        return id == 1 ? firstAgent : secondAgent;
    }

    private static String[] getNumbers(Scanner sc) {
        String line = sc.nextLine();
        String modified = line.substring(1, line.length() - 1);
        return modified.split("\\s+");
    }

    BoardState getInitialState() {
        return initialState;
    }

    private Graph initializeGraph() {
        Graph graph = new SingleGraph("RISK");
        graph.setStrict(false);
        graph.setAutoCreate(false);
        graph.addAttribute("ui.quality");
        graph.addAttribute("ui.antialias");
        graph.addAttribute("ui.stylesheet", "url('style.css')");
        return graph;
    }
}
