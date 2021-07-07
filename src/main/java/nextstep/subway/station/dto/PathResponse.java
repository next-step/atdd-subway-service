package nextstep.subway.station.dto;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.station.domain.SubwayMapPath;

import java.util.List;

public class PathResponse {

    private List<StationResponse> stations;
    private int distance;
    private int fare;

    public static PathResponse of(SubwayMapPath subwayMapPath, LoginMember loginMember) {
        List<StationResponse> stations = StationResponse.asList(subwayMapPath.stations());
        return new PathResponse(stations, subwayMapPath.distance(), subwayMapPath.fare(loginMember));
    }

    private PathResponse(List<StationResponse> stations, int distance, int fare) {
        this.stations = stations;
        this.distance = distance;
        this.fare = fare;
    }

    public PathResponse() {
    }

    public List<StationResponse> getStations() {
        return stations;
    }

    public int getDistance() {
        return distance;
    }

    public int getFare() {
        return fare;
    }
}
