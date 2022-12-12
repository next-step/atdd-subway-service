package nextstep.subway.path.domain;

import nextstep.subway.exception.NotValidDataException;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import nextstep.subway.station.domain.Station;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

import java.util.List;
import java.util.Objects;

import static nextstep.subway.exception.type.ValidExceptionType.IS_TARGET_ANS_SOURCE_SAME;
import static nextstep.subway.exception.type.ValidExceptionType.NOT_CONNECT_STATION;

public class DijkstraShortestPathStrategy implements PathStrategy {

    private final List<Line> lines;

    public DijkstraShortestPathStrategy(List<Line> lines) {
        this.lines = lines;
    }

    private void initSetting(List<Line> lines, WeightedMultigraph<Station, DefaultWeightedEdge> graph) {
        lines.forEach(line -> {
            addVertex(line, graph);
            addEdgeWeight(line, graph);
        });
    }

    private void addEdgeWeight(Line line, WeightedMultigraph<Station, DefaultWeightedEdge> graph) {
        line.getSections()
                .forEach(section -> graph.setEdgeWeight(addEdge(section, graph), section.getDistance()));
    }

    private DefaultWeightedEdge addEdge(Section section, WeightedMultigraph<Station, DefaultWeightedEdge> graph) {
        return graph.addEdge(section.getUpStation(), section.getDownStation());
    }


    private void addVertex(Line line, WeightedMultigraph<Station, DefaultWeightedEdge> graph) {
        line.getStations().forEach(graph::addVertex);
    }

    public PathFinder getShortPath(Station source, Station target) {
        validCheckIsSameStation(source, target);

        WeightedMultigraph<Station, DefaultWeightedEdge> graph = new WeightedMultigraph(DefaultWeightedEdge.class);
        initSetting(lines, graph);
        DijkstraShortestPath<Station, DefaultWeightedEdge> dijkstraStrategy = new DijkstraShortestPath<>(graph);

        GraphPath<Station, DefaultWeightedEdge> graphResult = dijkstraStrategy.getPath(source, target);
        validCheckConnectStation(graphResult);


        return PathFinder.from(graphResult);
    }

    private void validCheckConnectStation(GraphPath<Station, DefaultWeightedEdge> graph) {
        if (Objects.isNull(graph)) {
            throw new NotValidDataException(NOT_CONNECT_STATION.getMessage());
        }
    }

    private void validCheckIsSameStation(Station source, Station target) {
        if (source.equals(target)) {
            throw new NotValidDataException(IS_TARGET_ANS_SOURCE_SAME.getMessage());
        }
    }
}
