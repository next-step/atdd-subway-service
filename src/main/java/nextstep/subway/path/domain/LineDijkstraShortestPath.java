package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Distance;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Lines;
import nextstep.subway.station.domain.Station;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

import java.util.List;

public class LineDijkstraShortestPath {
    private WeightedMultigraph<Station, DefaultWeightedEdge> graph =
            new WeightedMultigraph<>(DefaultWeightedEdge.class);

    public LineDijkstraShortestPath(Lines lines) {
        validationLines(lines);

        lines.forEach(this::registerLine);
    }

    public List<Station> getShortestStationPath(Station source, Station target) {
        return findShortestPath(source, target).getVertexList();
    }

    public int getShortestPathStationDistance(Station source, Station target) {
        return (int) findShortestPath(source, target).getWeight();
    }

    private void validationLines(Lines lines) {
        if (lines.isEmpty()) {
            throw new IllegalArgumentException("경로 조회 시 최소 1개 이상의 노선이 포함되어야 합니다.");
        }
    }

    private void validationFindPathStation(Station source, Station target) {
        if (source.equals(target)) {
            throw new IllegalArgumentException("출발지와 도착지가 같을 수 없습니다.");
        }
        if (isNotHasStationVertex(source)) {
            throw new IllegalArgumentException("출발역 정보가 노선에 등록되어 있지 않습니다.");
        }
        if (isNotHasStationVertex(target)) {
            throw new IllegalArgumentException("도착역 정보가 노선에 등록되어 있지 않습니다.");
        }
    }

    private void registerLine(Line line) {
        line.getStations().forEach(this::addStationVertex);

        line.getSections().forEach(section -> {
            addEdgeWithWeight(section.getUpStation(), section.getDownStation(), section.getDistance());
        });
    }

    private void addStationVertex(Station station) {
        if (isNotHasStationVertex(station)) {
            this.graph.addVertex(station);
        }
    }

    private boolean isNotHasStationVertex(Station station) {
        return !this.graph.containsVertex(station);
    }

    private void addEdgeWithWeight(Station upStation, Station downStation, Distance weightDistance) {
        if (isNotHasEdge(upStation, downStation)) {
            DefaultWeightedEdge edge = this.graph.addEdge(upStation, downStation);
            this.graph.setEdgeWeight(edge, weightDistance.getValue());
        }
    }

    private boolean isNotHasEdge(Station upStation, Station downStation) {
        return !this.graph.containsEdge(upStation, downStation);
    }

    private GraphPath<Station, DefaultWeightedEdge> findShortestPath(Station source, Station target) {
        validationFindPathStation(source, target);

        DijkstraShortestPath<Station, DefaultWeightedEdge> dijkstraShortestPath =
                new DijkstraShortestPath<>(graph);

        GraphPath<Station, DefaultWeightedEdge> result = dijkstraShortestPath.getPath(source, target);

        if (result == null) {
            throw new IllegalArgumentException("경로를 찾을 수 없습니다.");
        }

        return result;
    }
}
