package nextstep.subway.path.domain;

import io.jsonwebtoken.lang.Collections;
import nextstep.subway.station.domain.Station;

import java.util.ArrayList;
import java.util.List;

public class ShortestPath {
    private final List<Station> stations = new ArrayList<>();
    private final PathDistance distance;

    public static ShortestPath of(List<Station> stations, Integer distance) {
        return new ShortestPath(stations, distance);
    }

    private ShortestPath(List<Station> stations, Integer distance) {
        validate(stations, distance);
        this.stations.addAll(stations);
        this.distance = new PathDistance(distance);
    }

    public List<Station> getStations() {
        return stations;
    }

    public int getDistance() {
        return distance.getDistance();
    }

    private void validate(List<Station> stations, Integer distance) {
        if (Collections.isEmpty(stations)) {
            throw new IllegalArgumentException("최단 경로의 지하철역이 존재하지 않습니다.");
        }
        if(distance == null) {
            throw new IllegalArgumentException("최단 경로의 거리가 존재하지 않습니다.");
        }
    }
}
