package nextstep.subway.path.domain.price;

import java.util.List;
import java.util.Set;

import nextstep.subway.line.domain.Line;

public class PathPrice {

	private int price;

	public PathPrice(int price) {

		this.price = price;
	}

	public static PathPrice calculatePriceFromPath(PriceCalculator priceCalculator, int distance) {
		int price = priceCalculator.calculatePrice(distance);
		return new PathPrice(price);
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
