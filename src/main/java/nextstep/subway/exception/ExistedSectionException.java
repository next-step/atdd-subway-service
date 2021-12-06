package nextstep.subway.exception;

import nextstep.subway.station.domain.Station;

public class ExistedSectionException extends RuntimeException {

    public ExistedSectionException(Station upStation, Station downStation) {
        super("상행역 : " + upStation.getName() + ", 하행역 : " + downStation.getName());
    }

    @Override
    public Throwable fillInStackTrace() {
        return this;
    }
}
