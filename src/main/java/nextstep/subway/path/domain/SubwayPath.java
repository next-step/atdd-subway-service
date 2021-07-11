package nextstep.subway.path.domain;

import nextstep.subway.station.domain.Station;

import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;

public class SubwayPath {
    private static final String NOT_FOUND_EXTRA_CHARGE = "지하철 경로의 추가 요금을 찾을 수 없습니다.";

    private final List<Station> stations;
    private final List<SectionEdge> sectionEdges;

    public SubwayPath(List<Station> stations, List<SectionEdge> sectionEdges) {
        this.stations = stations;
        this.sectionEdges = sectionEdges;
    }

    public List<Station> stations() {
        return Collections.unmodifiableList(stations);
    }

    public int distance() {
        return sectionEdges.stream().mapToInt(edge -> edge.section().distance()).sum();
    }

    public int extraCharge() {
        return sectionEdges.stream()
                .mapToInt(sectionEdge -> sectionEdge.line().extraCharge())
                .max()
                .orElseThrow(() -> new NoSuchElementException(NOT_FOUND_EXTRA_CHARGE));
    }
}
