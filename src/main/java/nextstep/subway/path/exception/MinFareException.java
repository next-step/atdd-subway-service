package nextstep.subway.path.exception;

public class MinFareException extends RuntimeException {

    public static final String MESSAGE = "기준 요금 이하가 될 수 없습니다.";

    public MinFareException(long minFare) {
        super(MESSAGE + " (최소 요금 : " + minFare + ")");
    }
}
