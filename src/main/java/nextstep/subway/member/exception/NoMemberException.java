package nextstep.subway.member.exception;

import static nextstep.subway.exception.ExceptionMessage.*;

import nextstep.subway.exception.BadRequestException;

public class NoMemberException extends BadRequestException {
    public NoMemberException(Long id) {
        super(noMember(id));
    }
}
