package nextstep.subway.common.exception;

public enum ErrorCode {

    STATION_NOT_FOUND("station not found."),
    MEMBER_NOT_FOUND("member not found."),
    SECTION_NOT_FOUND("section not found."),
    FAVORITE_NOT_FOUND("favorite not found.");

    private String message;

    ErrorCode(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
