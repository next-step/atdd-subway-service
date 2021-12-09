package nextstep.subway.path.domain.price;

import java.util.Set;

import nextstep.subway.line.domain.Line;

public class PathPrice {

	private int price;

	public PathPrice(int price) {
		this.price = price;
	}

	public static PathPrice calculatePrice(int distance, PriceCalculator priceCalculator) {
		int price = priceCalculator.calculatePrice(distance);
		return new PathPrice(price);
	}

	public static PathPrice calculatePrice(int distance, PriceCalculator priceCalculator, Integer age) {
		if (age == null) {
			return calculatePrice(distance, priceCalculator);
		}
		int price = priceCalculator.calculatePrice(distance);
		int discountedPrice = priceCalculator.adjustAgeDiscount(price, age);
		return new PathPrice(discountedPrice);
	}

	public int getPrice() {
		return price;
	}

	public void addLineExtraPrice(Set<Line> lines) {
		price += lines.stream()
			.mapToInt(Line::getExtraPrice)
			.sum();
	}
}
