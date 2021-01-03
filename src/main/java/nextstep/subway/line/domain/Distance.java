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

	public int get() {
		return distance;
	}

	public void calculateNewDistance(int newDistance) {
		if (this.distance <= newDistance) {
			throw new RuntimeException("역과 역 사이의 거리보다 좁은 거리를 입력해주세요");
		}
		this.distance -= newDistance;
	}
}
