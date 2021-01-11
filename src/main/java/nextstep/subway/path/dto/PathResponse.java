package nextstep.subway.path.dto;

import java.util.List;
import java.util.stream.Collectors;
import nextstep.subway.path.domain.Money;
import nextstep.subway.path.domain.Path;
import nextstep.subway.station.dto.StationResponse;

/**
 * @author : leesangbae
 * @project : subway
 * @since : 2021-01-06
 */
public class PathResponse {

    private final int distance;
    private final long price;

    private final List<StationResponse> stations;

    protected PathResponse(int distance, List<StationResponse> stations, long price) {
        this.distance = distance;
        this.stations = stations;
        this.price = price;
    }

    public static PathResponse of(Path path, Money money) {
        List<StationResponse> stationResponses = path.getPathStations().stream()
                .map(StationResponse::of)
                .collect(Collectors.toList());
        return new PathResponse(path.getDistance(), stationResponses, money.getAmountLongValue());
    }

    public int getDistance() {
        return distance;
    }

    public long getPrice() {
        return price;
    }

    public List<StationResponse> getStations() {
        return stations;
    }
}
