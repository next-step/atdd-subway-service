package nextstep.subway.line.dto;

import static java.util.stream.Collectors.toList;

import java.util.List;
import nextstep.subway.line.domain.Distance;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.dto.StationResponse;

public class PathResponse {

    private List<StationResponse> stations;
    private double distance;
    private Long fare;

    private PathResponse(List<StationResponse> stations, double distance, Long fare) {
        this.stations = stations;
        this.distance = distance;
        this.fare = fare;
    }

    public static PathResponse ofList(List<Station> stations, Distance distance, Long fare) {
        List<StationResponse> collect = stations.stream()
                                                .map(StationResponse::of)
                                                .collect(toList());
        return new PathResponse(collect, distance.getDistance(), fare);
    }

    public List<StationResponse> getStations() {
        return stations;
    }

    public double getDistance() {
        return distance;
    }

    public Long getFare() {
        return fare;
    }
}
