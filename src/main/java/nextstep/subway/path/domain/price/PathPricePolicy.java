package nextstep.subway.path.domain.price;

public enum PathPricePolicy {

	BASIC(0, 10, 10, 1250),
	MIDDLE(10, 50, 5, 100),
	HIGHEST(50, Integer.MAX_VALUE, 8, 100);

	private int preSectionUpperBound;
	private int upperDistanceBound;
	private int perDistance;
	private int pricePerDistance;

	PathPricePolicy(int preSectionUpperBound, int upperDistanceBound, int perDistance, int pricePerDistance) {
		this.preSectionUpperBound = preSectionUpperBound;
		this.upperDistanceBound = upperDistanceBound;
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
		if (pathDistance <= preSectionUpperBound) {
			return 0;
		}
		int sectionDistance = pathDistance <= upperDistanceBound ? pathDistance : upperDistanceBound;
		return sectionDistance - preSectionUpperBound;
	}
}
