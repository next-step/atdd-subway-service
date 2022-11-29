package nextstep.subway.message;

public class ExceptionMessage {

    public static final String DUPLICATED_SECTION = "이미 등록된 구간 입니다.";
    public static final String INVALID_SECTION = "등록할 수 없는 구간 입니다.";
    public static final String EMPTY_SECTION = "지하철 구간이 비어있습니다.";
    public static final String INVALID_SECTION_DISTANCE = "역과 역 사이의 거리보다 좁은 거리를 입력해주세요";
    public static final String EMPTY_LINE_NAME = "지하철 노선 이름은 필수값입니다.";
    public static final String EMPTY_LINE_COLOR = "지하철 노선 색상은 필수값입니다.";
    public static final String EMPTY_STATION_NAME = "지하철 역 이름은 필수값입니다.";
    public static final String STATION_NOT_EXIST = "지하철역이 존재하지 않습니다.";
    public static final String LINE_NOT_EXIST = "지하철 노선이 존재하지 않습니다.";
    public static final String SOURCE_AND_TARGET_EQUAL = "출발역과 도착역이 같을 경우 최단경로를 찾을 수 없습니다.";
    public static final String SOURCE_NOT_CONNECTED_TO_TARGET = "출발역과 도착역이 연결되어 있지 않습니다.";
    public static final String INVALID_TOKEN = "유효하지 않은 토큰입니다.";
    public static final String MEMBER_NOT_EXIST = "회원이 존재하지 않습니다.";
    public static final String INVALID_PASSWORD = "비밀번호가 일치하지 않습니다.";
    public static final String FAVORITE_NOT_HAVE_STATION = "즐겨찾기에 출발역 또는 도착역 정보가 없습니다.";
    public static final String FAVORITE_NOT_HAVE_MEMBER = "즐겨찾기에 회원 정보가 없습니다.";
    public static final String FAVORITE_NOT_EXIST = "즐겨찾기가 존재하지 않습니다.";

    private ExceptionMessage() {
    }
}
