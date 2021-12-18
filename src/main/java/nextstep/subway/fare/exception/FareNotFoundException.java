package nextstep.subway.fare.exception;

public class FareNotFoundException extends RuntimeException {

	public static final String MESSAGE = "운임료가 존재하지 않습니다.";

	public FareNotFoundException() {
		super(MESSAGE);
	}
}
