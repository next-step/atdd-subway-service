package nextstep.subway.domain.path.exception;

import nextstep.subway.global.error.ErrorCode;
import nextstep.subway.global.exception.BusinessException;

public class NotConnectedStation extends BusinessException {

    public NotConnectedStation(final String message) {
        super(message, ErrorCode.NOT_CONNECTED_STATION);
    }

    public NotConnectedStation() {
        super(ErrorCode.NOT_CONNECTED_STATION);
    }
}
