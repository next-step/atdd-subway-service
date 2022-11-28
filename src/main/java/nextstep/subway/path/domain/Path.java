package nextstep.subway.path.domain;

import java.util.List;
import nextstep.subway.common.exception.InvalidParameterException;
import nextstep.subway.line.domain.Distance;
import nextstep.subway.station.domain.Station;

public class Path {
    private static final String ERROR_MESSAGE_EMPTY_STATIONS = "경로는 비어있을 수 없습니다.";

    private final List<Station> stations;
    private final Distance distance;

    private Path(List<Station> stations, Distance distance) {
        validPath(stations);

        this.stations = stations;
        this.distance = distance;
    }

    private void validPath(List<Station> stations) {
        if (stations.isEmpty()) {
            throw new InvalidParameterException(ERROR_MESSAGE_EMPTY_STATIONS);
        }
    }

    public static Path of(List<Station> stations, Distance distance) {
        return new Path(stations, distance);
    }

    public List<Station> stations() {
        return stations;
    }

    public int distance() {
        return distance.value();
    }
}
