package nextstep.subway.exception.station;

public class NoStationException extends RuntimeException {

    private static final long serialVersionUID = 3150115850905140204L;
    public static final String NO_UPSTAION = "출발역이 존재하지 않습니다.";
    public static final String NO_DOWNSTATION = "도착역이 존재하지 않습니다.";

    public NoStationException() {
        super();
    }

    public NoStationException(String message) {
        super(message);
    }

}
