package nextstep.subway.station.domain;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import nextstep.subway.line.domain.Sections;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

import java.util.List;

public class PathFinder {

    private WeightedMultigraph<Station, DefaultWeightedEdge> graph = new WeightedMultigraph(DefaultWeightedEdge.class);

    public PathFinder(List<Line> lines) {
        addVertexes(lines);
        addEdges(lines);
    }

    private void addVertexes(List<Line> lines) {
        for (Line line : lines) {
            addVertex(line);
        }
    }

    private void addVertex(Line line) {
        for (Station station : line.stations()) {
            graph.addVertex(station);
        }
    }

    private void addEdges(List<Line> lines) {
        for (Line line : lines) {
            addEdge(line.sections());
        }
    }

    private void addEdge(Sections sections) {
        for (Section section : sections) {
            setEdgeWeight(section);
        }
    }

    private void setEdgeWeight(Section section) {
        graph.addEdge(section.getUpStation(), section.getDownStation(), section);
    }

    public SubwayMapPath findPaths(Station source, Station target) {
        validateFindable(source, target);
        DijkstraShortestPath dijkstraShortestPath = new DijkstraShortestPath(graph);
        GraphPath path = dijkstraShortestPath.getPath(source, target);
        if (path == null) {
            throw new IllegalArgumentException("두 역이 서로 연결되어 있지 않습니다. 경로를 조회할 수 없습니다.");
        }
        return new SubwayMapPath(path);
    }

    private void validateFindable(Station source, Station target) {
        if (source.equals(target)) {
            throw new IllegalArgumentException("출발지와 도착지가 동일합니다. 입력값을 확인해주세요.");
        }
    }
}
