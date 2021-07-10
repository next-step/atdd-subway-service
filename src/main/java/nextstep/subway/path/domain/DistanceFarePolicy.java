package nextstep.subway.path.domain;

/**
 * 기본운임(10㎞ 이내) : 기본운임 1,250원
 * 이용 거리초과 시 추가운임 부과
 * 	- 10km초과∼50km까지(5km마다 100원)
 * 	- 50km초과 시 (8km마다 100원)
 */
public class DistanceFarePolicy {
	private static final int BASIC_FARE = 1_250;

	private static final int FIRST_DISTANCE_STANDARD = 10;
	private static final int FIRST_UNIT = 5;

	private static final int SECOND_DISTANCE_STANDARD = 50;
	private static final int SECOND_UNIT = 8;

	public static int fare(Integer distance) {
		if (distance <= 0) {
			throw new IllegalArgumentException("거리가 불명확하여 요금 측정이 불가합니다.");
		}

		if (distance <= FIRST_DISTANCE_STANDARD) {
			return BASIC_FARE;
		}

		int fare = BASIC_FARE;

		if (SECOND_DISTANCE_STANDARD < distance) {
			fare += calculateOverFare(distance - SECOND_DISTANCE_STANDARD, SECOND_UNIT);
		}

		if (FIRST_DISTANCE_STANDARD < distance) {
			int betweenFirstToSecond = (distance - FIRST_DISTANCE_STANDARD) >= SECOND_DISTANCE_STANDARD - FIRST_DISTANCE_STANDARD
				? SECOND_DISTANCE_STANDARD - FIRST_DISTANCE_STANDARD : distance - FIRST_DISTANCE_STANDARD;
			fare += calculateOverFare(betweenFirstToSecond, FIRST_UNIT);
		}

		return fare;
	}

	private static int calculateOverFare(int distance, int km) {
		return (int)((Math.ceil((distance - 1) / km) + 1) * 100);
	}
}
