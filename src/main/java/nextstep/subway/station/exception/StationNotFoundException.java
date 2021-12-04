package nextstep.subway.station.exception;

import nextstep.subway.common.exception.NotFoundException;

public class StationNotFoundException extends NotFoundException {
    public static final String DEFAULT_MESSAGE = "해당 역을 찾을 수 없습니다. id : %d";
    public StationNotFoundException(Long id) {
        super(String.format(DEFAULT_MESSAGE, id));
    }
}
