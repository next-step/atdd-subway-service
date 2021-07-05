package nextstep.subway.path.domain;

import nextstep.subway.station.domain.Station;

import java.util.Collections;
import java.util.List;

public class SubwayPath {

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
}
