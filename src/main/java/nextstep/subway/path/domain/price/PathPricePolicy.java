package nextstep.subway.path.domain.price;

import java.util.Arrays;

public enum PathPricePolicy {

	NONE(null, 0, 0, 0),
	BASIC(NONE, 10, 10, 1250),
	MIDDLE(BASIC, 50, 5, 100),
	HIGHEST(MIDDLE, Integer.MAX_VALUE, 8, 100);

	private PathPricePolicy preSectionPolicy;
	private int upperDistanceBound;
	private int perDistance;
	private int pricePerDistance;

	PathPricePolicy(PathPricePolicy preSectionPolicy, int upperDistance, int perDistance, int pricePerDistance) {
		this.preSectionPolicy = preSectionPolicy;
		this.upperDistanceBound = upperDistance;
		this.perDistance = perDistance;
		this.pricePerDistance = pricePerDistance;
	}

	public int getUpperDistanceBound() {
		return upperDistanceBound;
	}

	public int calculateSectionPrice(int pathDistance) {
		int sectionDistance = calculateSectionDistance(pathDistance);
		return sectionDistance <= 0 ? 0 :
			(int)Math.ceil((double)sectionDistance / perDistance) * pricePerDistance;
	}

	private int calculateSectionDistance(int pathDistance) {
		if (pathDistance <= preSectionPolicy.getUpperDistanceBound()) {
			return 0;
		}
		int sectionDistance = pathDistance <= upperDistanceBound ? pathDistance : upperDistanceBound;
		return sectionDistance - preSectionPolicy.getUpperDistanceBound();
	}
}
