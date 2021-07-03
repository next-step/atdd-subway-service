package nextstep.subway.advice.exception;

import nextstep.subway.station.domain.Station;

public class SectionBadRequestException extends RuntimeException {

    public SectionBadRequestException(String message, Station upStation, Station downStation, int distance) {
        super(String.format(message + " (상행id:%d, 하행id:%d, 거리:%d)", upStation.getId(), downStation.getId(), distance));
    }

    public SectionBadRequestException(String message, int curDistance, int distance) {
        super(String.format(message + " (추가하려는 거리:%d, 현재 역의 거리:%d)", curDistance, distance));
    }

    public SectionBadRequestException(String message, Station station) {
        super(String.format(message + " (역id:%d)", station.getId()));
    }
}
