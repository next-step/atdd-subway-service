package nextstep.subway.common;

public enum ErrorMessage {
    SAME_SOURCE_AND_TARGET("존재하지 않는 노선입니다."),
    SAME_CAN_NOT_SAME("동일한 역은 처리할 수 없습니다."),
    STATION_NOT_FOUND("구간에 존재하지 않는 역입니다."),
    NOT_CONNECTED("연결되지 않은 역입니다."),
    UNAUTHORIZED("접근 권한이 없습니다."),
    FAVORITE_DUPLICATION("동일한 즐겨찾기는 등록할 수 없습니다."),
    FAVORITE_NOT_FOUND("등록된 즐겨찾기를 찾을 수 없습니다."),
    FAVORITE_CAN_NOT_DELETE("선택된 즐겨찾기는 삭제할 수 없습니다.");

    private final String message;

    ErrorMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
