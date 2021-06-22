package nextstep.subway.line.domain;

import java.util.Objects;

import javax.persistence.Embeddable;

import nextstep.subway.station.domain.Station;

@Embeddable
public class Distance {

	private static final int MIN_DISTANCE = 0;

	private int distance;

	protected Distance() {

	}

	public Distance(int distance) {
		validate(distance);
		this.distance = distance;
	}

	private void validate(int distance) {
		if (distance <= MIN_DISTANCE)
			throw new RuntimeException("거리는 0보다 같거나 작을 수 없습니다.");
	}

	public Distance plus(Distance distance) {
		return new Distance(this.distance + distance.distance);
	}

	public Distance minus(Distance distance) {
		if(isLessThanOrEqual(distance)) {
			throw new RuntimeException("역과 역 사이의 거리보다 좁은 거리를 입력해주세요");
		}
		return new Distance(this.distance - distance.distance);
	}

	private boolean isLessThanOrEqual(Distance distance) {
		return this.distance <= distance.distance;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null || getClass() != obj.getClass())
			return false;
		Distance distance = (Distance)obj;
		return this.distance == distance.distance;
	}
}