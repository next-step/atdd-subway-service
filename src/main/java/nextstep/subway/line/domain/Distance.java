package nextstep.subway.line.domain;

import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class Distance {

	private static final int DISTANCE_LESS_THAN_ZERO_NOT_ALLOWED = 0;
	private static final String COLUMN_DESCRIPTION = "구간의 간격";

	@Column
	private int distance;

	protected Distance() {
	}

	public Distance(int distance) {
		validateOverZero(distance);
		this.distance = distance;
	}

	public int value() {
		return distance;
	}

	private void validateOverZero(int distance) {
		if (distance <= DISTANCE_LESS_THAN_ZERO_NOT_ALLOWED) {
			throw new IllegalArgumentException(COLUMN_DESCRIPTION + "은 0을 초과하는 거리여야 합니다.");
		}
	}

	public int minus(int distance) {
		validateUnderDistance(distance);
		return this.distance - distance;
	}

	private void validateUnderDistance(int distance) {
		if (this.distance <= distance) {
			throw new RuntimeException("기존 " + COLUMN_DESCRIPTION + "보다 좁은 거리를 입력해주세요");
		}
	}

	public int plus(int distance) {
		return this.distance + distance;
	}

	@Override
	public boolean equals(Object object) {
		if (this == object) {
			return true;
		}
		if (!(object instanceof Distance)) {
			return false;
		}
		Distance compareDistance = (Distance)object;
		return distance == compareDistance.value();
	}

	@Override
	public int hashCode() {
		return Objects.hash(distance);
	}
}
