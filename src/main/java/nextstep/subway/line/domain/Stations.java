package nextstep.subway.line.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import nextstep.subway.station.domain.Station;
import nextstep.subway.station.dto.StationResponse;

public class Stations {
    private final List<Station> stations = new ArrayList<>();

    public void add(Station station) {
        stations.add(station);
    }

    public boolean isEmpty() {
        return stations.isEmpty();
    }

    public List<StationResponse> toResponse() {
        return stations.stream()
                .map(Station::toResponse)
                .collect(Collectors.toList());
    }

    public int size() {
        return stations.size();
    }

    public boolean contain(Station station) {
        return stations.contains(station);
    }
}
