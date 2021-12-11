package nextstep.subway.path.domain;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;

import nextstep.subway.line.domain.Distance;
import nextstep.subway.member.domain.Age;

public class Price {

	public static final BigDecimal BASE_PRICE = new BigDecimal(1250);

	private BigDecimal price;

	protected Price() {
	}

	private Price(BigDecimal price) {
		this.price = price;
	}

	public static Price from() {
		return new Price(BASE_PRICE);
	}

	public static Price from(int price) {
		return new Price(new BigDecimal(price));
	}

	public static Price from(BigDecimal price) {
		return new Price(price);
	}

	public static Price from(Distance distance) {
		if (distance.isUnderEleven()) {
			return new Price(BASE_PRICE);
		}

		return new Price(generateOverPrice(distance));
	}

	public static Price of(Distance distance, List<LineWeightedEdge> edgeList) {
		if (distance.isUnderEleven()) {
			return new Price(BASE_PRICE.add(findMaxExtraPrice(edgeList)));
		}

		return new Price(generateOverPrice(distance).add(findMaxExtraPrice(edgeList)));
	}

	public static Price of(Distance distance, List<LineWeightedEdge> edgeList, Age age) {
		if (distance.isUnderEleven()) {
			return new Price(BASE_PRICE.add(findMaxExtraPrice(edgeList))).calculateDiscountAge(age);
		}

		return new Price(generateOverPrice(distance).add(findMaxExtraPrice(edgeList))).calculateDiscountAge(age);
	}

	private static BigDecimal generateOverPrice(Distance distance) {
		BigDecimal extraPrice = new BigDecimal(distance.calculateExtraPriceSize());
		extraPrice = extraPrice.multiply(PriceRate.MIN_RATE.getExtraPrice().price);
		extraPrice = extraPrice.add(BASE_PRICE);
		return extraPrice;
	}

	private static BigDecimal findMaxExtraPrice(List<LineWeightedEdge> edgeList) {
		return edgeList.stream()
			.max(Comparator.comparing(it -> it.getLine().getExtraPrice()))
			.map(it -> it.getLine().getExtraPrice())
			.orElse(BigDecimal.ZERO);
	}

	public Price calculateDiscountAge(Age age) {
		if (age.isChild()) {
			return Price.from(calculateDiscountAgePriceRate(AgePriceRate.CHILD));
		}

		if (age.isTeenager()) {
			return Price.from(calculateDiscountAgePriceRate(AgePriceRate.TEENAGER));
		}

		return Price.from(price);
	}

	private BigDecimal calculateDiscountAgePriceRate(AgePriceRate agePriceRate) {
		return price.subtract(agePriceRate.getDeductionPrice()).multiply(agePriceRate.getDiscountRate());
	}

	public BigDecimal getPrice() {
		return price;
	}

	public int getIntPrice() {
		return price.intValue();
	}
}
