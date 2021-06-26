package nextstep.subway.path.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import nextstep.subway.station.domain.Station;

import java.util.List;

@Getter
@NoArgsConstructor
public class PathResponse {
    private List<Station> stationList;
    private int distance;

    public PathResponse(final List<Station> stationList, final int distance) {
        this.stationList = stationList;
        this.distance = distance;
    }
}
