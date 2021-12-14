package nextstep.subway.common.exception;

import nextstep.subway.common.CustomException;

public class OneSectionDeleteException extends CustomException {
    private static final String NOT_ONE_SECTION_DELETE = "구간이 하나인 경우 삭제할 수 없습니다.";

    public OneSectionDeleteException() {
        super(NOT_ONE_SECTION_DELETE);
    }
}
