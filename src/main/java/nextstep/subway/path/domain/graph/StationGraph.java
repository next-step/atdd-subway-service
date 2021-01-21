package nextstep.subway.path.domain.graph;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import nextstep.subway.path.domain.Path;
import nextstep.subway.path.domain.fare.FareCalculator;
import nextstep.subway.path.exception.InvalidFindShortestPathException;
import nextstep.subway.station.domain.Station;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class StationGraph {
    private final WeightedMultigraph<Station, DefaultWeightedEdge> stationGraph;
    private final ShortestPathFinder shortestPathFinder;
    private final List<Line> lines;

    public StationGraph(List<Line> lines, ShortestPathFinder shortestPathFinder) {
        this.stationGraph = new WeightedMultigraph<>(DefaultWeightedEdge.class);
        this.lines = lines;
        generateGraph();
        this.shortestPathFinder = shortestPathFinder.addGraph(stationGraph);
    }

    private void generateGraph() {
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

    public Path findShortestPath(Station source, Station target) {
        validate(source, target);

        List<Station> shortestPath = shortestPathFinder.getShortestPath(source, target);
        int distance = shortestPathFinder.getDistance(source, target);
        int fare = FareCalculator.calculateFare(distance, getSurchargesOfLine(shortestPath));

        return new Path(shortestPath, distance, fare);
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
        return shortestPathFinder.isNotConnectStations(source, target);
    }

    private List<Integer> getSurchargesOfLine(final List<Station> stations) {
        List<Integer> surcharges = new ArrayList<>();
        for (int index = 0; index < stations.size() - 1; index++) {
            int finalIndex = index;
            getAllSections().stream()
                    .filter(section -> section.isEqualStations(stations.get(finalIndex), stations.get(finalIndex + 1)))
                    .findFirst()
                    .ifPresent(section -> surcharges.add(section.getSurcharge()));
        }
        return surcharges;
    }
}
