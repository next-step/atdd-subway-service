package nextstep.subway.exception;

import java.util.NoSuchElementException;

public class NoSearchStationException extends NoSuchElementException {
    private static final String MESSAGE = "해당하는 지하철역을 찾을 수 없습니다. (id: %d)";

    public NoSearchStationException(Long stationId) {
        super(String.format(MESSAGE, stationId));
    }
}
