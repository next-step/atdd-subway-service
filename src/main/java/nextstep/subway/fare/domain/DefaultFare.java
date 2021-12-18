package nextstep.subway.fare.domain;

public class DefaultFare {

	private static final int KRW = 1250;

	private DefaultFare() {
	}

	public static Fare calculate() {
		return Fare.of(KRW);
	}
}
