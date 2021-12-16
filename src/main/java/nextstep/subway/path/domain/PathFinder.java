package nextstep.subway.path.domain;

import com.google.common.collect.Lists;
import nextstep.subway.line.domain.Distance;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Lines;
import nextstep.subway.line.domain.Section;
import nextstep.subway.station.domain.Station;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class PathFinder {
    private final Lines lines;
    private Distance distance;
    private List<Long> stationIds = new ArrayList<>();

    public PathFinder(Line line) {
        this.lines = new Lines(Lists.newArrayList(line));
    }

    public PathFinder(List<Line> lines) {
        this.lines = new Lines(lines);
    }

    public void findPath(Long source, Long target) {
        WeightedMultigraph<Long, DefaultWeightedEdge> graph = new WeightedMultigraph(DefaultWeightedEdge.class);
        List<Station> stations = lines.getStations();
        for (Station station : stations) {
            graph.addVertex(station.getId());
        }

        List<Section> sections = lines.getSections();
        for (Section section : sections) {
            graph.setEdgeWeight(graph.addEdge(section.getUpStation().getId(), section.getDownStation().getId()), section.getDistance());
        }
        findShortestPath(source, target, graph);
    }

    private void findShortestPath(Long source, Long target, WeightedMultigraph<Long, DefaultWeightedEdge> graph) {
        DijkstraShortestPath dijkstraShortestPath = new DijkstraShortestPath(graph);
        GraphPath<Long, DefaultWeightedEdge> graphPath;
        try {
            graphPath = dijkstraShortestPath.getPath(source, target);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("출발역 또는 도착역이 노선에 존재하지 않습니다.");
        }

        if (Objects.isNull(graphPath)) {
            throw new IllegalArgumentException("경로를 찾을 수 없습니다.");
        }

        Double weight = graphPath.getWeight();

        this.distance = new Distance(weight.intValue());
        this.stationIds = graphPath.getVertexList();
    }

    public Distance getDistance() {
        return distance;
    }

    public List<Long> getStationIds() {
        return stationIds;
    }
}