package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import nextstep.subway.path.exception.InvalidFindShortestPathException;
import nextstep.subway.station.domain.Station;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

import java.util.List;
import java.util.stream.Collectors;

public class SubwayMap {
    private final WeightedMultigraph<Station, DefaultWeightedEdge> stationGraph;
    private final DijkstraShortestPath dijkstraShortestPath;
    private final List<Line> lines;

    public SubwayMap(List<Line> lines) {
        this.lines = lines;
        stationGraph = new WeightedMultigraph(DefaultWeightedEdge.class);
        generateStationGraph();
        dijkstraShortestPath = new DijkstraShortestPath(stationGraph);
    }

    public Path findShortestPath(Station source, Station target) {
        validate(source, target);

        List<Station> shortestPath = dijkstraShortestPath.getPath(source, target).getVertexList();
        int distance = (int) dijkstraShortestPath.getPath(source, target).getWeight();
        return new Path(shortestPath, distance);
    }

    private void generateStationGraph() {
        addVertex();
        setEdgeWeight();
    }

    private void addVertex() {
        getAllStations().forEach(stationGraph::addVertex);
    }

    private void setEdgeWeight() {
        getAllSections().forEach(section -> {
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

    private List<Station> getAllStations() {
        return lines.stream()
                .flatMap(line -> line.getStations().stream())
                .collect(Collectors.toList());
    }

    private List<Section> getAllSections() {
        return lines.stream()
                .map(Line::getSections)
                .flatMap(it -> it.getSections().stream())
                .collect(Collectors.toList());
    }
}
