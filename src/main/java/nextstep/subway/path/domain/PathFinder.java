package nextstep.subway.path.domain;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import nextstep.subway.exception.InvalidArgumentException;
import nextstep.subway.exception.NotFoundException;
import nextstep.subway.line.domain.Line;
import nextstep.subway.station.domain.Station;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.WeightedMultigraph;

public class PathFinder {
    private final WeightedMultigraph<Station, SectionEdge> graph;
    private final DijkstraShortestPath dijkstraShortestPath;

    private PathFinder() {
        graph = new WeightedMultigraph(SectionEdge.class);
        dijkstraShortestPath = new DijkstraShortestPath(graph);
    }

    public static PathFinder of(List<Line> lines) {
        return new PathFinder().createGraph(lines);
    }

    public Path findShortestPath(Station fromStation, Station toStation) {
        validate(fromStation, toStation);
        GraphPath graphPath = dijkstraShortestPath.getPath(fromStation, toStation);
        if (graphPath == null ) {
            throw new InvalidArgumentException("출발역과 도착역이 연결되어 있지 않습니다.");
        }
        return Path.from(graphPath);
    }

    private PathFinder createGraph(List<Line> lines) {
        for (Line line: lines) {
            addVertex(line.getStations());
            addEdgeWeight(SectionEdge.fromList(line.getSections()));
        }
        return this;
    }

    private  void addVertex(List<Station> stations) {
        for (Station s: stations) {
            graph.addVertex(s);
        }
    }

    private void addEdgeWeight(List<SectionEdge> sectionEdges) {
        for (SectionEdge se: sectionEdges) {
            graph.addEdge(se.getSource(), se.getTarget(), se);
            graph.setEdgeWeight(se, se.getDistance());
        }
    }

    private void validate(Station fromStation, Station toStation) {
        validSameStation(fromStation, toStation);
        validContains(fromStation, toStation);
    }

    private void validSameStation(Station fromStation, Station toStation) {
        if (fromStation.equals(toStation)) {
            throw new InvalidArgumentException("출발역과 도착역이 같습니다.");
        }
    }

    private void validContains(Station fromStation, Station toStation) {
        if (!(graph.containsVertex(fromStation) && graph.containsVertex(toStation))) {
            throw new NotFoundException("출발역 또는 도착역이 존재하지 않습니다.");
        }
    }

}
