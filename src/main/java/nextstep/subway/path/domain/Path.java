package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Distance;
import nextstep.subway.station.domain.Station;

import java.util.List;

public class Path {

    private final List<Station> stations;
    private final List<SectionEdge> sectionEdges;
    private final Distance distance;

    public Path(List<Station> stations, List<SectionEdge> sectionEdges, Distance distance) {
        this.stations = stations;
        this.sectionEdges = sectionEdges;
        this.distance = distance;
    }

    public static Path of(List<Station> stations, List<SectionEdge> sectionEdges, int distance) {
        return new Path(stations, sectionEdges, new Distance(distance));
    }

    public List<Station> getStations() {
        return stations;
    }

    public Distance getDistance() {
        return distance;
    }

    public List<SectionEdge> getSectionEdges() {
        return this.sectionEdges;
    }

    public boolean hasStations() {
        return !this.stations.isEmpty();
    }

    @Override
    public String toString() {
        return "Path{" +
                "stations=" + stations +
                ", sectionEdges=" + sectionEdges +
                ", distance=" + distance +
                '}';
    }
}
