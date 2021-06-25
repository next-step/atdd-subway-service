package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import nextstep.subway.station.domain.Station;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.alg.shortestpath.KShortestPaths;
import org.jgrapht.graph.WeightedMultigraph;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class Path {
    private static final String SAME_SOURCE_TARGET_EXCEPTION = "출발지와 도착지가 같은 경로는 검색할수 없습니다.";
    private static final String SOURCE_TARGET_EXCEPTION = "역이 연결되지 않았거나 등록되지 않았습니다.";

    private Station source;
    private Station target;
    private WeightedMultigraph<Station, LineSection> graph;

    public Path(Station source, Station target) {
        if (source.equals(target)){
            throw new RuntimeException(SAME_SOURCE_TARGET_EXCEPTION);
        }

        this.source = source;
        this.target = target;
        this.graph = new WeightedMultigraph(LineSection.class);
    }

    public List<Station> findShortPath(List<Line> lines) {
        setUpPath(lines);
        DijkstraShortestPath dijkstraShortestPath = new DijkstraShortestPath(graph);
        return dijkstraShortestPath.getPath(source, target).getVertexList();
    }

    private void setUpPath(List<Line> lines) {
        List<Station> stations = assembleStations(lines);
        validStations(stations);
        addVertex(lines);
        addEdge(lines);
    }

    private List<Station> toStationList(List<String> shortestPath, List<Station> stations) {
        List<Station> resultStationList = new ArrayList<>();
        for (String stationName: shortestPath) {
            Optional<Station> station = toStation(stations, stationName);
            resultStationList.add(station.get());
        }
        return resultStationList;
    }

    public Optional<Station> toStation(List<Station> stations, String stationName) {
        return stations.stream()
                .filter(station -> station.getName().equals(stationName))
                .findFirst();
    }

    public void addVertex(List<Line> lines) {
        lines.stream()
                .flatMap(it -> it.assembleStations().stream())
                .distinct()
                .collect(Collectors.toList())
                .forEach(station -> graph.addVertex(station));
    }

    public void addEdge(List<Line> lines) {
        for (Line line : lines) {
            line.getSections().stream()
                    .forEach(it -> addEdgeWeight(it, line));
        }
    }

    private void addEdgeWeight(Section section, Line line) {
        LineSection lineSection = new LineSection(section, line.getId());
        graph.addEdge(section.upStation(), section.downStation(), lineSection);
        graph.setEdgeWeight(lineSection, section.distance());
    }

    public void validStations(List<Station> stations) {
        boolean hasSourceStation = hasStations(stations, source);
        boolean hasTargetStation = hasStations(stations, target);

        if (!hasSourceStation || !hasTargetStation) {
            throw new RuntimeException(SOURCE_TARGET_EXCEPTION);
        }
    }

    private boolean hasStations(List<Station> stations, Station inputStation) {
        return stations.stream()
                .anyMatch(station -> station.equals(inputStation));
    }

    public List<Station> assembleStations(List<Line> lines) {
        return lines.stream()
                .flatMap(it -> it.assembleStations().stream())
                .distinct()
                .collect(Collectors.toList());
    }

    public int calculateDistance(int pathNumber) {
        List<GraphPath> paths = new KShortestPaths(graph, pathNumber).getPaths(source, target);
        return paths.stream()
                .mapToInt(it -> (int)it.getWeight())
                .sum();
    }
}
