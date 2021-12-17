package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Fare;
import nextstep.subway.station.domain.Station;

import java.util.List;

public class Path {
    private static final int 요금_최소값 = 0;
    private final List<Station> stations;
    private final int distance;
    private final List<SectionEdge> sectionEdges;

    private Path(List<Station> stations, int distance, List<SectionEdge> sectionEdges) {
        this.stations = stations;
        this.distance = distance;
        this.sectionEdges = sectionEdges;
    }

    public static Path of(List<Station> stations, double distance, List<SectionEdge> sectionEdges) {
        return new Path(stations, (int) distance, sectionEdges);
    }

    public List<Station> getStations() {
        return stations;
    }

    public int getDistance() {
        return distance;
    }

    public List<SectionEdge> getSectionEdge() {
        return sectionEdges;
    }

    public Fare getExtraFare() {
        return sectionEdges.stream()
                .map(section -> section.getLine().getExtraFare())
                .max(Fare::compareTo)
                .orElse(Fare.of(요금_최소값));
    }
}
