package nextstep.subway.path.dto;

import nextstep.subway.path.domain.Path;
import nextstep.subway.station.dto.StationResponse;

import java.util.ArrayList;
import java.util.List;

public class PathResponse {


    public static PathResponse of(Path path) {
        return new PathResponse();
    }

    public List<StationResponse> getPathStations() {

        return new ArrayList<>();
    }
}
