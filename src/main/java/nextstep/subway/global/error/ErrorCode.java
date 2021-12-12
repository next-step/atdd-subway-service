package nextstep.subway.global.error;

public enum ErrorCode {

    // Common
    INVALID_INPUT_VALUE(400, "C001", " Invalid Input Value"),
    METHOD_NOT_ALLOWED(405, "C002", " Invalid Input Value"),
    ENTITY_NOT_FOUND(400, "C003", " Entity Not Found"),
    INTERNAL_SERVER_ERROR(500, "C004", "Server Error"),

    // Section
    DISTANCE_LESS_THAN_MINIMUM(400, "S001", "역과 역 사이의 거리는 최소 1 이상입니다."),
    DISTANCE_FEWER_THAN_MAXIMUM(400, "S002", "역과 역 사이의 거리보다 좁은 거리를 입력해주세요"),
    SECTION_NO_REGISTRATION(400, "S003", "등록할 수 없는 구간 입니다."),
    SECTION_ALREADY_REGISTER(400, "S004", "이미 등록된 구간 입니다."),
    NO_REGISTER_SECTION(400, "S005", "등록된 구간이 없습니다."),

    // Path
    SAME_DEPARTURE_AND_ARRIVAL_STATION(400, "P001", "출발역과 도착역이 동일합니다."),
    NOT_CONNECTED_STATION(400, "P002", "출발역과 도착역이 연결되어 있지 않습니다."),
    STATION_NOT_FOUND(400, "P003", "출발역 또는 도착역이 존재하지 않습니다");

    private final String code;
    private final String message;
    private int status;

    ErrorCode(final int status, final String code, final String message) {
        this.status = status;
        this.message = message;
        this.code = code;
    }

    public String getMessage() {
        return this.message;
    }

    public String getCode() {
        return code;
    }

    public int getStatus() {
        return status;
    }
}
