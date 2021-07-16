package nextstep.subway.path.exception;

import nextstep.subway.station.domain.Stations;

import static java.lang.String.format;

public class CannotCalculateAdditionalFareException extends IllegalStateException {

    public CannotCalculateAdditionalFareException(Stations stations) {
        super(format("최단 경로에 대한 추가 요금 계산에 실패했습니다. | Stations :  %s", stations));
    }
}
