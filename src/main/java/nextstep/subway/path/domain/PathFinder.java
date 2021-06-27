package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Section;
import nextstep.subway.station.domain.Station;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

import java.util.Collections;
import java.util.List;

public class PathFinder {
    private List stations;
    private double distance;

    public static PathFinder of(Station sourceStation, Station targetStation, List<Station> stations, List<Section> sections) {
        PathFinder pathFinder = new PathFinder();
        pathFinder.validateEqualStation(sourceStation,targetStation);
        pathFinder.init(sourceStation,targetStation,stations,sections);
        return pathFinder;
    }

    private void validateEqualStation(Station sourceStation, Station targetStation) {
        if(sourceStation.equals(targetStation)){
            throw new IllegalArgumentException();
        }
    }

    private void init(Station sourceStation, Station targetStation, List<Station> stations, List<Section> sections) {
        WeightedMultigraph<Station, DefaultWeightedEdge> graph = new WeightedMultigraph(DefaultWeightedEdge.class);
        stations.forEach(graph::addVertex);
        sections.forEach(section -> graph.setEdgeWeight(graph.addEdge(section.getUpStation(), section.getDownStation()), section.getDistance()));

        DijkstraShortestPath dijkstraShortestPath = new DijkstraShortestPath(graph);
        this.distance = dijkstraShortestPath.getPath(sourceStation, targetStation).getWeight();
        this.stations = dijkstraShortestPath.getPath(sourceStation, targetStation).getVertexList();
    }

    public List getStations() {
        return Collections.unmodifiableList(stations);
    }

    public double getDistance() {
        return distance;
    }
}
