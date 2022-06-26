package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import nextstep.subway.line.domain.Sections;
import nextstep.subway.station.domain.Station;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

import java.util.List;
import java.util.Objects;

public class PathFinder {

    private WeightedMultigraph<Station, DefaultWeightedEdge> graph;
    private DijkstraShortestPath<Station, DefaultWeightedEdge> dijkstraShortestPath;

    public PathFinder(List<Line> lines) {
        initGraph(lines);
        dijkstraShortestPath = new DijkstraShortestPath<>(graph);
    }

    private void initGraph(List<Line> lines) {
        graph = new WeightedMultigraph<>(DefaultWeightedEdge.class);

        for (Line line : lines) {
            addStation(line.getStations());
            addSection(line.getSections());
        }
    }

    private void addSection(Sections sections) {
        sections.getValues()
                .forEach(this::setEdgeWeight);
    }

    private void setEdgeWeight(Section section) {
        graph.setEdgeWeight(graph.addEdge(section.getUpStation(), section.getDownStation()), section.getDistance());
    }

    private void addStation(List<Station> stations) {
        stations.forEach(station -> graph.addVertex(station));
    }

    public ShortestPath findShortestPath(Station sourceStation, Station targetStation) {
        validate(sourceStation, targetStation);


        return getShortestPath(sourceStation, targetStation);
    }

    private ShortestPath getShortestPath(Station sourceStation, Station targetStation) {
        GraphPath<Station, DefaultWeightedEdge> path = dijkstraShortestPath.getPath(sourceStation, targetStation);

        if (Objects.isNull(path)) {
            throw new IllegalStateException("경로가 존재하지 않습니다.");
        }

        return new ShortestPath(path.getVertexList(), (int) path.getWeight());
    }

    private void validate(Station sourceStation, Station targetStation) {
        validateNull(sourceStation, targetStation);
        validateEqualsStation(sourceStation, targetStation);
        validateContainStation(sourceStation, targetStation);
    }

    private void validateNull(Station sourceStation, Station targetStation) {
        if (Objects.isNull(sourceStation)) {
            throw new IllegalArgumentException("출발역을 지정해주세요.");
        }

        if (Objects.isNull(targetStation)) {
            throw new IllegalArgumentException("도착역을 지정해주세요.");
        }
    }

    private void validateEqualsStation(Station sourceStation, Station targetStation) {
        if (Objects.equals(sourceStation, targetStation)) {
            throw new IllegalArgumentException("출발역과 도착역이 동일합니다.");
        }
    }

    private void validateContainStation(Station sourceStation, Station targetStation) {
        if (!graph.containsVertex(sourceStation) || !graph.containsVertex(targetStation)) {
            throw new IllegalArgumentException("존재하지 않는 지하철역입니다.");
        }
    }

    private void validateExistSection(Station sourceStation, Station targetStation) {
        if (!graph.containsEdge(sourceStation, targetStation)) {
            throw new IllegalStateException("경로가 존재하지 않습니다.");
        }
    }
}
