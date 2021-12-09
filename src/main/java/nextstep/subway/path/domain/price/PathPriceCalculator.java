package nextstep.subway.path.domain.price;

public class PathPriceCalculator implements PriceCalculator {

	@Override
	public int calculatePrice(int pathDistance) {
		return SectionPricePolicy.calculatePrice(pathDistance);
	}
}
