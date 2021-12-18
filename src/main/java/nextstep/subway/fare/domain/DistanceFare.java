package nextstep.subway.fare.domain;

public class DistanceFare {

	private static final int BASE_KRW = 0;
	private static final double BASE_MAX_KM_INCLUSIVE = 10;

	private static final double UNIT_KM_TO_CHARGE_EXTRA = 5;
	private static final int EXTRA_KRW = 100;
	private static final double EXTRA_MAX_KM_INCLUSIVE = 50;

	private static final double UNIT_KM_TO_CHARGE_OVER_EXTRA = 8;
	private static final int OVER_EXTRA_KRW = 100;

	public static Fare calculate(double distanceKm) {
		validate(distanceKm);
		if (distanceKm <= BASE_MAX_KM_INCLUSIVE) {
			return Fare.of(BASE_KRW);
		} else if (distanceKm <= EXTRA_MAX_KM_INCLUSIVE) {
			return Fare.of(BASE_KRW + calculateExtraFare(distanceKm - BASE_MAX_KM_INCLUSIVE));
		}
		return Fare.of(BASE_KRW
			+ calculateExtraFare(EXTRA_MAX_KM_INCLUSIVE - BASE_MAX_KM_INCLUSIVE)
			+ calculateOverExtraFare(distanceKm - EXTRA_MAX_KM_INCLUSIVE)
		);
	}

	private static void validate(double distanceKm) {
		if (distanceKm <= 0) {
			throw new IllegalArgumentException("운임료를 부과할 거리는 0km 보다 커야합니다.");
		}
	}

	private static int calculateExtraFare(double distanceKm) {
		return (int) ((Math.floor((distanceKm - 1) / UNIT_KM_TO_CHARGE_EXTRA) + 1) * EXTRA_KRW);
	}

	private static int calculateOverExtraFare(double distanceKm) {
		return (int) ((Math.floor((distanceKm - 1) / UNIT_KM_TO_CHARGE_OVER_EXTRA) + 1) * OVER_EXTRA_KRW);
	}
}
