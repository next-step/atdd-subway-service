package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Section;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.dto.StationResponse;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class DijkstraShortestPathCalculator implements ShortestPathCalculator {
    private final WeightedMultigraph<Station, DefaultWeightedEdge> graph;
    private DijkstraShortestPath dijkstraShortestPath;

    public DijkstraShortestPathCalculator() {
        this.graph = new WeightedMultigraph(DefaultWeightedEdge.class);
    }

    @Override
    public PathResponse calculatePath(List<Section> sections, Station sourceStation, Station targetStation) {
        addVertexAndEdge(sections, graph);
        createDijkstraShortestPath();

        GraphPath path = Optional.ofNullable(dijkstraShortestPath.getPath(sourceStation, targetStation))
                .orElseThrow(() -> new IllegalArgumentException("출발역과 도착역이 연결이 되어 있지 않습니다."));

        return new PathResponse(getStationRespone(path.getVertexList()), (int)Math.round(path.getWeight()));
    }

    private void createDijkstraShortestPath() {
        dijkstraShortestPath = new DijkstraShortestPath(graph);
    }

    private void addVertexAndEdge(List<Section> sections, WeightedMultigraph<Station, DefaultWeightedEdge> graph) {
        List<Station> stations = findStations(sections);

        for (Station station : stations) {
            graph.addVertex(station);
        }
        for (Section section : sections) {
            graph.setEdgeWeight(graph.addEdge(section.getUpStation(), section.getDownStation()), section.getDistance());
        }
    }

    private List<Station> findStations(List<Section> sections) {
        return sections.stream()
                .flatMap(Section::stations)
                .distinct()
                .collect(Collectors.toList());
    }

    private List<StationResponse> getStationRespone(List<Station> stations) {
        return stations.stream()
                .map(station -> StationResponse.of(station))
                .collect(Collectors.toList());
    }
}
