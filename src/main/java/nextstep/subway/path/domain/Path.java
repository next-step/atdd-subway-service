package nextstep.subway.path.domain;

import nextstep.subway.station.domain.Station;

import java.util.List;

public class Path {

    private List<Station> stations;

    private int distance;

    private List<SectionEdge> sectionEdges;

    public Path(List<Station> stations, int distance, List<SectionEdge> sectionEdges) {
        this.stations = stations;
        this.distance = distance;
        this.sectionEdges = sectionEdges;
    }

    public List<Station> getStations() {
        return stations;
    }

    public void setStations(List<Station> stations) {
        this.stations = stations;
    }

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public List<SectionEdge> getSectionEdges() {
        return sectionEdges;
    }

    public void setSectionEdges(List<SectionEdge> sectionEdges) {
        this.sectionEdges = sectionEdges;
    }
}
