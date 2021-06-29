package nextstep.subway.path.domain;

import nextstep.subway.station.domain.Station;

import java.util.Objects;

public class Direction {
    private static final String EQUAL_STATION_EXCEPTION = "같은 역으로 경로를 조회할 수 없습니다.";
    private static final String UNKNOWN_STATION_EXCEPTION = "기존에 등록되지 않은 역입니다.";

    private final Station source;
    private final Station target;

    public Direction(Station source, Station target) {
        validateNullStation(source, target);
        validateEqualStations(source, target);
        this.source = source;
        this.target = target;
    }

    private void validateEqualStations(Station source, Station target) {
        if (source.equals(target)) {
            throw new IllegalArgumentException(EQUAL_STATION_EXCEPTION);
        }
    }

    private void validateNullStation(Station source, Station target) {
        if (Objects.isNull(source) || Objects.isNull(target)) {
            throw new IllegalArgumentException(UNKNOWN_STATION_EXCEPTION);
        }
    }

    public Station getSource() {
        return source;
    }

    public Station getTarget() {
        return target;
    }
}
