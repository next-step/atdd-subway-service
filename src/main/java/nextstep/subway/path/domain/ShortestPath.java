package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Distance;
import nextstep.subway.station.domain.Station;

import java.util.List;

public class ShortestPath {

    private List<Station> stations;
    private List<SectionEdge> sectionEdges;
    private Distance distance;

    public ShortestPath(List<Station> stations, List<SectionEdge> sectionEdges, int distance) {
        this.stations = stations;
        this.sectionEdges = sectionEdges;
        this.distance = new Distance(distance);
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
