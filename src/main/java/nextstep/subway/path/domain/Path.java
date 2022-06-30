package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Distance;
import nextstep.subway.station.domain.Station;

import java.util.List;

public class Path {
    private List<Station> stations;
    private Distance distance;
    private List<SectionEdge> sectionEdges;

    protected Path(List<Station> stations, int distance, List<SectionEdge> sectionEdges) {
        this.stations = stations;
        this.distance = Distance.of(distance);
        this.sectionEdges = sectionEdges;
    }

    public static Path of(List<Station> stations, int distance, List<SectionEdge> sectionEdges) {
        return new Path(stations, distance, sectionEdges);
    }

    public List<Station> getStations() {
        return stations;
    }

    public Distance getDistance() {
        return distance;
    }

    public List<SectionEdge> getSectionEdges() {
        return sectionEdges;
    }
}
