package nextstep.subway.fare.domain;

import nextstep.subway.fare.exception.FareException;

public class Fare {

	private int fare;

	public Fare(int fare) {
		this.fare = fare;
	}

	public int value() {
		return fare;
	}

	private void validate(int fare) {
		if(fare < 0) {
			throw new FareException("요금은 0 보다 작을 수 없습니다.");
		}
	}
}
