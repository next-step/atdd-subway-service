package nextstep.subway.member.exception;

import static nextstep.subway.exception.ExceptionMessage.*;

import nextstep.subway.exception.BadRequestException;

public class NotOwnerException extends BadRequestException {
    public NotOwnerException() {
        super(NOT_OWNER_FAVORITE);
    }
}
