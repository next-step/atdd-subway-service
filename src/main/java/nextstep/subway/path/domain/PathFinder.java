package nextstep.subway.path.domain;

import java.util.List;
import nextstep.subway.exception.SubwayException;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import nextstep.subway.station.domain.Station;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.WeightedMultigraph;
import org.springframework.http.HttpStatus;

public class PathFinder {
    private final WeightedMultigraph<Long, SectionEdge> graph = new WeightedMultigraph<>(SectionEdge.class);
    public static PathFinder of(List<Line> lines) {
        return new PathFinder(lines);
    }

    private PathFinder(List<Line> lines) {
        makeGraph(lines);
    }

    public Path findShortestPath(Station source, Station target) {
        validatePathFinder(source, target);
        try {
            DijkstraShortestPath dijkstraShortestPath = new DijkstraShortestPath(graph);
            GraphPath<Long, SectionEdge> graphPath = dijkstraShortestPath.getPath(source.getId(), target.getId());
            int distance = (int) dijkstraShortestPath.getPath(source.getId(), target.getId()).getWeight();
            return new Path(graphPath.getVertexList(), distance, graphPath.getEdgeList());
        }
        catch (RuntimeException e) {
            throw new SubwayException(HttpStatus.BAD_REQUEST, "경로 탐색에 실패하였습니다.");
        }
    }

    private void validatePathFinder(Station source, Station target) {
        if (source.getId().equals(target.getId())) {
            throw new SubwayException(HttpStatus.BAD_REQUEST, "동일한 두 구간에 대한 탐색은 할 수 없습니다.");
        }
    }

    private void makeGraph(List<Line> lines) {
        lines.forEach(line-> {
            addVertex(graph, line);
            setEdgeWeight(graph, line);
        });
    }

    private void addVertex(WeightedMultigraph<Long, SectionEdge> graph, Line line) {
        for (Station station : line.getStations()) {
            graph.addVertex(station.getId());
        }
    }

    private void setEdgeWeight(WeightedMultigraph<Long, SectionEdge> graph, Line line) {
        for (Section section : line.getSections()) {
            SectionEdge edge = graph.addEdge(section.getUpStationId(), section.getDownStationId());
            graph.setEdgeWeight(edge, section.getDistance());
            edge.addSection(section);
        }
    }
}
