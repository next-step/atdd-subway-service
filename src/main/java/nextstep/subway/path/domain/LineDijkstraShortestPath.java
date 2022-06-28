package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Distance;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Lines;
import nextstep.subway.line.domain.Section;
import nextstep.subway.member.domain.AgeGroup;
import nextstep.subway.path.dto.ShortestPathResponse;
import nextstep.subway.station.domain.Station;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.WeightedMultigraph;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class LineDijkstraShortestPath {
    private WeightedMultigraph<Station, SectionEdge> graph = new WeightedMultigraph<>(SectionEdge.class);

    public LineDijkstraShortestPath(Lines lines) {
        validationLines(lines);

        lines.forEach(this::registerLine);
    }

    public ShortestPathResponse getShortestPathResponse(Station source, Station target, AgeGroup ageGroup) {
        GraphPath<Station, SectionEdge> result = findShortestPath(source, target);
        Lines shortestPathLines = new Lines(getLinesInSectionEdge(result.getEdgeList()));
        Distance totalDistance = new Distance((int) result.getWeight());

        return new ShortestPathResponse(
                result.getVertexList(),
                totalDistance.getValue(),
                shortestPathLines.getMaxAdditionalFare().calculateTotalFare(totalDistance, ageGroup)
        );
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
        line.getSections().forEach(this::addEdge);
    }

    private void addStationVertex(Station station) {
        if (isNotHasStationVertex(station)) {
            this.graph.addVertex(station);
        }
    }

    private boolean isNotHasStationVertex(Station station) {
        return !this.graph.containsVertex(station);
    }

    private void addEdge(Section section) {
        SectionEdge sectionEdge = new SectionEdge(section);

        this.graph.addEdge(section.getUpStation(), section.getDownStation(), sectionEdge);
        this.graph.setEdgeWeight(sectionEdge, section.getDistance().getValue());
    }

    private GraphPath<Station, SectionEdge> findShortestPath(Station source, Station target) {
        validationFindPathStation(source, target);

        DijkstraShortestPath<Station, SectionEdge> dijkstraShortestPath = new DijkstraShortestPath<>(graph);

        GraphPath<Station, SectionEdge> result = dijkstraShortestPath.getPath(source, target);

        if (result == null) {
            throw new IllegalArgumentException("경로를 찾을 수 없습니다.");
        }

        return result;
    }

    private Set<Line> getLinesInSectionEdge(List<SectionEdge> target) {
        return target.stream()
                .map(SectionEdge::getLine)
                .collect(Collectors.toSet());
    }
}
