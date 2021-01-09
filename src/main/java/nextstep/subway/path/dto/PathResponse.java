package nextstep.subway.path.dto;

import lombok.Getter;
import nextstep.subway.station.dto.StationResponse;

import java.util.List;

@Getter
public class PathResponse {

    private List<StationResponse> stations;
    private int distance;
}
