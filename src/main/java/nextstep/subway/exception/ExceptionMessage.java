package nextstep.subway.exception;

public class ExceptionMessage {

    private ExceptionMessage() {
    }


    public static final String INVALID_TOKEN = "유효하지 않은 토큰입니다.";
    public static final String LAST_SECTION = "마지막 구간은 삭제할 수 없습니다.";
    public static final String PATH_SAME_STATION = "출발지와 도착지가 같은 경우 경로조회할 수 없습니다.";
    public static final String PATH_NOT_CONNECTED = "연결되어 있지 않은 역은 경로를 구할 수 없습니다.";
    public static final String PATH_MUST_CONTAIN_GRAPH = "전체 구간에 존재하는 역만 경로 조회할 수 있습니다.";
    public static final String NOT_OWNER_FAVORITE = "사용자 자신의 즐겨찾기가 아닙니다.";
    public static final String NO_STATION = "해당 역은 존재하지 않습니다.";
    public static final String NO_MEMBER = "해당 사용자는 존재하지 않습니다.";
    public static final String DISTANCE_ERROR = "역과 역 사이의 거리보다 좁은 거리를 입력해주세요";

}
