package nextstep.subway.path.domain;

import nextstep.subway.station.domain.Station;

import java.util.Collections;
import java.util.List;

public class ShortestPath {
    List<SectionEdge> sectionEdges;
    List<Station> stations;
    int distance;

    private ShortestPath(List<SectionEdge> sectionEdges, List<Station> stations, int distance) {
        this.sectionEdges = sectionEdges;
        this.stations = stations;
        this.distance = distance;
    }

    public static ShortestPath of(List<SectionEdge> sectionEdges, List<Station> stations, int distance) {
        return new ShortestPath(sectionEdges, stations, distance);
    }

    public int getAdditionalPrice() {
        return sectionEdges.stream()
                .map(SectionEdge::getAdditionalPrice)
                .max(Integer::compareTo)
                .orElse(0);
    }

    public List<Station> getStations() {
        return Collections.unmodifiableList(stations);
    }

    public int getDistance() {
        return distance;
    }

    public int getPathStationCount() {
        return stations.size();
    }
}
