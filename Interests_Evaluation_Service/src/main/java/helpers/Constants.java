package helpers;

import com.google.common.collect.Table;
import model.Entity;
import org.jgrapht.alg.shortestpath.FloydWarshallShortestPaths;
import org.jgrapht.graph.DefaultWeightedEdge;

import java.util.HashMap;
import java.util.Map;

public class Constants {
    public static final String DEMO_DATE_FORMAT = "dd/MM/yyyy HH:mm:ss";
    public static  final double INFINITY = Double.POSITIVE_INFINITY;
    public static double toParentWeight, toChildWeight, toAssociatedWeight;
    public static HashMap<String, Entity> ontology;
    public static FloydWarshallShortestPaths<Entity, DefaultWeightedEdge> graph;
    public static Map<Integer, Map<String, Double>> interests;
    public static int PORT;

}
