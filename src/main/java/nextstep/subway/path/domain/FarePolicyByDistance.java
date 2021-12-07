package nextstep.subway.path.domain;

public class FarePolicyByDistance implements FarePolicy {
	private static final int BASIC_FARE = 1250;

	@Override
	public int calculate(int distance) {
		if (!OverFareSection.contains(distance)) {
			return BASIC_FARE;
		}

		OverFareSection overFareSection = OverFareSection.findBy(distance);
		int totalOverFare = overFareSection.calculateTotalOverFare(distance);
		return BASIC_FARE + totalOverFare;
	}
}
