package nextstep.subway.path.application;

import nextstep.subway.station.domain.Station;

public class SameStartAndEndException extends RuntimeException{
    public SameStartAndEndException(Station station) {
        super("출발역과 도착역이 같습니다. 역: " + station.getName());
    }
}
