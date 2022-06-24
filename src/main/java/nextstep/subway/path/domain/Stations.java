package nextstep.subway.path.domain;

import java.util.List;
import java.util.stream.Collectors;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.dto.StationResponse;

public class Stations {
    public static final String ERROR_MESSAGE_STATIONS_NULL = "역 목록이 null 입니다.";
    private final List<Station> stations;

    public Stations(List<Station> stations) {
        validateStations(stations);
        this.stations = stations;
    }

    private void validateStations(List<Station> stations) {
        if(stations == null) {
            throw new NullPointerException(ERROR_MESSAGE_STATIONS_NULL);
        }
    }

    public List<StationResponse> toStationResponses() {
        return stations.stream().map(StationResponse::of).collect(Collectors.toList());
    }
}
