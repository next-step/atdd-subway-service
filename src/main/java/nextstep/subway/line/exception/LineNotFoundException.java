package nextstep.subway.line.exception;

import javax.persistence.EntityNotFoundException;

import static java.lang.String.format;

public class LineNotFoundException extends EntityNotFoundException {
    public LineNotFoundException() {
    }

    public LineNotFoundException(String message) {
        super(message);
    }

    public LineNotFoundException(Long id) {
        super(format("%d인 노선을 찾을 수 없습니다.", id));
    }
}
