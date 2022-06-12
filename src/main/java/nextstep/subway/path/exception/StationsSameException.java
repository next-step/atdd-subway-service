package nextstep.subway.path.exception;

public class StationsSameException extends RuntimeException {

    private static final String STATIONS_IS_SAME = "stations is same.";

    public StationsSameException() {
        super(STATIONS_IS_SAME);
    }
}
