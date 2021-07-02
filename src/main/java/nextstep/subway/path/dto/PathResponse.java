package nextstep.subway.path.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import nextstep.subway.station.domain.Station;

import java.util.List;

@Getter
@NoArgsConstructor
public class PathResponse {
    private List<Station> stationList;
    private double distance;

    @Builder
    private PathResponse(final List<Station> stationList, final double distance) {
        this.stationList = stationList;
        this.distance = distance;
    }
}
