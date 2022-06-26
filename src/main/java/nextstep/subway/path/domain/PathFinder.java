package nextstep.subway.path.domain;

import nextstep.subway.exception.NotConnectStationException;
import nextstep.subway.fare.domain.Fare;
import nextstep.subway.fare.domain.FareCalculator;
import nextstep.subway.line.domain.Distance;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import nextstep.subway.station.domain.Station;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.util.List;

@Component
public class PathFinder {
    private final WeightedMultigraph<Station, DefaultWeightedEdge> graph = new WeightedMultigraph<>(DefaultWeightedEdge.class);
    private final FareCalculator fareCalculator;
    private final DijkstraShortestPath shortestPath = new DijkstraShortestPath(graph);

    public PathFinder(FareCalculator fareCalculator) {
        this.fareCalculator = fareCalculator;
    }

    public Path findShortest(List<Line> lines, Station source, Station target) {
        for (Line line : lines) {
            addSectionToGraph(line);
        }

        GraphPath path = shortestPath.getPath(source, target);
        if (ObjectUtils.isEmpty(path)) {
            throw new NotConnectStationException("출발지와 도착지가 연결되지 않음.");
        }

        double pathWeight = shortestPath.getPathWeight(source, target);
        Fare fare = fareCalculator.calculateWithoutAge(Distance.from((int) pathWeight), lines);
        return Path.of(path.getVertexList(), Distance.from((int) pathWeight), fare);
    }

    private void addSectionToGraph(Line line) {
        for (Section section : line.getSections()) {
            graph.addVertex(section.getUpStation());
            graph.addVertex(section.getDownStation());
            graph.setEdgeWeight(graph.addEdge(section.getUpStation(), section.getDownStation()), section.getDistanceValue());
        }
    }
}
