package nextstep.subway.exception.path;

public class PathException extends RuntimeException {
    private static final long serialVersionUID = 3409699756854507009L;
    public static final String SAME_STATION = "검색하려는 두 역이 동일합니다. 다른 역을 입력하세요";
    public static final String NOT_CONNECTED = "출발역과 도착역이 연결되지 않았습니다.";
    public static final String NO_REGISTRATION = "검색하려는 역이 라인에 등록되어 있지 않습니다.";

    public PathException() {
        super();
    }

    public PathException(String message) {
        super(message);
    }
}
