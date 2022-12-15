package nextstep.subway.line.dto;

import nextstep.subway.path.domain.ShortestPath;
import nextstep.subway.path.dto.StationPathResponse;
import nextstep.subway.station.domain.Station;

import java.util.List;
import java.util.stream.Collectors;

public class PathResponse {

    private List<StationPathResponse> stations;
    private int distance;
    private int fare;

    protected PathResponse() {

    }

    private PathResponse(List<StationPathResponse> stations) {
        this.stations = stations;
    }

    private PathResponse(List<StationPathResponse> stations, int distance, int fare) {
        this.stations = stations;
        this.distance = distance;
        this.fare = fare;
    }
    
    public static PathResponse from(ShortestPath shortestGraph, int fare) {
        List<StationPathResponse> responses = getStationPathResponses(shortestGraph.getPath());
        return new PathResponse(responses, shortestGraph.getPathDistance(), fare);
    }

    private static List<StationPathResponse> getStationPathResponses(List<Station> stationsInPath) {
        List<StationPathResponse> collect = stationsInPath.stream()
            .map(StationPathResponse::from)
            .collect(Collectors.toList());
        return collect;
    }


    public List<StationPathResponse> getStations() {
        return this.stations;
    }
}
