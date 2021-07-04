package nextstep.subway.fare.domain;

import javax.persistence.Embeddable;

import nextstep.subway.fare.exception.FareException;

@Embeddable
public class Fare {

	private int fare;

	protected Fare() {

	}

	public Fare(int fare) {
		validate(fare);
		this.fare = fare;
	}

	public int value() {
		return fare;
	}

	public Fare plus(Fare otherFare) {
		return new Fare(this.fare + otherFare.fare);
	}

	private void validate(int fare) {
		if(fare < 0) {
			throw new FareException("요금은 0 보다 작을 수 없습니다.");
		}
	}

	public Fare minus(Fare otherFare) {
		return new Fare(this.fare - otherFare.fare);
	}
}
