package nextstep.subway.path.application;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.path.exception.NoLinkPathException;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.dto.StationResponse;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

import java.util.List;
import java.util.stream.Collectors;

public class PathFinder {

    private WeightedMultigraph<Station, DefaultWeightedEdge> graph;

    public PathFinder(List<Line> lines) {
        graph = buildGraph(lines);
    }

    public PathResponse getPath(Station sourceStation, Station targetStation) {
        verifySameSourceTargetStation(sourceStation, targetStation);

        GraphPath shortestPath = getShortestPath(sourceStation, targetStation);
        verifyNoLinkPath(shortestPath);

        List<Station> stations = shortestPath.getVertexList();
        return new PathResponse(stations.stream()
                .map(station -> StationResponse.of(station))
                .collect(Collectors.toList()), (int) shortestPath.getWeight());
    }

    private void verifySameSourceTargetStation(Station sourceStation, Station targetStation) {
        if (sourceStation.equals(targetStation)) {
            throw new IllegalArgumentException(
                    String.format("출발지와 목적지가 같습니다. (id: %s - %s)", sourceStation.getId(), sourceStation.getName())
            );
        }
    }

    private void verifyNoLinkPath(GraphPath shortestPath) {
        if (shortestPath == null) {
            throw new NoLinkPathException("출발역과 도착역이 연결되어 있지 않습니다.");
        }
    }

    private GraphPath getShortestPath(Station sourceStation, Station targetStation) {
        DijkstraShortestPath dijkstraShortestPath = new DijkstraShortestPath(graph);
        return dijkstraShortestPath.getPath(sourceStation, targetStation);
    }

    private WeightedMultigraph<Station, DefaultWeightedEdge> buildGraph(List<Line> lines) {
        WeightedMultigraph<Station, DefaultWeightedEdge> graph = new WeightedMultigraph(DefaultWeightedEdge.class);
        for (Line line : lines) {
            buildGraphVertex(graph, line);
            buildGraphEdges(graph, line);
        }
        return graph;
    }

    private void buildGraphVertex(WeightedMultigraph<Station, DefaultWeightedEdge> graph, Line line) {
        List<Station> stations = line.getStations();
        for (Station station : stations) {
            graph.addVertex(station);
        }
    }

    private void buildGraphEdges(WeightedMultigraph<Station, DefaultWeightedEdge> graph, Line line) {
        List<Section> sections = line.getSections();
        for (Section section : sections) {
            int distance = section.getDistance();
            graph.setEdgeWeight(graph.addEdge(section.getUpStation(), section.getDownStation()), distance);
        }
    }
}
