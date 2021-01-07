package nextstep.subway.line.domain;

import nextstep.subway.station.domain.Station;
import nextstep.subway.station.dto.StationResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Stations {
    private List<Station> stations;

    public Stations(List<Station> stations) {
        if ( stations == null ) {
            this.stations = new ArrayList<>();
        }
        this.stations = stations;
    }

    public List<StationResponse> toResponses() {
        return stations.stream()
                .map(StationResponse::of)
                .collect(Collectors.toList());
    }

    public boolean isEmpty() {
        return stations.isEmpty();
    }

    public boolean contains(Station station) {
        return stations.contains(station);
    }

}
