package nextstep.subway.path.dto;

import static java.util.stream.Collectors.toList;

import java.util.List;
import java.util.Objects;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.path.domain.PathFindResult;
import nextstep.subway.path.domain.SubwayFare;
import nextstep.subway.station.dto.StationResponse;

public class PathResponse {
    private List<StationResponse> stations;

    private List<LineResponse> lines;

    private int distance;

    private SubwayFare fare;

    protected PathResponse() {

    }

    public PathResponse(List<StationResponse> stations, List<LineResponse> lines, int distance, SubwayFare fare) {
        this.stations = stations;
        this.lines = lines;
        this.distance = distance;
        this.fare = fare;
    }

    public static PathResponse of(PathFindResult findResult) {
        List<StationResponse> stationResponses = StationResponse.of(findResult.getStations());
        List<LineResponse> lineResponses = findResult.getLines()
                .stream()
                .map(LineResponse::of)
                .collect(toList());
        return new PathResponse(stationResponses, lineResponses, findResult.getDistance(), findResult.getFare());
    }

    public List<StationResponse> getStations() {
        return stations;
    }

    public List<LineResponse> getLines() {
        return lines;
    }

    public int getDistance() {
        return distance;
    }

    public SubwayFare getFare() {
        return fare;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        PathResponse that = (PathResponse) o;
        return distance == that.distance && Objects.equals(stations, that.stations);
    }

    @Override
    public int hashCode() {
        return Objects.hash(stations, distance);
    }


}
