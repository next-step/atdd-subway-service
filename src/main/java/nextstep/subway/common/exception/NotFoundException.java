package nextstep.subway.common.exception;

import nextstep.subway.common.CustomException;

public class NotFoundException extends CustomException {
    private static final String NOT_FOUND = "해당 정보는 존재하지 않습니다.";

    public NotFoundException() {
        super(NOT_FOUND);
    }
}
