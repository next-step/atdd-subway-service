package nextstep.subway.line.domain;

import javax.persistence.Embeddable;

@Embeddable
public class Distance {

	private int distance;

	public Distance() {
	}

	public Distance(int distance) {
		this.distance = distance;
	}

	public Distance minus(int distance) {
		return new Distance(this.distance - distance);
	}

	public boolean isLessThanOrEqualTo(int distance) {
		return this.distance <= distance;
	}

	public int getDistance() {
		return distance;
	}
}
