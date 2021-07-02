package nextstep.subway.path.dto;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.path.domain.Paths;
import nextstep.subway.station.domain.Station;

import java.util.List;

public class PathResponse {
    private int distance;
    private int totalFare;
    private List<Station> stations;

    public PathResponse() {
        // empty
    }

    public PathResponse(final Paths paths, final LoginMember loginMember) {
        this.distance = paths.getTotalDistance();
        this.stations = paths.getShortestStationRoutes();
        this.totalFare = paths.calculateFare(loginMember);
    }

    public int getDistance() {
        return distance;
    }

    public int getTotalFare() {
        return totalFare;
    }

    public List<Station> getStations() {
        return stations;
    }
}
