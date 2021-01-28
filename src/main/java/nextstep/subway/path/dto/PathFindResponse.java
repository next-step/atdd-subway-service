package nextstep.subway.path.dto;

import java.util.List;

public class PathFindResponse {

    private List<Long> stationIds;

    public PathFindResponse(List<Long> stationIds) {
        this.stationIds = stationIds;
    }

    public List<Long> getStationIds() {
        return stationIds;
    }

}
