package nextstep.subway.station.domain;

import nextstep.subway.station.dto.StationResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Stations {

    private final List<Station> stations = new ArrayList<>();

    public List<Station> getStations() {
        return stations;
    }

    public List<StationResponse> toResponse() {
        return stations.stream()
                .map(StationResponse::of)
                .collect(Collectors.toList());
    }

    public void add(Station station) {
        if (notContains(station)) {
            stations.add(station);
        }
    }

    public boolean contains(Station station) {
        return stations.contains(station);
    }

    public boolean notContains(Station station) {
        return !contains(station);
    }
}
