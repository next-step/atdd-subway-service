package nextstep.subway.favorite.exception;

import nextstep.subway.station.domain.Station;

import static java.lang.String.format;

public class SameSourceTargetStationException extends IllegalArgumentException {
    public SameSourceTargetStationException(Station source, Station target) {
        super(format("출발지 역인 %s와 도착지 역인 %s는 같은 수 없습니다.", source.getName(), target.getName()));
    }
}
