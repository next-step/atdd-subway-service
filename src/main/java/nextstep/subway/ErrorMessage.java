package nextstep.subway;

public class ErrorMessage {
    ErrorMessage() {
        throw new AssertionError();
    }



    public static String STATIONS_ALREADY_EXISTS = "이미 상행역과 하행역이 노선에 존재합니다.";
    public static String STATIONS_NOT_EXISTS = "상행역과 하행역이 모두 노선에 존재하지 않습니다.";

    public static final String CANNOT_DELETE_SECTION_WHEN_ONE = "구간이 하나인 경우에는 역을 삭제할 수 없습니다.";
    private static final String CANNOT_FIND_LINE = "번 노선을 찾을 수 없습니다.";
    public static String notFoundLine(Long id) {
        return id + CANNOT_FIND_LINE;
    }

    public static String notValidDistance(int minimumDistance) {
        return String.format("거리는 %d보다 커야 합니다.", minimumDistance);
    }
}
