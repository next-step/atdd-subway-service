package nextstep.subway.line.excpetion;

import static nextstep.subway.exception.ExceptionMessage.*;

import nextstep.subway.exception.BadRequestException;

public class LastSectionException extends BadRequestException {

    public LastSectionException() {
        super(LAST_SECTION);
    }
}
