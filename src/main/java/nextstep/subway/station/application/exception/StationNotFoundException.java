package nextstep.subway.station.application.exception;

import nextstep.subway.common.NotFoundException;

public class StationNotFoundException extends NotFoundException {
    public StationNotFoundException() {
        super("지하철 역을 찾을 수 없습니다.");
    }

    public StationNotFoundException(String message) {
        super(message);
    }
}
