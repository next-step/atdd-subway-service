package nextstep.subway.path.domain;

import java.util.List;
import nextstep.subway.line.domain.Line;
import nextstep.subway.station.domain.Station;

public class Path {

    private final List<Station> stations;
    private final int distance;
    private final List<SectionEdge> sectionEdges;

    public Path(List<Station> stations, int distance, List<SectionEdge> sectionEdges) {
        this.stations = stations;
        this.distance = distance;
        this.sectionEdges = sectionEdges;
    }

    public List<Station> getStations() {
        return stations;
    }

    public int getDistance() {
        return distance;
    }

    public int additionalLineFare() {
        return this.sectionEdges.stream()
            .map(SectionEdge::getLine)
            .mapToInt(Line::getAdditionalFare)
            .max()
            .orElse(0);
    }

}
