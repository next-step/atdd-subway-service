package nextstep.subway.path.dto;

import nextstep.subway.station.domain.Station;

import java.util.List;

public class PathResponse {
    private List<Station> stations;
    private int distance;

    public PathResponse() {
    }
}
