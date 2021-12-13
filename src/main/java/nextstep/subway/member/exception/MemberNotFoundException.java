package nextstep.subway.member.exception;

import nextstep.subway.common.error.ErrorCode;
import nextstep.subway.common.error.exception.EntityNotFoundException;

public class MemberNotFoundException extends EntityNotFoundException {

    public MemberNotFoundException() {
        super(ErrorCode.MEMBER_NOT_FOUND);
    }
}
