package nextstep.subway.path.dto;

import lombok.NoArgsConstructor;
import nextstep.subway.auth.domain.Stranger;
import nextstep.subway.auth.domain.User;
import nextstep.subway.line.domain.Distance;
import nextstep.subway.line.domain.SubwayFare;
import nextstep.subway.line.domain.SubwayUser;
import nextstep.subway.path.domain.Path;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.dto.StationResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * packageName : nextstep.subway.path.dto
 * fileName : PathResponse
 * author : haedoang
 * date : 2021/12/04
 * description :
 */
@NoArgsConstructor
public class PathResponse {
    private List<StationResponse> stations;
    private int distance;
    private int fare;

    private PathResponse(List<StationResponse> stations, Distance distance) {
        this.stations = new ArrayList<>(stations);
        this.distance = distance.intValue();
        this.fare = SubwayFare.rateInquiry(distance);
    }

    private PathResponse(List<StationResponse> stations, Distance distance, User user) {
        this.stations = new ArrayList<>(stations);
        this.distance = distance.intValue();
        this.fare = calculateFare(distance, user);
    }

    private int calculateFare(Distance distance, User user) {
        int fare = SubwayFare.rateInquiry(distance);
        if (user instanceof Stranger) {
            return fare;
        }
        return SubwayFare.discountFare(fare, SubwayUser.of(user.getAge()));
    }

    public static PathResponse of(List<Station> stations, Distance distance) {
        return new PathResponse(StationResponse.ofList(stations), distance);
    }

    public static PathResponse of(Path path) {
        return PathResponse.of(path.routes(), path.distance());
    }

    public static PathResponse of(Path path, User user) {
        return new PathResponse(StationResponse.ofList(path.routes()), path.distance(), user);
    }

    public List<StationResponse> getStations() {
        return stations;
    }

    public int getFare() {
        return fare;
    }

    public int getDistance() {
        return distance;
    }
}
