package nextstep.subway.line.exception;

public class InvalidPathException extends RuntimeException {
    public static final String SOURCE_AND_TARGET_EQUAL = "출발역과 도착역이 같을 수 없습니다";
    public static final String SOURCE_AND_TARGET_NOT_CONNECTED = "출발역과 도착역이 연결되지 않았습니다";
    public static final String STATION_NOT_EXISTS = "존재하지 않는 출발역(또는 도착역) 입니다";

    public InvalidPathException(String message) {
        super(message);
    }
}
