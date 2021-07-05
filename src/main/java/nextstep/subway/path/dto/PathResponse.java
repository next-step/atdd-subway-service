package nextstep.subway.path.dto;

import nextstep.subway.path.util.Path;
import nextstep.subway.path.util.SectionEdge;
import nextstep.subway.station.dto.StationResponse;

import java.util.Collections;
import java.util.List;

public class PathResponse {
    private List<StationResponse> stations;
    private List<SectionEdge> sectionEdges;
    private int distance;
    private int fare;

    public PathResponse() {
    }

    public PathResponse(Path path, int totalFare) {
        this.sectionEdges = path.getSectionEdges();
        this.stations = path.getStations();
        this.distance = path.getDistance();
        this.fare = totalFare;
    }

    public PathResponse(List<StationResponse> stations, int distance) {
        this.stations = stations;
        this.distance = distance;
    }

    public int getDistance() {
        return distance;
    }

    public int getFare() {
        return fare;
    }

    public List<StationResponse> getStations() {
        return Collections.unmodifiableList(stations);
    }
}
