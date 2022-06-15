package nextstep.subway.path.application;

import java.util.List;
import java.util.stream.Collectors;
import nextstep.subway.common.exception.EntityNotFoundException;
import nextstep.subway.common.exception.ErrorCode;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import nextstep.subway.path.domain.DiscountType;
import nextstep.subway.path.domain.Path;
import nextstep.subway.path.exception.StationNotConnectException;
import nextstep.subway.station.domain.Station;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.WeightedMultigraph;

public class PathFinder {

    private WeightedMultigraph<Station, SectionEdge> pathGraph = new WeightedMultigraph<>(SectionEdge.class);
    private DijkstraShortestPath<Station, SectionEdge> shortestPath;

    public PathFinder(List<Line> lines) {
        addVertexes(lines);
        setEdgeWeights(lines);
        shortestPath = new DijkstraShortestPath<>(pathGraph);
    }

    private void addVertexes(List<Line> lines) {
        lines.stream()
                .flatMap(line -> line.getStations().stream())
                .collect(Collectors.toSet())
                .forEach(station -> pathGraph.addVertex(station));
    }

    private void setEdgeWeights(List<Line> lines) {
        lines.stream()
                .flatMap(line -> line.getSections().stream())
                .forEach(section -> setEdgeWeight(pathGraph, section));
    }

    private void setEdgeWeight(WeightedMultigraph<Station, SectionEdge> pathGraph, Section section) {
        SectionEdge sectionEdge = new SectionEdge(section);
        pathGraph.addEdge(section.getUpStation(), section.getDownStation(), sectionEdge);
        pathGraph.setEdgeWeight(sectionEdge, section.getDistance());
    }

    public Path getPath(Station sourceStation, Station targetStation, DiscountType discountType) {
        List<Station> pathStations = getPathStations(sourceStation, targetStation);
        int distance = getDistance(sourceStation, targetStation);
        int addFee = getAddFee(sourceStation, targetStation);
        return new Path(pathStations, distance, addFee, discountType);
    }

    private List<Station> getPathStations(Station source, Station target) {
        return getGraphPath(source, target).getVertexList();
    }

    private GraphPath<Station, SectionEdge> getGraphPath(Station source, Station target) {
        GraphPath<Station, SectionEdge> graphPath;
        try {
            graphPath = shortestPath.getPath(source, target);
        } catch (IllegalArgumentException e) {
            throw new EntityNotFoundException(ErrorCode.STATION_NOT_FOUND);
        }
        checkConnected(graphPath);
        return graphPath;
    }

    private void checkConnected(GraphPath<Station, SectionEdge> graphPath) {
        if (graphPath == null) {
            throw new StationNotConnectException();
        }
    }

    private int getDistance(Station source, Station target) {
        return (int) shortestPath.getPath(source, target).getWeight();
    }

    private Integer getAddFee(Station source, Station target) {
        return shortestPath.getPath(source, target).getEdgeList().stream()
            .map(SectionEdge::getAddFee)
            .max(Integer::compareTo).orElse(0);
    }
}
