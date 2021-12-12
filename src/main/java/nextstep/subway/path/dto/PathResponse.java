package nextstep.subway.path.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import nextstep.subway.line.domain.Distance;
import nextstep.subway.line.domain.Money;
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

    private PathResponse(List<StationResponse> stations, Distance distance, Money fare) {
        this.stations = new ArrayList<>(stations);
        this.distance = distance.intValue();
        this.fare = fare.intValue();
    }

    public static PathResponse of(Path path, Money money) {
        return new PathResponse(StationResponse.ofList(path.stations()), path.distance(), money);
    }
}
