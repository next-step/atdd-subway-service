package nextstep.subway.path.dto;

import java.util.Collections;
import java.util.List;

import nextstep.subway.line.domain.Distance;
import nextstep.subway.station.domain.Station;

public class ShortestPathInfo {
    List<PathAnalysisKey> stations;
    Distance distance;

    private ShortestPathInfo(List<PathAnalysisKey> stations, Distance distance) {
        this.stations = stations;
        this.distance = distance;
    }

    public static ShortestPathInfo of(List<PathAnalysisKey> stations, Distance distance) {
        return new ShortestPathInfo(stations, distance);
    }

    public List<PathAnalysisKey> getPathAnalysisKeys() {
        return Collections.unmodifiableList(this.stations);
    }

    public Distance getDistance() {
        return this.distance;
    }
}
