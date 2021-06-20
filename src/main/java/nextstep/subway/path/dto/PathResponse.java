package nextstep.subway.path.dto;

import nextstep.subway.station.domain.Station;

import java.util.List;

public class PathResponse {

    List<Station> list;
    int distance;

    public PathResponse(List<Station> list, int distance) {
        this.list = list;
        this.distance = distance;
    }

    public List<Station> getList() {
        return list;
    }

    public int getDistance() {
        return distance;
    }
}
