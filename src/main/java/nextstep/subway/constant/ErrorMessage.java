package nextstep.subway.constant;

public class ErrorMessage {
    protected ErrorMessage() {
    }

    public static final String NOT_FOUND_STATION = "[ERROR] 역을 찾을 수 없습니다.";
    public static final String NOT_FOUND_LINE = "[ERROR] 호선을 찾을 수 없습니다.";
    public static final String ALREADY_EXIST_SECTION = "[ERROR] 이미 등록된 구간 입니다.";
    public static final String CAN_NOT_ADD_SECTION = "[ERROR] 등록할 수 없는 구간 입니다.";
    public static final String NOT_FOUND_SECTION_STATION = "[ERROR] 구간에 등록된 역이 없습니다.";
    public static final String EMPTY_SECTION = "[ERROR] 등록된 구간이 없습니다.";
    public static final String SAME_STATION = "[ERROR] 출발역과 도착역이 같습니다.";
    public static final String NOT_CONNECTED_STATION = "[ERROR] 출발역과 도착역이 연결 되어 있지 않습니다.";
}
