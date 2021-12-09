package nextstep.subway.path.domain;

public class PerOverchargeFareSectionDistanceBasedFarePolicy implements DistanceBasedFarePolicy {
	@Override
	public int calculate(int basicFare, int distance) {
		if (!OverchargeFareSection.contains(distance)) {
			return basicFare;
		}

		OverchargeFareSection overchargeFareSection = OverchargeFareSection.findBy(distance);
		int totalOverFare = overchargeFareSection.calculateTotalOverFare(distance);
		return basicFare + totalOverFare;
	}
}
