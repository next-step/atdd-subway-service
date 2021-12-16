package nextstep.subway.path.domain;

import com.google.common.collect.Lists;
import nextstep.subway.line.domain.Distance;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Lines;
import nextstep.subway.line.domain.Section;
import nextstep.subway.station.domain.Station;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

import java.util.ArrayList;
import java.util.List;

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

        DijkstraShortestPath dijkstraShortestPath = new DijkstraShortestPath(graph);
        Double weight = dijkstraShortestPath.getPath(source, target).getWeight();

        this.distance = new Distance(weight.intValue());
        this.stationIds = dijkstraShortestPath.getPath(source, target).getVertexList();
    }

    public Distance getDistance() {
        return distance;
    }

    public List<Long> getStationIds() {
        return stationIds;
    }
}