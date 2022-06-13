package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Lines;
import nextstep.subway.line.domain.Section;
import nextstep.subway.station.domain.Station;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

import java.util.Objects;

public class PathFinder {
    private static final String MUST_NOT_BE_NULL = "null일 수 없습니다.";

    private WeightedMultigraph<Station, DefaultWeightedEdge> graph;

    public PathFinder() {
        this.graph = new WeightedMultigraph<>(DefaultWeightedEdge.class);
    }

    public ShortestPath findShortestPath(Lines lines, Station upStation, Station downStation) {
        validate(lines, upStation, downStation);
        addVertex(lines);
        assignEdgeWeight(lines);

        return new ShortestPath(graph, upStation, downStation);
    }

    private void validate(Lines lines, Station upStation, Station downStation) {
        Objects.requireNonNull(lines, MUST_NOT_BE_NULL) ;
        Objects.requireNonNull(upStation, MUST_NOT_BE_NULL);
        Objects.requireNonNull(downStation, MUST_NOT_BE_NULL);

        validateVertex(upStation, downStation);
        validateStationIsIncluded(lines, upStation, downStation);
    }

    private void validateVertex(Station upStation, Station downStation) {
        if (upStation.match(downStation)) {
            throw new IllegalArgumentException("출발역과 도착역이 같을 수 없습니다.");
        }
    }

    private void validateStationIsIncluded(Lines lines, Station upStation, Station downStation) {
        if (lines.notContainsAll(upStation, downStation)) {
            throw new IllegalArgumentException("노선에 지하철역이 모두 포함되어 있지 않습니다.");
        }
    }

    private void addVertex(Lines lines) {
        for (Station station : lines.getAllStations()) {
            graph.addVertex(station);
        }
    }

    private void assignEdgeWeight(Lines lines) {
        for (Section section : lines.getAllSections()) {
            graph.setEdgeWeight(graph.addEdge(section.getUpStation(), section.getDownStation()), section.getDistance());
        }
    }
}
