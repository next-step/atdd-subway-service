package nextstep.subway.path.domain;

import static nextstep.subway.exception.ExceptionMessage.*;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import org.springframework.stereotype.Component;

import nextstep.subway.exception.BadRequestException;
import nextstep.subway.line.domain.Section;
import nextstep.subway.path.dto.PathFinderResponse;
import nextstep.subway.station.domain.Station;

@Component
public class GraphPathFinder implements PathFinder {

    @Override
    public PathFinderResponse getShortestPaths(Set<Section> sections, Station sourceStation, Station targetStation) {
        validate(sections, sourceStation, targetStation);
        GraphPath<Station, SubwayWeightedEdge> findPath = getPath(sections, sourceStation, targetStation);
        return new PathFinderResponse(findPath.getVertexList(), (int)findPath.getWeight(),
            getMaxLineSurcharge(findPath.getEdgeList()));
    }

    private GraphPath<Station, SubwayWeightedEdge> getPath(Set<Section> sections, Station sourceStation,
        Station targetStation) {
        Set<Station> stations = getAllStation(sections);
        WeightedMultigraph<Station, SubwayWeightedEdge> graph =
            new WeightedMultigraph(SubwayWeightedEdge.class);

        addVertexToGraph(stations, graph);
        setEdgeWeightToGraph(sections, graph);

        return new DijkstraShortestPath(graph).getPath(sourceStation, targetStation);
    }

    private void setEdgeWeightToGraph(Set<Section> sections,
        WeightedMultigraph<Station, SubwayWeightedEdge> graph) {
        for (Section section : sections) {
            SubwayWeightedEdge subwayWeightedEdge = createEdge(graph, section);
            graph.setEdgeWeight(subwayWeightedEdge, section.getDistance());
        }
    }

    private SubwayWeightedEdge createEdge(WeightedMultigraph<Station, SubwayWeightedEdge> graph,
        Section section) {
        SubwayWeightedEdge subwayWeightedEdge = graph.addEdge(section.getUpStation(),
            section.getDownStation());
        subwayWeightedEdge.setSurcharge(section.getLine().getSurcharge());
        return subwayWeightedEdge;
    }

    private void addVertexToGraph(Set<Station> stations, WeightedMultigraph<Station, SubwayWeightedEdge> graph) {
        for (Station station : stations) {
            graph.addVertex(station);
        }
    }

    private int getMaxLineSurcharge(List<SubwayWeightedEdge> edges) {
        return edges.stream()
            .map(SubwayWeightedEdge::getSurcharge)
            .max(Integer::compare)
            .orElse(0);
    }

    private void validate(Set<Section> sections, Station sourceStation, Station targetStation) {
        validateSameStation(sourceStation, targetStation);
        validateExistStation(sections, sourceStation, targetStation);
    }

    private void validateSameStation(Station sourceStation, Station targetStation) {
        if (sourceStation.equals(targetStation)) {
            throw new BadRequestException(SAME_STATION);
        }
    }

    private void validateExistStation(Set<Section> sections, Station sourceStation, Station targetStation) {
        Set<Station> allStation = getAllStation(sections);
        if (!allStation.contains(sourceStation) || !allStation.contains(targetStation)) {
            throw new BadRequestException(NOT_EXIST_STATION);
        }
    }

    private Set<Station> getAllStation(Set<Section> sections) {
        Set<Station> upStations = getUpStations(sections);
        Set<Station> downStations = getDownStations(sections);
        upStations.addAll(downStations);
        return upStations;
    }

    private Set<Station> getUpStations(Set<Section> sections) {
        return sections.stream()
            .map(Section::getUpStation)
            .collect(Collectors.toSet());
    }

    private Set<Station> getDownStations(Set<Section> sections) {
        return sections.stream()
            .map(Section::getDownStation)
            .collect(Collectors.toSet());
    }

    public static class SubwayWeightedEdge extends DefaultWeightedEdge {
        private int surcharge;

        public void setSurcharge(int surcharge) {
            this.surcharge = surcharge;
        }

        public int getSurcharge() {
            return surcharge;
        }
    }

}
