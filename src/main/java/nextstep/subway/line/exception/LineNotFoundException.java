package nextstep.subway.line.exception;

import static nextstep.subway.common.Message.MESSAGE_LINE_NOT_FOUND;

import nextstep.subway.common.NoResultDataException;

public class LineNotFoundException extends NoResultDataException {

    public LineNotFoundException() {
        super(MESSAGE_LINE_NOT_FOUND.getMessage());
    }

    public LineNotFoundException(String message) {
        super(message);
    }
}
