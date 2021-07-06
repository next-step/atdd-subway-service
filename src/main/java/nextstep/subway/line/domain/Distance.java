package nextstep.subway.line.domain;

import javax.persistence.Embeddable;

@Embeddable
public class Distance {
	private int distance;

	protected Distance() {
	}

	public Distance(int distance) {
		if (distance < 1) {
			throw new IllegalArgumentException("구간은 1 이상의 거리를 가질 수 있습니다.");
		}
		this.distance = distance;
	}

	public int getDistance() {
		return distance;
	}

	public int calculatePlusDistance(Distance distance) {
		return this.distance + distance.distance;
	}

	public void minusDistance(Distance distance) {
		this.distance -= distance.distance;
	}

	public boolean isLessThanOrEqualTo(Distance distance) {
		return this.distance <= distance.distance;
	}

	public Double toWeight() {
		return Double.valueOf(this.distance);
	}
}
