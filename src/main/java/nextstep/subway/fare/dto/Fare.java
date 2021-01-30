package nextstep.subway.fare.dto;

public class Fare {
	private final long fare;

	private Fare(long fare) {
		this.fare = fare;
	}

	public static Fare from(long fare) {
		return new Fare(fare);
	}

	public long getFare() {
		return fare;
	}

	public Fare sum(Fare other) {
		return new Fare(this.fare + other.fare);
	}
}
