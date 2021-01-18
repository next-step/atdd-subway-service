package nextstep.subway.line.domain;

import javax.persistence.Embeddable;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Embeddable
public class Distance {

	private static final int DISTANCE_LIMIT = 1;

	private int distance;

	public Distance(int distance) {
		validateDistance(distance);
		this.distance = distance;
	}

	public static Distance of(int weight) {
		return new Distance(weight);
	}

	public void update(int newDistance) {
		if (this.distance <= newDistance) {
			throw new RuntimeException("역과 역 사이의 거리보다 좁은 거리를 입력해주세요");
		}
		this.distance -= newDistance;
	}

	public int minus(int minimumDistance) {
		return this.distance - minimumDistance;
	}

	private void validateDistance(int distance) {
		if (distance < DISTANCE_LIMIT) {
			throw new IllegalArgumentException("거리는 최소 1 이상 이여야 합니다.");
		}
	}
}
