package nextstep.subway.line.application.exception;

import nextstep.subway.common.exception.ErrorCode;

public enum SectionErrorCode implements ErrorCode {

    SECTION_EXIST("이미 등록된 구간 입니다."),
    SECTION_ADD_NO_POSITION("등록할 수 없는 구간 입니다."),
    DISTANCE_RANGE("역과 역 사이의 거리보다 좁은 거리를 입력해주세요."),
    SECTION_ONE_COUNT_CAN_NOT_REMOVE("구간이 하나 일 경우 제거 할 수 없습니다."),
    ;

    private final String errorMessage;

    SectionErrorCode(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    @Override
    public String getErrorMessage() {
        return errorMessage;
    }
}
