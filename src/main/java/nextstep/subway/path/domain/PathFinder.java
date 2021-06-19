package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Lines;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.Stations;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

import java.util.List;
import java.util.NoSuchElementException;

public class PathFinder {

    private DijkstraShortestPath dijkstraShortestPath;

    public PathFinder(Lines lines, Stations stations) {
        WeightedMultigraph<Station, DefaultWeightedEdge> graph = new WeightedMultigraph(DefaultWeightedEdge.class);

        initStationVertex(stations, graph);
        initSectionEdge(lines, graph);

        this.dijkstraShortestPath = new DijkstraShortestPath(graph);
    }

    private void initSectionEdge(Lines lines, WeightedMultigraph<Station, DefaultWeightedEdge> graph) {
        lines.getSections()
                .stream()
                .forEach(section -> graph.setEdgeWeight(graph.addEdge(section.getUpStation(), section.getDownStation()), section.getDistance()));
    }

    private void initStationVertex(Stations stations, WeightedMultigraph<Station, DefaultWeightedEdge> graph) {
        stations.getStations()
                .stream()
                .forEach(station -> graph.addVertex(station));
    }

    public List<Station> getPath(Station source, Station target) {
        try {
            return dijkstraShortestPath.getPath(source, target)
                    .getVertexList();
        } catch (NullPointerException e) {
            throw new NoSuchElementException("출발지와 목적지는 연결되어있지 않는 경로입니다.");
        }

    }

    public int getWeight(Station source, Station target) {
        return (int) dijkstraShortestPath.getPath(source, target).getWeight();
    }
}
