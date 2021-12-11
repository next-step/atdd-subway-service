package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.dto.StationResponse;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class PathFinder {
    public static PathResponse findPath(List<Line> lines, Station sourceStation, Station targetStation) {
        validateSourceEqualsTarget(sourceStation, targetStation);
        List<Section> sections = findSections(lines);

        WeightedMultigraph<Station, DefaultWeightedEdge> graph
                = new WeightedMultigraph(DefaultWeightedEdge.class);
        addVertexAndEdge(sections, graph);

        DijkstraShortestPath dijkstraShortestPath
                = new DijkstraShortestPath(graph);
        Optional<GraphPath> optPath = Optional.ofNullable(dijkstraShortestPath.getPath(sourceStation, targetStation));

        GraphPath path = optPath.orElseThrow(() -> new IllegalArgumentException("출발역과 도착역이 연결이 되어 있지 않습니다."));

        return new PathResponse(getStationRespone(path.getVertexList()), (int)Math.round(path.getWeight()));
    }

    private static void validateSourceEqualsTarget(Station sourceStation, Station targetStation) {
        if (sourceStation.equals(targetStation)) {
            throw new IllegalArgumentException("출발역과 도착역이 같습니다.");
        }
    }

    private static List<Section> findSections(List<Line> lines) {
        List<Section> sections = new ArrayList<>();
        lines.stream()
                .map(line -> line.getSections())
                .forEach(sections::addAll);
        return sections;
    }

    private static void addVertexAndEdge(List<Section> sections, WeightedMultigraph<Station, DefaultWeightedEdge> graph) {
        List<Station> stations = findStations(sections);

        for (Station station : stations) {
            graph.addVertex(station);
        }
        for (Section section : sections) {
            graph.setEdgeWeight(graph.addEdge(section.getUpStation(), section.getDownStation()), section.getDistance());
        }
    }

    private static List<Station> findStations(List<Section> sections) {
        return sections.stream()
                .flatMap(Section::stations)
                .distinct()
                .collect(Collectors.toList());
    }

    private static List<StationResponse> getStationRespone(List<Station> stations) {
        return stations.stream()
                .map(station -> StationResponse.of(station))
                .collect(Collectors.toList());
    }
}
