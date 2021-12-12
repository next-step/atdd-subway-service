package nextstep.subway.station.application.exception;

import nextstep.subway.common.exception.ErrorCode;

public enum StationErrorCode implements ErrorCode {
    SECTION_NOT_FOUND("지하철 역을 찾을 수 없습니다."),
    ;

    private final String errorMessage;

    StationErrorCode(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    @Override
    public String getErrorMessage() {
        return null;
    }
}
