package nextstep.subway.path.domain;

import java.util.List;
import nextstep.subway.station.domain.Station;

/**
 * 경로 검색 결과를 리턴하는 도메인 객체 (도메인에서 DTO를 의존하는 상황을 피하기 위함)
 */
public class PathFindResult {
    private List<Station> stations;

    private int distance;

    protected PathFindResult() {

    }

    public PathFindResult(List<Station> stations, int distance) {
        this.stations = stations;
        this.distance = distance;
    }

    public List<Station> getStations() {
        return stations;
    }

    public int getDistance() {
        return distance;
    }
}
