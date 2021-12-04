package nextstep.subway.path.ui.dto;

import nextstep.subway.station.dto.StationResponse;

import java.util.ArrayList;
import java.util.List;

/**
 * packageName : nextstep.subway.path.ui.dto
 * fileName : PathResponse
 * author : haedoang
 * date : 2021/12/04
 * description :
 */
public class PathResponse {
    private List<StationResponse> stations;

    private PathResponse() {
    }

    private PathResponse(List<StationResponse> stations) {
        this.stations = new ArrayList<>(stations);
    }

    public static PathResponse of(List<StationResponse> stations) {
        return new PathResponse(stations);
    }

    public List<StationResponse> getStations() {
        return stations;
    }
}
