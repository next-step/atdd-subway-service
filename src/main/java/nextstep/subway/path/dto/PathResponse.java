package nextstep.subway.path.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import nextstep.subway.auth.domain.Stranger;
import nextstep.subway.auth.domain.User;
import nextstep.subway.line.domain.Distance;
import nextstep.subway.line.domain.ExtraCharge;
import nextstep.subway.line.domain.SubwayFare;
import nextstep.subway.line.domain.SubwayUser;
import nextstep.subway.path.domain.Path;
import nextstep.subway.station.dto.StationResponse;

import java.util.ArrayList;
import java.util.List;

/**
 * packageName : nextstep.subway.path.dto
 * fileName : PathResponse
 * author : haedoang
 * date : 2021/12/04
 * description :
 */
@NoArgsConstructor
@Getter
public class PathResponse {
    private List<StationResponse> stations;
    private int distance;
    private int fare;

    private PathResponse(List<StationResponse> stations, Distance distance) {
        this.stations = new ArrayList<>(stations);
        this.distance = distance.intValue();
        this.fare = SubwayFare.rateInquiry(distance);
    }

    private PathResponse(List<StationResponse> stations, Distance distance, User user, ExtraCharge extraCharge) {
        this.stations = new ArrayList<>(stations);
        this.distance = distance.intValue();
        this.fare = calculateFare(distance, user, extraCharge);
    }

    private int calculateFare(Distance distance, User user, ExtraCharge extraCharge) {
        if(user instanceof Stranger) {
            return SubwayFare.rateInquiry(distance, extraCharge);
        }
        return SubwayFare.rateInquiry(distance, SubwayUser.of(user.getAge()), extraCharge);
    }

    public static PathResponse of(Path path) {
        return new PathResponse(StationResponse.ofList(path.stations()), path.distance());
    }

    public static PathResponse of(Path path, User user) {
        return new PathResponse(StationResponse.ofList(path.stations()), path.distance(), user, path.extraCharge());
    }
}
