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

import static nextstep.subway.exception.type.ValidExceptionType.IS_TARGET_ANS_SOURCE_SAME;

public class DijkstraShortestPathStrategy implements PathInterface {

    private final WeightedMultigraph<Station, DefaultWeightedEdge> graph = new WeightedMultigraph(DefaultWeightedEdge.class);

    private DijkstraShortestPathStrategy(List<Line> lines) {
        initSetting(lines);
    }

    public static DijkstraShortestPathStrategy from(List<Line> lines) {
        return new DijkstraShortestPathStrategy(lines);
    }

    private void initSetting(List<Line> lines) {
        lines.forEach(line -> {
            addVertex(line);
            addEdgeWeight(line);
        });
    }

    private void addEdgeWeight(Line line) {
        line.getSections()
                .forEach(section -> graph.setEdgeWeight(addEdge(section), section.getDistance()));
    }

    private DefaultWeightedEdge addEdge(Section section) {
        return graph.addEdge(section.getUpStation(), section.getDownStation());
    }


    private void addVertex(Line line) {
        line.getStations().forEach(graph::addVertex);
    }

    @Override
    public GraphPath<Station, DefaultWeightedEdge> getShortPath(Station source, Station target) {
        validCheckIsSameStation(source, target);

        DijkstraShortestPath<Station, DefaultWeightedEdge> dijkstraStrategy = new DijkstraShortestPath<>(graph);

        return dijkstraStrategy.getPath(source, target);
    }

    private void validCheckIsSameStation(Station source, Station target) {
        if (source.equals(target)) {
            throw new NotValidDataException(IS_TARGET_ANS_SOURCE_SAME.getMessage());
        }
    }
}
