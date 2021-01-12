package nextstep.subway.path.application;

import nextstep.subway.station.domain.Station;

public class NotConnectedExeption extends RuntimeException{
    public NotConnectedExeption(Station start, Station end) {
        super("연결되어 있지 않는 역입니다. 출발역: " + start.getName() + " 도착역: " + end.getName());
    }
}
