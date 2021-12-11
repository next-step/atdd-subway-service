package nextstep.subway.path.domain;

import nextstep.subway.common.exception.NotFoundException;
import nextstep.subway.common.exception.PathDisconnectedException;
import nextstep.subway.common.exception.PathSameException;
import nextstep.subway.line.domain.Distance;
import nextstep.subway.line.domain.Section;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.domain.Station;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class PathFinder {
    private final Station sourceStation;
    private final Station targetStation;
    private final List<Section> sections;

    private PathFinder(final Station sourceStation, final Station targetStation, final List<Section> sections) {
        this.sourceStation = sourceStation;
        this.targetStation = targetStation;
        this.sections = sections;
    }

    public static PathFinder of(final Station sourceStation, final Station targetStation, final List<Section> sections) {
        return new PathFinder(sourceStation, targetStation, sections);
    }

    public List<Section> getSections() {
        return sections;
    }

    public PathResponse findShortestPath() {
        validateSameStation(sourceStation, targetStation);
        final WeightedMultigraph<Station, DefaultWeightedEdge> graph = new WeightedMultigraph<>(DefaultWeightedEdge.class);
        addVertexes(graph, getStations());
        addEdges(graph, sections);

        final DijkstraShortestPath<Station, DefaultWeightedEdge> shortestPath = new DijkstraShortestPath<>(graph);
        final GraphPath<Station, DefaultWeightedEdge> graphPath = shortestPath.getPath(sourceStation, targetStation);
        validateFoundBothStations(sourceStation, targetStation);
        validateConnectedStations(graphPath);
        return PathResponse.of(graphPath.getVertexList(), graphPath.getWeight());
    }

    private void validateSameStation(final Station sourceStation, final Station targetStation) {
        if (sourceStation.equals(targetStation)) {
            throw new PathSameException();
        }
    }

    private void validateConnectedStations(final GraphPath<Station, DefaultWeightedEdge> graphPath) {
        if (graphPath == null) {
            throw new PathDisconnectedException();
        }
    }

    private void validateFoundBothStations(final Station sourceStation, final Station targetStation) {
        if (!getStations().contains(sourceStation) || !getStations().contains(targetStation)) {
            throw new NotFoundException();
        }
    }

    private List<Station> getStations() {
        Set<Station> uniqueStations = new HashSet<>();
        sections.forEach(
                section -> {
                    uniqueStations.add(section.getUpStation());
                    uniqueStations.add(section.getDownStation());
                }
        );
        return new ArrayList<>(uniqueStations);
    }

    private void addVertexes(final WeightedMultigraph<Station, DefaultWeightedEdge> graph, final List<Station> stations) {
        stations.forEach(graph::addVertex);
    }

    private void addEdges(final WeightedMultigraph<Station, DefaultWeightedEdge> graph, final List<Section> sections) {
        for (Section section: sections) {
            Station upStation = section.getUpStation();
            Station downStation = section.getDownStation();
            Distance distance = section.getDistance();
            graph.setEdgeWeight(graph.addEdge(upStation, downStation), distance.getDistance());
        }
    }
}
