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

	private void validate(int distance) {
		if(distance <= 0)
			throw new RuntimeException("거리는 0보다 같거나 작을 수 없습니다.");
	}

	public Distance plus(Distance distance) {
		return new Distance(this.distance + distance.distance);
	}

	public Distance minus(Distance distance) {
		return new Distance(this.distance - distance.distance);
	}

	public boolean isLessthanOrEqual(Distance newDistance) {
		return this.distance <= newDistance.distance;
	}
}