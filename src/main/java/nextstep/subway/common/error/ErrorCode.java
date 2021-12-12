package nextstep.subway.common.error;

public enum ErrorCode {

    // common
    INVALID_INPUT_VALUE(400, "C001", "Invalid Input Value"),
    ENTITY_NOT_FOUND(400, "C002", "Entity not found"),

    // line
    LINE_NOT_FOUND(500, "L001", "노선을 찾을 수 없습니다."),
    DISTANCE_TOO_LONG(500, "L002", "역 사이의 거리가 너무깁니다."),
    SECTION_NOT_REMOVABLE(500, "L003", "구간을 제거할 수 없습니다."),
    SECTION_CANNOT_ADD(500, "L004", "등록할 수 없는 구간 입니다."),
    SECTION_DUPLICATED(500, "L005", "이미 등록된 구간 입니다."),

    // path
    PATH_NOT_FOUND(500, "P001", "경로를 찾을 수 없습니다."),
    INVALID_PATH_STATION_VALUE(500, "S001", "경로를 찾기 위한 지하철 역 입력이 잘못되었습니다.");

    private final int status;
    private final String code;
    private final String message;

    ErrorCode(final int status, final String code, final String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }

    public int getStatus() {
        return status;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
