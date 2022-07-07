package nextstep.subway.path.dto;

import java.util.List;
import java.util.stream.Collectors;
import nextstep.subway.line.domain.Fare;
import nextstep.subway.path.domain.Path;

public class PathResponse {
    private List<StationDto> stations;
    private int distance;
    private int fare;

    private PathResponse() {
    }

    private PathResponse(List<StationDto> stations, int distance, int fare) {
        this.stations = stations;
        this.distance = distance;
        this.fare = fare;
    }

    public static PathResponse of(Path path, Fare fare) {
        List<StationDto> stationDtos = path.getStations().stream()
                .map(StationDto::from)
                .collect(Collectors.toList());

        return new PathResponse(stationDtos, path.getDistance().value(), fare.value());
    }

    public List<StationDto> getStations() {
        return stations;
    }

    public int getDistance() {
        return distance;
    }

    public int getFare() {
        return fare;
    }
}
