package nextstep.subway.path.dto;

import lombok.NoArgsConstructor;
import nextstep.subway.line.domain.Distance;
import nextstep.subway.line.domain.SubwayFare;
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

    public static PathResponse of(List<Station> stations, Distance distance) {
        List<StationResponse> stationResponses =
                stations.stream()
                        .map(StationResponse::of)
                        .collect(Collectors.toList());

        return new PathResponse(stationResponses, distance);
    }

    public static PathResponse of(Path path) {
        return PathResponse.of(path.routes(), path.distance());
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
