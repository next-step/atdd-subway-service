package nextstep.subway.fare.domain;

import java.util.Objects;

import javax.persistence.Embeddable;

@Embeddable
public class Fare {

	private int fare;

	protected Fare() {
	}

	private Fare(int fare) {
		validate(fare);
		this.fare = fare;
	}

	private void validate(int fare) {
		if (fare < 0) {
			throw new IllegalArgumentException("운임료는 0원 이상이여야 합니다.");
		}
	}

	public static Fare of(int fare) {
		return new Fare(fare);
	}

	public int getFare() {
		return fare;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (!(o instanceof Fare)) {
			return false;
		}
		Fare fare1 = (Fare)o;
		return fare == fare1.fare;
	}

	@Override
	public int hashCode() {
		return Objects.hash(fare);
	}
}
