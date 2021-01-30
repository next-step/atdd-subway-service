package nextstep.subway.line.dto;

import java.util.List;
import java.util.stream.Collectors;
import nextstep.subway.line.domain.Stations;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.dto.StationResponse;

public class StationResponses {

    private List<StationResponse> stationResponses;

    public StationResponses(List<StationResponse> stationResponses) {
        this.stationResponses = stationResponses;
    }

    public static StationResponses of(Stations stations) {
        List<Station> stations1 = stations.getStations();
        return new StationResponses(stations1.stream().map(StationResponse::of).collect(Collectors.toList()));
    }

    public List<StationResponse> getStationResponses() {
        return stationResponses;
    }
}
