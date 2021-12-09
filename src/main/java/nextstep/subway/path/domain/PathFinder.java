package nextstep.subway.path.domain;

import nextstep.subway.common.exception.CyclePathException;
import nextstep.subway.common.exception.NotFoundEntityException;
import nextstep.subway.common.exception.UnconnectedStationException;
import nextstep.subway.line.domain.Distance;
import nextstep.subway.line.domain.Section;
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
    private Station sourceStation;
    private Station targetStation;
    private List<Section> sections;

    public PathFinder(Station sourceStation, Station targetStation, List<Section> sections) {
        validateSameStation(sourceStation, targetStation);
        this.sourceStation = sourceStation;
        this.targetStation = targetStation;
        this.sections = sections;
    }

    private void validateSameStation(Station sourceStation, Station targetStation) {
        if (sourceStation.equals(targetStation)) {
            throw new CyclePathException();
        }
    }

    public Station getSourceStation() {
        return sourceStation;
    }

    public Station getTargetStation() {
        return targetStation;
    }

    public List<Section> getSections() {
        return sections;
    }

    public GraphPath<Station, DefaultWeightedEdge> findShortestPath() {
        WeightedMultigraph<Station, DefaultWeightedEdge> graph = new WeightedMultigraph<>(DefaultWeightedEdge.class);
        addVertexes(graph, getAllStations());
        addEdges(graph, sections);

        DijkstraShortestPath<Station, DefaultWeightedEdge> shortestPath = new DijkstraShortestPath<>(graph);
        validateFindShortestPath(sourceStation, targetStation, shortestPath);

        return shortestPath.getPath(sourceStation, targetStation);
    }

    private void validateFindShortestPath(Station sourceStation, Station targetStation, DijkstraShortestPath<Station, DefaultWeightedEdge> shortestPath) {
        validateFoundBothStations(sourceStation, targetStation);
        validateConnectedStations(sourceStation, targetStation, shortestPath);
    }

    private void validateConnectedStations(Station sourceStation, Station targetStation, DijkstraShortestPath<Station, DefaultWeightedEdge> shortestPath) {
        if (shortestPath.getPath(sourceStation, targetStation) == null) {
            throw new UnconnectedStationException();
        }
    }

    private void validateFoundBothStations(Station sourceStation, Station targetStation) {
        if (!getAllStations().contains(sourceStation) || !getAllStations().contains(targetStation)) {
            throw new NotFoundEntityException();
        }
    }

    private List<Station> getAllStations() {
        Set<Station> uniqueStations = new HashSet<>();
        sections.forEach(
                section -> {
                    uniqueStations.add(section.getUpStation());
                    uniqueStations.add(section.getDownStation());
                }
        );

        return new ArrayList<>(uniqueStations);
    }

    private void addVertexes(WeightedMultigraph<Station, DefaultWeightedEdge> graph, List<Station> stations) {
        stations.forEach(graph::addVertex);
    }

    private void addEdges(WeightedMultigraph<Station, DefaultWeightedEdge> graph, List<Section> sections) {
        for (Section section: sections) {
            Station upStation = section.getUpStation();
            Station downStation = section.getDownStation();
            Distance distance = section.getDistance();
            graph.setEdgeWeight(graph.addEdge(upStation, downStation), distance.getDistance());
        }
    }
}
