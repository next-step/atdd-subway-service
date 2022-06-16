package nextstep.subway.line.dto;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.dto.StationPathDTO;

public class PathResponse {

    private List<StationPathDTO> stations = new LinkedList<>();
    private double distance;
    private int price;

    public static PathResponse of(List<Station> stations, double distance, int price) {
        return new PathResponse(
            stations.stream()
                .map(StationPathDTO::of)
                .collect(Collectors.toList()),
            distance,
            price);
    }

    protected PathResponse(List<StationPathDTO> stations, double distance, int price) {
        this.stations = stations;
        this.distance = distance;
        this.price = price;
    }

    public List<StationPathDTO> getStations() {
        return stations;
    }

    public double getDistance() {
        return distance;
    }
}
