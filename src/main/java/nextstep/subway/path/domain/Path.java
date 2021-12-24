package nextstep.subway.path.domain;

import nextstep.subway.common.exception.InvalidDataException;
import nextstep.subway.line.domain.Distance;
import nextstep.subway.line.domain.Lines;
import nextstep.subway.station.domain.Station;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.interfaces.ShortestPathAlgorithm;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;

import java.util.List;

public class Path {

    private static final int CONNECTED_STATION_COUNT = 2;

    private SubwayGraph subwayGraph = new SubwayGraph(SectionEdge.class);
    private Station source;
    private Station target;

    public Path(final Station source, final Station target) {
        validate(source, target);
        this.source = source;
        this.target = target;
    }

    private void validate(final Station source, final Station target) {
        if (source.equals(target)) {
            throw new InvalidDataException("출발역과 도착역은 동일할 수 없습니다.");
        }
    }

    public List<Station> findShortestPath(final Lines lines) {
        validateStationsWithSameLine(lines);
        addVertex(lines);
        addEdge(lines);
        validateStations();
        return findShortestPaths().getVertexList();
    }

    private void validateStationsWithSameLine(final Lines lines) {
        long existStationCount = lines.stations()
                .stream()
                .filter(station -> station.equals(source) || station.equals(target))
                .count();

        if (existStationCount != CONNECTED_STATION_COUNT) {
            throw new InvalidDataException("출발역과 도착역이 다른 노선입니다.");
        }
    }

    private void addVertex(final Lines lines) {
        lines.stations().forEach(subwayGraph::addVertex);
    }

    private void addEdge(final Lines lines) {
        lines.sections().forEach(subwayGraph::addEdge);
    }

    private void validateStations() {
        GraphPath<Station, SectionEdge> shortestPaths = findShortestPaths();

        if (shortestPaths == null) {
            throw new InvalidDataException("출발역과 도착역이 연결되지 않았습니다.");
        }
    }

    private GraphPath<Station, SectionEdge> findShortestPaths() {
        DijkstraShortestPath<Station, SectionEdge> dijkstraShortestPath = new DijkstraShortestPath(subwayGraph);
        ShortestPathAlgorithm.SingleSourcePaths<Station, SectionEdge> singleSourcePaths = dijkstraShortestPath.getPaths(source);
        return singleSourcePaths.getPath(target);
    }

    public Distance findShortestDistance(final Lines lines, final List<Station> shortestPaths) {
        int totalDistance = lines.sections()
                .stream()
                .mapToInt(section -> section.getDistanceOfSection(shortestPaths))
                .sum();

        return Distance.of(totalDistance);
    }

}
