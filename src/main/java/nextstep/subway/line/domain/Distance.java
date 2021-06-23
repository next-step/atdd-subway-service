package nextstep.subway.line.domain;

import javax.persistence.Embeddable;

@Embeddable
public class Distance {

	private int distance;

	protected Distance() {
	}

	public Distance(int distance) {
		this.distance = distance;
	}

	public int getDistance() {
		return distance;
	}

	public boolean isSmallerThan(Distance distance) {
		return this.distance <= distance.getDistance();
	}

	public void reduce(Distance distance) {
		this.distance -= distance.getDistance();
	}

	public int addDistance(Distance distance) {
		return this.distance + distance.getDistance();
	}
}
