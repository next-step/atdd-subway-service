package nextstep.subway.line.domain;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Embeddable;

@Access(AccessType.FIELD)
@Embeddable
public class Fare {

	private int fare;

	protected Fare() {
	}

	public Fare(final int fare) {
		this.fare = fare;
	}

	public int getFare() {
		return fare;
	}

	public Fare add(final Fare added) {
		return new Fare(this.fare + added.fare);
	}

}
