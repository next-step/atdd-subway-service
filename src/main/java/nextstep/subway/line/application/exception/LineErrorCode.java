package nextstep.subway.line.application.exception;

import nextstep.subway.common.exception.ErrorCode;

public enum LineErrorCode implements ErrorCode {
    LINE_NOT_FOUND("노선을 찾을 수 없습니다."),
    ;

    private final String errorMessage;

    LineErrorCode(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    @Override
    public String getErrorMessage() {
        return errorMessage;
    }
}
