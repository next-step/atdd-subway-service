package nextstep.subway.path.domain;

public class PathPrice {

	private int price;

	public PathPrice(int price) {
		this.price = price;
	}

	public static PathPrice  calculatePriceFromPath(int distance){
		int price = PathPricePolicy.calculatePrice(distance);
		return new PathPrice(price);
	}

	public int getPrice() {
		return price;
	}
}
