package nextstep.subway.path.dto;

import java.util.List;
import java.util.stream.Collectors;
import nextstep.subway.generic.domain.Distance;
import nextstep.subway.line.domain.Sections;
import nextstep.subway.generic.domain.Price;
import nextstep.subway.station.dto.StationResponse;

public class PathResponse {

    private List<StationResponse> stations;

    private int distance;

    private int price;


    public PathResponse(List<StationResponse> stations, Distance distance, Price price) {
        this.stations = stations;
        this.distance = distance.getValue();
        this.price = price.getValue();
    }

    public static PathResponse of(Sections sections, Price surcharge) {
        return new PathResponse(sections.getStations()
                .stream()
                .map(StationResponse::of)
                .collect(Collectors.toList()), sections.totalDistance(), Price.of(sections.totalDistance()).plus(surcharge));
    }

    public static PathResponse of(Sections sections, Price surcharge, double discountRate) {
        return new PathResponse(sections.getStations()
                .stream()
                .map(StationResponse::of)
                .collect(Collectors.toList()), sections.totalDistance(), Price.of(sections.totalDistance()).plus(surcharge).discount(discountRate));
    }

    public List<StationResponse> getStations() {
        return stations;
    }

    public int getDistance() {
        return distance;
    }

    public int getPrice() {
        return price;
    }
}
