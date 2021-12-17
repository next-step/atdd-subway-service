package nextstep.subway.fare.domain;

public class Fare {

	private static final int BASIC_KRW = 1250;

	private static final double BASIC_MAX_KM_INCLUSIVE = 10;

	private static final double UNIT_KM_TO_CHARGE_EXTRA = 5;
	private static final int EXTRA_KRW = 100;
	private static final double EXTRA_MAX_KM_INCLUSIVE = 50;

	private static final double UNIT_KM_TO_CHARGE_OVER_EXTRA = 8;
	private static final int OVER_EXTRA_KRW = 100;

	public static int calculate(double distanceKm) {
		validate(distanceKm);
		if (distanceKm <= BASIC_MAX_KM_INCLUSIVE) {
			return BASIC_KRW;
		} else if (distanceKm <= EXTRA_MAX_KM_INCLUSIVE) {
			return BASIC_KRW + calculateExtraFare(distanceKm - BASIC_MAX_KM_INCLUSIVE);
		}
		return BASIC_KRW
			+ calculateExtraFare(EXTRA_MAX_KM_INCLUSIVE - BASIC_MAX_KM_INCLUSIVE)
			+ calculateOverExtraFare(distanceKm - EXTRA_MAX_KM_INCLUSIVE);
	}

	private static void validate(double distanceKm) {
		if (distanceKm <= 0) {
			throw new IllegalArgumentException("운임을 부과할 거리는 0km 보다 커야합니다.");
		}
	}

	private static int calculateExtraFare(double distanceKm) {
		return (int) ((Math.floor((distanceKm - 1) / UNIT_KM_TO_CHARGE_EXTRA) + 1) * EXTRA_KRW);
	}

	private static int calculateOverExtraFare(double distanceKm) {
		return (int) ((Math.floor((distanceKm - 1) / UNIT_KM_TO_CHARGE_OVER_EXTRA) + 1) * OVER_EXTRA_KRW);
	}
}
