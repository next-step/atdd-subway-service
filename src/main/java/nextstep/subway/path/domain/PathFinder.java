package nextstep.subway.path.domain;

import nextstep.subway.path.exception.InvalidFindShortestPathException;
import nextstep.subway.station.domain.Station;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

import java.util.List;

public class PathFinder {
    private final WeightedMultigraph<Station, DefaultWeightedEdge> stationGraph;
    private final DijkstraShortestPath dijkstraShortestPath;

    public PathFinder(SubwayMap subwayMap) {
        stationGraph = new WeightedMultigraph(DefaultWeightedEdge.class);

        generateStationGraph(subwayMap);

        dijkstraShortestPath = new DijkstraShortestPath(stationGraph);
    }

    public Path findShortestPath(Station source, Station target) {
        validate(source, target);

        List<Station> shortestPath = dijkstraShortestPath.getPath(source, target).getVertexList();
        int distance = (int) dijkstraShortestPath.getPath(source, target).getWeight();
        return new Path(shortestPath, distance);
    }

    private void generateStationGraph(SubwayMap subwayMap) {
        addVertex(subwayMap);
        setEdgeWeight(subwayMap);
    }

    private void addVertex(SubwayMap subwayMap) {
        subwayMap.getAllStations().forEach(stationGraph::addVertex);
    }

    private void setEdgeWeight(SubwayMap subwayMap) {
        subwayMap.getAllSections().forEach(section -> {
            Station upStation = section.getUpStation();
            Station downStation = section.getDownStation();
            int distance = section.getDistance().getDistance();

            stationGraph.setEdgeWeight(stationGraph.addEdge(upStation, downStation), distance);
        });
    }

    private void validate(Station source, Station target) {
        if (source.equals(target)) {
            throw new InvalidFindShortestPathException("출발역과 도착역이 같으면 조회 불가능합니다.");
        }
        if (isNotContainStation(source, target)) {
            throw new InvalidFindShortestPathException("출발역이나 도착역이 존재하지 않습니다.");
        }
        if (isNotConnectStations(source, target)) {
            throw new InvalidFindShortestPathException("출발역과 도착역이 연결이 되어 있지 않습니다.");
        }
    }

    private boolean isNotContainStation(Station source, Station target) {
        return !stationGraph.containsVertex(source) || !stationGraph.containsVertex(target);
    }

    private boolean isNotConnectStations(Station source, Station target) {
        return dijkstraShortestPath.getPath(source, target) == null;
    }
}
