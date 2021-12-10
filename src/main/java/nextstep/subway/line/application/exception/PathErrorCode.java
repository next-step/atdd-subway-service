package nextstep.subway.line.application.exception;

import nextstep.subway.common.exception.ErrorCode;

public enum PathErrorCode implements ErrorCode {
    NOT_EMPTY("역 정보가 빈값을 수 없습니다."),
    PATH_NOT_CONNECT("조회 역이 연결되어 있지 않습니다."),
    PATH_IN_OUT_SAME("출발지 도착지가 같을 수 없습니다."),
    PATH_IN_OUT_NOT_FOUND("조회역을 찾을 수 없습니다."),
    ;

    private final String errorMessage;

    PathErrorCode(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    @Override
    public String getErrorMessage() {
        return errorMessage;
    }
}
