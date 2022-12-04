package nextstep.subway;

public class ErrorMessage {




    ErrorMessage() {
        throw new AssertionError();
    }



    public static String STATIONS_ALREADY_EXISTS = "이미 상행역과 하행역이 노선에 존재합니다.";
    public static String STATIONS_NOT_EXISTS = "상행역과 하행역이 모두 노선에 존재하지 않습니다.";

    public static final String CANNOT_DELETE_SECTION_WHEN_ONE = "구간이 하나인 경우에는 역을 삭제할 수 없습니다.";

    public static final String CANNOT_FIND_STATIONS_IN_LINE = "노선에서 요청한 역을 찾을 수 없습니다. ";

    public static final String FIND_PATH_OF_STATION_NOT_ON_GRAPH = "경로에 없는 역들간의 경로를 구할 수 없습니다.";

    public static final String FIND_PATH_NOT_CONNECTED = "연결되지 않은 역들간의 경로를 구할 수 없습니다.";

    public static final String FIND_PATH_SAME_STATION = "경로조회 시작역과 종료역이 같으면 경로를 구할 수 없습니다.";

    public static final String DUPLICATED_EDGE_ON_GRAPH = "경로에 이미 해당구간이 존재합니다.";

    public static String notValidDistance(int minimumDistance) {
        return String.format("거리는 %d보다 커야 합니다.", minimumDistance);
    }
    public static String notFoundEntity(String entityName, Long id) {
        return String.format("요청하신 엔티티를 찾을 수 없습니다. 엔티티명: [%s], 요청아이디 [%d]", entityName, id);
    }
}
