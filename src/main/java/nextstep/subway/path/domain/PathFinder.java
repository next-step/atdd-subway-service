package nextstep.subway.path.domain;

import java.util.List;
import nextstep.subway.common.exception.InvalidParameterException;
import nextstep.subway.line.domain.Distance;
import nextstep.subway.line.domain.Lines;
import nextstep.subway.line.domain.Section;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.Stations;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

public class PathFinder {
    public static final String ERROR_MESSAGE_SAME_STATIONS = "출발역과 도착역은 같을 수 없습니다.";
    private final WeightedMultigraph<Station, DefaultWeightedEdge> graph = new WeightedMultigraph<>(DefaultWeightedEdge.class);
    private final DijkstraShortestPath<Station, DefaultWeightedEdge> path;

    private PathFinder(Lines lines) {
        addVertex(lines.stations());
        setEdgeWeight(lines.sections());
        path = new DijkstraShortestPath<>(graph);
    }

    public static PathFinder from(Lines lines) {
        return new PathFinder(lines);
    }

    private void addVertex(List<Station> stations) {
        stations.forEach(graph::addVertex);
    }

    private void setEdgeWeight(List<Section> sections) {
        sections.forEach(section -> graph.setEdgeWeight(addEdge(section), section.distanceValue()));
    }

    private DefaultWeightedEdge addEdge(Section section) {
        return graph.addEdge(section.upStation(), section.downStation());
    }

    public Path findShortestPath(Station departure, Station arrival) {
        validSameStations(departure, arrival);
        GraphPath<Station, DefaultWeightedEdge> shortestPath = path.getPath(departure, arrival);
        return Path.of(Stations.from(shortestPath.getVertexList()), Distance.from((int) shortestPath.getWeight()));
    }

    private void validSameStations(Station departure, Station arrival) {
        if (departure.isSameStation(arrival)) {
            throw new InvalidParameterException(ERROR_MESSAGE_SAME_STATIONS);
        }
    }
}
