package nextstep.subway.exception;

public class DistanceException extends IllegalArgumentException {

    private static final String ROUTE_DISTANCE_ERROR = "구간거리는 %d보다 크거나 같아야합니다.";

    public DistanceException(int distance) {
        super(String.format(ROUTE_DISTANCE_ERROR, distance));
    }
}
