package nextstep.subway.path.domain.price;

public class PathPrice {

	private int price;

	public PathPrice(int price) {

		this.price = price;
	}

	public static PathPrice calculatePriceFromPath(PriceCalculator priceCalculator, int distance){
		int price = priceCalculator.calculatePrice(distance);
		return new PathPrice(price);
	}

	public int getPrice() {
		return price;
	}
}
