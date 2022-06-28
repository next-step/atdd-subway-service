package nextstep.subway.path.dto;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.fare.domain.Fare;
import nextstep.subway.path.domain.StationPath;
import nextstep.subway.station.domain.Station;

import java.util.List;
import java.util.Objects;

public class PathResponse {
    private final List<Station> stations;
    private final int distance;
    private final int fare;

    public PathResponse(final List<Station> stations, final int distance, final Fare fare) {
        this.stations = stations;
        this.distance = distance;
        this.fare = fare.getValue();
    }

    public static PathResponse of(final StationPath stationPath, final LoginMember loginMember) {
        return new PathResponse(stationPath.getStations(), stationPath.getDistance(),
                Fare.of(loginMember, stationPath.getExtraCharge(), stationPath.getDistance()));
    }

    public List<Station> getStations() {
        return stations;
    }

    public int getDistance() {
        return distance;
    }

    public int getFare() {
        return fare;
    }

    @Override
    public String toString() {
        return "PathResponse{" +
                "stations=" + stations +
                ", distance=" + distance +
                ", fare=" + fare +
                '}';
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final PathResponse that = (PathResponse) o;
        return distance == that.distance && fare == that.fare && Objects.equals(stations, that.stations);
    }

    @Override
    public int hashCode() {
        return Objects.hash(stations, distance, fare);
    }
}
