package nextstep.subway.line.exeption;

public class LimitDistanceException extends RuntimeException {

    public static final String MESSAGE = "거리가 기준 거리 이하가 될 수 없습니다.";

    public LimitDistanceException(int min) {
        super(MESSAGE + " (기준 거리 : " + min + ")");
    }
}
