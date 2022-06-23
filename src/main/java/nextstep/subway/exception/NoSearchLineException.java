package nextstep.subway.exception;

import java.util.NoSuchElementException;

public class NoSearchLineException extends NoSuchElementException {
    private static final String MESSAGE = "해당하는 지하철노선을 찾을 수 없습니다. (id: %d)";

    public NoSearchLineException(Long lineId) {
        super(String.format(MESSAGE, lineId));
    }
}
