package nextstep.subway.member.application.exception;

import nextstep.subway.common.exception.ErrorCode;

public enum FavoriteErrorCode implements ErrorCode {
    NOT_EMPTY("빈값을 입력 할 수 없습니다."),
    NOT_DUPLICATE("좋아요가 이미 존재합니다."),
    SOURCE_TARGET_NOT_SAME("좋아요 상행,하행이 같을 수 없습니다."),
    FAVORITE_STATION_NOT_FOUND("좋아요한 역을 찾을 수 없습니다."),
    ;

    private final String errorMessage;

    FavoriteErrorCode(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    @Override
    public String getErrorMessage() {
        return errorMessage;
    }
}
