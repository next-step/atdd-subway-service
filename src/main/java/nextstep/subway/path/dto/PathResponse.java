package nextstep.subway.path.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import nextstep.subway.path.domain.Path;
import nextstep.subway.station.domain.Station;

import java.util.List;

@Getter
@NoArgsConstructor
public class PathResponse {
    private List<Station> stationList;
    private double distance;
    private int fee;

    public static PathResponse of(Path path) {
        return PathResponse.builder()
                .stationList(path.getPaths())
                .distance(path.getDistance())
                .fee((int) path.getFee())
                .build();
    }

    @Builder
    private PathResponse(final List<Station> stationList, final double distance, final int fee) {
        this.stationList = stationList;
        this.distance = distance;
        this.fee = fee;
    }
}
