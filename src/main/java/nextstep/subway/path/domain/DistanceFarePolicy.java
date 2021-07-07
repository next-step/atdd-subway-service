package nextstep.subway.path.domain;

/**
 * 기본운임(10㎞ 이내) : 기본운임 1,250원
 * 이용 거리초과 시 추가운임 부과
 * 	- 10km초과∼50km까지(5km마다 100원)
 * 	- 50km초과 시 (8km마다 100원)
 */
public class DistanceFarePolicy {
	private static final int BASIC_FARE = 1_250;

	private final int distance;

	public DistanceFarePolicy(int distance) {
		if (distance <= 0) {
			throw new IllegalArgumentException("거리가 불명확하여 요금 측정이 불가합니다.");
		}
		this.distance = distance;
	}

	public int fare() {
		if (distance <= 10) {
			return BASIC_FARE;
		}

		int fare = BASIC_FARE;

		if (50 < distance) {
			fare += calculateOverFare(distance - 50, 8);
		}

		if (10 < distance) {
			int _distance = (distance - 10) >= 40 ? 40 : distance - 10;
			fare += calculateOverFare(_distance, 5);
		}

		return fare;
	}

	private int calculateOverFare(int distance, int km) {
		return (int)((Math.ceil((distance - 1) / km) + 1) * 100);
	}
}
