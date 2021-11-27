package nextstep.subway.path.domain;

import nextstep.subway.exception.SectionException;
import nextstep.subway.exception.error.ErrorCode;
import nextstep.subway.line.domain.section.Section;
import nextstep.subway.station.domain.Station;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class PathFinder {
    private final DijkstraShortestPath dijkstraShortestPath;
    private final StationGraph stationGraph;
    private final List<Section> sections;

    public PathFinder(List<Section> sections) {
        this.sections = sections;
        this.stationGraph = new StationGraph();
        stationGraph.addAllVertex(sections);
        stationGraph.addAllEdgeAndEdgeWeight(sections);

        this.dijkstraShortestPath = new DijkstraShortestPath<>(stationGraph);
    }

    public List<Station> getDijkstraShortestPath(Station source, Station target) {
        validateLineStation(source, target);

        return getDijkstraStations(source, target);
    }

    public int getSumLineStationsDistance(Station source, Station target) {
        GraphPath<Station, SectionEdge> path = dijkstraShortestPath.getPath(source, target);

        return (int) path.getEdgeList()
                .stream()
                .mapToDouble(SectionEdge::getWeight)
                .sum();
    }

    private void validateLineStation(Station source, Station target) {
        boolean isExistSource = stationGraph.getStations(sections)
                .stream()
                .anyMatch(it -> it == source);
        boolean isExistTarget = stationGraph.getStations(sections)
                .stream()
                .anyMatch(it -> it == target);

        if (!isExistSource || !isExistTarget) {
            throw new SectionException(ErrorCode.NOT_FOUND_ENTITY, "검색 역이 잘못되었습니다.");
        }
        if (source == target) {
            throw new SectionException(ErrorCode.BAD_ARGUMENT, "출발역과 도착역이 동일합니다.");
        }
    }

    private List<Station> getDijkstraStations(Station source, Station target) {
        GraphPath<Station, SectionEdge> path = dijkstraShortestPath.getPath(source, target);
        return path.getVertexList();
    }

}
