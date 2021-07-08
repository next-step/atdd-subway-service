package nextstep.subway.path.dto;

import nextstep.subway.enums.SubwayFarePolicy;
import nextstep.subway.path.domain.Fare;
import nextstep.subway.path.domain.SubwayPath;
import nextstep.subway.station.dto.StationResponse;

import java.util.List;
import java.util.stream.Collectors;

public class PathResponse {

    private List<StationResponse> stations;
    private int distance;
    private int subwayFare;

    public PathResponse() {
    }

    public PathResponse(List<StationResponse> stations, int distance, int subwayFare) {
        this.stations = stations;
        this.distance = distance;
        this.subwayFare = subwayFare;
    }

    public static PathResponse of(SubwayPath subwayPath, SubwayFarePolicy farePolicy) {
        List<StationResponse> stationResponses = subwayPath
                .getStations()
                .stream()
                .map(StationResponse::of)
                .collect(Collectors.toList());
        int totalDistance = subwayPath.calcTotalDistance();
        Fare fare = subwayPath.calcMaxExtraFare();
        int subwayFare = fare.calcSubwayFare(totalDistance, farePolicy);
        return new PathResponse(stationResponses, totalDistance, subwayFare);
    }

    public List<StationResponse> getStations() {
        return stations;
    }

    public int getDistance() {
        return distance;
    }

    public int getSubwayFare() {
        return subwayFare;
    }
}
