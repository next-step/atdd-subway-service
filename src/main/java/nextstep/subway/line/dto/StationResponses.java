package nextstep.subway.line.dto;

import nextstep.subway.line.domain.SortedStations;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.Stations;
import nextstep.subway.station.dto.StationResponse;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class StationResponses {
    private final List<StationResponse> stationResponses;


    public StationResponses(SortedStations sortedStation) {
        this(sortedStation.toCollection());
    }

    public StationResponses(Stations stations) {
        this(stations.toCollection());
    }

    public StationResponses(List<Station> stations) {
        this.stationResponses = convertToDTO(stations);
    }

    public List<StationResponse> toCollection() {
        return Collections.unmodifiableList(stationResponses);
    }

    private List<StationResponse> convertToDTO(List<Station> stations) {
        return stations.stream()
                .map(StationResponse::of)
                .collect(Collectors.toList());
    }
}
