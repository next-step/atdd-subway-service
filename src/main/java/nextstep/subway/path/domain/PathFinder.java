package nextstep.subway.path.domain;

import nextstep.subway.BaseEntity;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import nextstep.subway.station.domain.Station;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

import java.util.List;
import java.util.stream.Collectors;

public class PathFinder extends BaseEntity {
    private final List<Line> lines;
    private final WeightedMultigraph<Station, DefaultWeightedEdge> graph = new WeightedMultigraph<>(DefaultWeightedEdge.class);
    private final DijkstraShortestPath<Station, DefaultWeightedEdge> dijkstraShortestPath = new DijkstraShortestPath<>(graph);
    private GraphPath<Station, DefaultWeightedEdge> resultPath;

    public PathFinder(List<Line> lines) {
        this.lines = lines;
        createStationGraph();
    }

    public List<Station> getStationShortestPath() {
        return resultPath.getVertexList();
    }

    public int getDistanceShortestPath() {
        return (int) resultPath.getWeight();
    }

    private void createStationGraph() {
        addVertexStation();
        addEdgeSection();
    }

    private void addEdgeSection() {
        lines.forEach(line -> line.getSections().forEach(this::addEdgeWeight));
    }

    private void addEdgeWeight(Section section) {
        graph.setEdgeWeight(graph.addEdge(section.getUpStation(), section.getDownStation()), section.getDistance());
    }

    private void addVertexStation() {
        List<Station> stations = lines.stream().flatMap(line -> line.getStations().stream())
                .collect(Collectors.toList());
        stations.forEach(graph::addVertex);
    }

    public void findShortestPath(Station sourceStation, Station targetStation) {
        if(sourceStation.equals(targetStation)) {
            throw new RuntimeException("출발역과 도착역이 동일합니다.");
        }

        System.out.println("1234:"+dijkstraShortestPath.getPath(sourceStation, targetStation));
        if(dijkstraShortestPath.getPath(sourceStation, targetStation) == null) {
            throw new RuntimeException("출발역과 도착역이 연결되어있지 않습니다.");
        }

        this.resultPath = dijkstraShortestPath.getPath(sourceStation, targetStation);
    }
}
